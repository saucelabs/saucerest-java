package com.saucelabs.saucerest.api;

import com.saucelabs.saucerest.*;
import com.saucelabs.saucerest.model.AbstractModel;
import com.squareup.moshi.*;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import okhttp3.*;
import okio.BufferedSink;
import okio.Okio;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.saucelabs.saucerest.api.ResponseHandler.responseHandler;

public abstract class AbstractEndpoint extends AbstractModel {
    private static final Logger logger = LogManager.getLogger();
    private static final String JSON_MEDIA_TYPE = "application/json";
    private static final int MAX_RETRIES = 5;
    private static final int BACKOFF_INITIAL_DELAY = 30;
    private static final int BACKOFF_MULTIPLIER = 500;
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
        .connectTimeout(300, TimeUnit.SECONDS)
        .readTimeout(300, TimeUnit.SECONDS)
        .writeTimeout(300, TimeUnit.SECONDS)
        .build();
    protected final String userAgent = "SauceREST/" + BuildUtils.getCurrentVersion();
    protected final String baseURL;
    protected final String username;
    protected final String accessKey;
    protected final String credentials;

    protected AbstractEndpoint(DataCenter dataCenter) {
        this.username = System.getenv("SAUCE_USERNAME");
        this.accessKey = System.getenv("SAUCE_ACCESS_KEY");
        this.credentials = initializeCredentials();
        this.baseURL = dataCenter.apiServer;
    }

    protected AbstractEndpoint(String apiServer) {
        this.username = System.getenv("SAUCE_USERNAME");
        this.accessKey = System.getenv("SAUCE_ACCESS_KEY");
        this.credentials = initializeCredentials();
        this.baseURL = apiServer;
    }

    protected AbstractEndpoint(String username, String accessKey, DataCenter dataCenter) {
        this.username = username;
        this.accessKey = accessKey;
        this.credentials = initializeCredentials();
        this.baseURL = dataCenter.apiServer;
    }

    protected AbstractEndpoint(String username, String accessKey, String apiServer) {
        this.username = username;
        this.accessKey = accessKey;
        this.credentials = initializeCredentials();
        this.baseURL = apiServer;
    }

    private String initializeCredentials() {
        if (username == null || accessKey == null) {
            logger.warn("Credentials are null. Please set the SAUCE_USERNAME and SAUCE_ACCESS_KEY environment variables.");
            return null;
        }
        return Credentials.basic(username, accessKey);
    }

    protected String getBaseEndpoint() {
        return baseURL;
    }

    /**
     * Build a URL with query parameters.
     *
     * @param url    Sauce Labs API endpoint
     * @param params Query parameters for GET request. If null, no query parameters will be added. Can be a string or an array of strings.
     * @return URL with query parameters
     */
    private String buildUrl(String url, Map<String, Object> params) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();

        if (params != null) {
            for (Map.Entry<String, Object> param : params.entrySet()) {
                String key = param.getKey();
                Object value = param.getValue();

                if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                    continue; // skip null or empty values
                }

                if (value instanceof String[]) {
                    for (String element : (String[]) value) {
                        urlBuilder.addQueryParameter(key, element);
                    }
                } else if (value instanceof Object[]) {
                    for (Object element : (Object[]) value) {
                        urlBuilder.addQueryParameter(key, element.toString());
                    }
                } else {
                    urlBuilder.addQueryParameter(key, value.toString());
                }
            }
        }

        return urlBuilder.build().toString();
    }

    public Response request(String url, HttpMethod httpMethod) throws IOException {
        return request(url, httpMethod, (String) null);
    }

    public Response request(String url, HttpMethod httpMethod, Map<String, Object> body) throws IOException {
        return request(url, httpMethod, new JSONObject(body).toString());
    }

    public Response request(String url, HttpMethod httpMethod, String body) throws IOException {
        Request request = createRequest(url, httpMethod, body);
        return makeRequest(request);
    }

    public Response requestWithQueryParameters(String url, HttpMethod httpMethod, Map<String, Object> params) throws IOException {
        return request(buildUrl(url, params), httpMethod);
    }

    private Request createRequest(String url, HttpMethod httpMethod, String body) {
        Request.Builder chain = new Request.Builder()
                .url(url)
                .header("User-Agent", userAgent);

        if (credentials != null) {
            chain.header("Authorization", credentials);
        }

        if (body != null) {
            MediaType mediaType = MediaType.parse(JSON_MEDIA_TYPE);
            RequestBody requestBody = (body.isEmpty()) ?
                RequestBody.create(body, mediaType) : RequestBody.create(new JSONObject(body).toString(), mediaType);
            chain.method(httpMethod.label, requestBody);
        } else {
            switch (httpMethod) {
                case POST:
                case PUT:
                case PATCH:
                    chain.method(httpMethod.label, RequestBody.create(new byte[]{}, MediaType.parse(JSON_MEDIA_TYPE)));
                    break;
                default:
                    chain.method(httpMethod.label, null);
                    break;
            }
        }
        logger.trace("Request {} {} with body {}", httpMethod.label, url, body);
        return chain.build();
    }

    protected Response makeRequest(Request request) throws IOException {
        Response response;
        try {
            response = CLIENT.newCall(request).execute();
        } catch (IOException e) {
            logger.fatal("Error executing request", e);
            throw new IOException(String.format("Error executing request: %s", e.getMessage()), e);
        }

        if (shouldRetryOnHttpError(response)) {
            logger.debug("Retrying request {} {} because of HTTP error {}", request.method(), request.url(), response.code());
            response = retryRequest(request);
        } else {
            logger.trace("Not retrying request {} {} because of HTTP error {}", request.method(), request.url(), response.code());
        }

        if (!response.isSuccessful()) {
            logger.warn("Request {} {} failed with response code {} and message \"{}\"", request.method(), request.url(), response.code(), response.message());
            responseHandler(this, response);
        }

        return response;
    }

    private boolean shouldRetryOnHttpError(Response response) {
        final int HTTP_TOO_MANY_REQUESTS = 429;
        final int HTTP_SERVER_ERROR_MIN = 500;
        final int HTTP_SERVER_ERROR_MAX = 599;
        int responseCode = response.code();
        boolean isHttpError = responseCode >= HTTP_SERVER_ERROR_MIN && responseCode <= HTTP_SERVER_ERROR_MAX;
        boolean isTooManyRequests = responseCode == HTTP_TOO_MANY_REQUESTS;
        return isHttpError || isTooManyRequests;
    }

    /**
     * Executes the given HTTP request and retries it if it fails due to a runtime exception, IOException,
     * or IllegalStateException.
     *
     * @param request The HTTP request to execute.
     * @return The HTTP response.
     * @throws IOException If an I/O error occurs while executing the request.
     */
    private Response retryRequest(Request request) throws IOException {
        Response response;
        try {
            logger.debug("Retrying request {} {}", request.method(), request.url());
            response = Failsafe.with(new RetryPolicy<>()
                    .handle(RuntimeException.class, IOException.class, IllegalStateException.class)
                    .withBackoff(BACKOFF_INITIAL_DELAY, BACKOFF_MULTIPLIER, ChronoUnit.MILLIS)
                    .withMaxRetries(MAX_RETRIES)
                    .onRetry(e -> {
                        if (e.getLastFailure() != null) {
                            logger.warn("Retrying because of: {}", e.getLastFailure().getClass().getSimpleName());
                        } else {
                            logger.warn("Retrying");
                        }
                    }))
                .get(() -> CLIENT.newCall(request).execute());
        } catch (Exception e) {
            logger.fatal("Error retrying request", e);
            throw new IOException(String.format("Error retrying request: %s", e.getMessage()), e);
        }
        logger.debug("Request {} {} succeeded after retry", request.method(), request.url());
        return response;
    }

    /**
     * This method is used to deserialize a JSON object response from an API endpoint.
     *
     * @param jsonResponse JSON object response from API endpoint
     * @param clazz        The class to deserialize the JSON object into
     * @param <T>          The type of the object to deserialize
     * @return The deserialized object
     * @throws IOException If the JSON object cannot be deserialized
     */
    protected <T> T deserializeJSONObject(String jsonResponse, Class<T> clazz) throws IOException {
        Objects.requireNonNull(jsonResponse, "JSON response cannot be null");
        Objects.requireNonNull(clazz, "Class object cannot be null");

        Moshi moshi = MoshiSingleton.getInstance();
        JsonAdapter<T> jsonAdapter = moshi.adapter(clazz);
        try {
            return jsonAdapter.fromJson(jsonResponse);
        } catch (IOException e) {
            throw new IOException("Error deserializing JSON response to " + clazz.getSimpleName() + " class", e);
        } catch (JsonDataException e) {
            logger.warn("Could not deserialize JSON response:" + System.lineSeparator() + jsonResponse);
            throw e;
        }
    }

    protected <T> List<T> deserializeJSONObject(Response response, List<Class<? extends T>> clazz) throws IOException {
        if (response.body() != null) {
            return deserializeJSONObject(response.body().string(), clazz);
        } else {
            throw new IOException("Response body is null");
        }
    }

    protected <T> List<T> deserializeJSONObject(String jsonResponse, List<Class<? extends T>> clazz) throws IOException {
        Objects.requireNonNull(jsonResponse, "JSON response cannot be null");
        Objects.requireNonNull(clazz, "Class object cannot be null");

        Moshi moshi = MoshiSingleton.getInstance();
        JsonAdapter<List<T>> jsonAdapter = moshi.adapter(Types.newParameterizedType(List.class, clazz.get(0)));
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonResponse.getBytes());
             JsonReader reader = JsonReader.of(Okio.buffer(Okio.source(inputStream)))) {
            reader.beginObject();
            reader.nextName();
            List<T> list = jsonAdapter.fromJson(reader);
            reader.endObject();
            return list;
        } catch (IOException e) {
            throw new IOException("Error deserializing JSON response to " + clazz.get(0).getSimpleName() + " class", e);
        } catch (JsonDataException e) {
            logger.warn("Could not deserialize JSON response:" + System.lineSeparator() + jsonResponse);
            throw e;
        }
    }

    protected <T> T deserializeJSONObject(Response response, Class<T> clazz) throws IOException {
        if (response.body() != null) {
            return deserializeJSONObject(response.body().string(), clazz);
        } else {
            throw new IOException("Response body is null");
        }
    }

    /**
     * This method is used to deserialize a JSON array response from an API endpoint.
     *
     * @param jsonResponse JSON array response from API endpoint
     * @param clazz        The class to deserialize the JSON array into
     * @param <T>          The type of the object to deserialize
     * @return The deserialized list of objects
     * @throws IOException If the JSON array cannot be deserialized
     */
    protected <T> List<T> deserializeJSONArray(String jsonResponse, Class<T> clazz) throws IOException {
        Moshi moshi = MoshiSingleton.getInstance();

        Type listPlatform = Types.newParameterizedType(List.class, clazz);
        JsonAdapter<List<T>> jsonAdapter = moshi.adapter(listPlatform);
        try {
            return jsonAdapter.fromJson(jsonResponse);
        } catch (IOException e) {
            throw new IOException("Error deserializing JSON response to " + clazz.getSimpleName() + " class", e);
        } catch (JsonDataException e) {
            logger.warn("Could not deserialize JSON response:" + System.lineSeparator() + jsonResponse);
            throw e;
        }
    }

    protected <T> List<T> deserializeJSONArray(Response response, Class<T> clazz) throws IOException {
        if (response.body() != null) {
            return deserializeJSONArray(response.body().string(), clazz);
        } else {
            throw new IOException("Response body is null");
        }
    }

    protected void downloadFile(String url, String path, String fileName) {
        try (BufferedSink sink = Okio.buffer(Okio.sink(Paths.get(path, fileName).toFile()))) {
            sink.writeAll(Objects.requireNonNull(request(url, HttpMethod.GET).body()).source());
        } catch (IOException e) {
            logger.fatal(String.format("Error downloading file to %s with filename %s", path, fileName), e);
        }
    }

    protected void downloadFile(String url, String path) {
        downloadFile(url, path, Paths.get(url).getFileName().toString());
    }

    protected Path getDirectoryPath(String directoryPathString) throws IOException {
        if (directoryPathString == null || directoryPathString.isEmpty()) {
            // Use current directory if directoryPath is not specified
            directoryPathString = System.getProperty("user.dir");
        }

        Path directoryPath = Paths.get(directoryPathString);
        // Create directory if it doesn't exist
        Files.createDirectories(directoryPath);

        return directoryPath;
    }

    protected Path getFilePath(Path directoryPath, String fileName) {
        return directoryPath.resolve(fileName);
    }
}