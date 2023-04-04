package com.saucelabs.saucerest.api;

import com.saucelabs.saucerest.BuildUtils;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.HttpMethod;
import com.saucelabs.saucerest.MoshiSingleton;
import com.saucelabs.saucerest.model.AbstractModel;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.saucelabs.saucerest.api.ResponseHandler.responseHandler;

public abstract class AbstractEndpoint extends AbstractModel {
    private static final Logger logger = Logger.getLogger(AbstractEndpoint.class.getName());
    private static final int MAX_RETRIES = 2;
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

    public AbstractEndpoint(DataCenter dataCenter) {
        this.username = System.getenv("SAUCE_USERNAME");
        this.accessKey = System.getenv("SAUCE_ACCESS_KEY");

        if (username == null || accessKey == null) {
            this.credentials = null;
        } else {
            this.credentials = Credentials.basic(username, accessKey);
        }

        this.baseURL = dataCenter.apiServer;
    }

    public AbstractEndpoint(String apiServer) {
        this.username = System.getenv("SAUCE_USERNAME");
        this.accessKey = System.getenv("SAUCE_ACCESS_KEY");

        if (username == null || accessKey == null) {
            this.credentials = null;
        } else {
            this.credentials = Credentials.basic(username, accessKey);
        }

        this.baseURL = apiServer;
    }

    public AbstractEndpoint(String username, String accessKey, DataCenter dataCenter) {
        this.username = username;
        this.accessKey = accessKey;

        if (username == null || accessKey == null) {
            this.credentials = null;
        } else {
            this.credentials = Credentials.basic(username, accessKey);
        }

        this.baseURL = dataCenter.apiServer;
    }

    public AbstractEndpoint(String username, String accessKey, String apiServer) {
        this.username = username;
        this.accessKey = accessKey;

        if (username == null || accessKey == null) {
            this.credentials = null;
        } else {
            this.credentials = Credentials.basic(username, accessKey);
        }

        this.baseURL = apiServer;
    }

    protected String getBaseEndpoint() {
        return baseURL;
    }

    public String getResponseObject(String url) throws IOException {
        Response response = getResponse(url);

        return response.body().string();
    }

    /**
     * Make a GET request with query parameters.
     *
     * @param url    Sauce Labs API endpoint
     * @param params query parameters for GET request
     * @return
     * @throws IOException
     */
    public Response getResponseObject(String url, Map<String, Object> params) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();

        for (Map.Entry<String, Object> param : params.entrySet()) {
            // Check if parameter is a String array and add every element one by one to the url
            if (param.getValue().getClass() == (String[].class)) {
                for (String element : (String[]) param.getValue()) {
                    urlBuilder.addQueryParameter(param.getKey(), element);
                }
            } else {
                urlBuilder.addQueryParameter(param.getKey(), param.getValue().toString());
            }
        }

        return request(urlBuilder.build().toString(), HttpMethod.GET);
    }

    public okio.BufferedSource getStream(String url) throws IOException {
        Response response = getResponse(url);
        return response.body().source();
    }

    private Response getResponse(String url) throws IOException {
        Request.Builder chain = new Request.Builder();

        if (credentials != null) {
            chain = chain.header("Authorization", credentials);
        }

        Request request = chain
            .url(url)
            .get()
            .build();

        return makeRequest(request);
    }

    public Response request(String url, HttpMethod httpMethod) throws IOException {
        return request(url, httpMethod, (String) null);
    }

    public Response request(String url, HttpMethod httpMethod, Map<String, Object> body) throws IOException {
        return request(url, httpMethod, new JSONObject(body).toString());
    }

    public Response request(String url, HttpMethod httpMethod, String body) throws IOException {
        Request.Builder chain = new Request.Builder();

        if (credentials != null) {
            chain.header("Authorization", credentials);
        }

        if (body != null) {
            if (body.equals("")) {
                chain.method(httpMethod.label, RequestBody.create(body, MediaType.parse("application/json")));
            } else {
                String json = new JSONObject(body).toString();
                chain.method(httpMethod.label, RequestBody.create(json, MediaType.parse("application/json")));
            }
        } else {
            if (httpMethod.equals(HttpMethod.GET)) {
                chain.method(httpMethod.label, null);
            } else if (httpMethod.equals(HttpMethod.POST)) {
                chain.method(httpMethod.label, RequestBody.create(null, "application/json"));
            } else {
                chain.method(httpMethod.label, null);
            }
        }

        Request request = chain
            .url(url)
            .build();

        return makeRequest(request);
    }

    protected Response makeRequest(Request request) throws IOException {
        Response response;
        try {
            response = CLIENT.newCall(request).execute();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error executing request", e);
            throw e;
        }

        if (!response.isSuccessful()) {
            logger.log(Level.INFO, "Request {0} {1} failed with response code {2} and message {3}",
                new Object[]{request.method(), request.url(), response.code(), response.message()});
            responseHandler(this, response);
        }

        if (shouldRetryOnHttpError(response)) {
            response = retryRequest(request);
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
        Response response = null;
        try {
            response = Failsafe.with(new RetryPolicy<>()
                    .handle(RuntimeException.class, IOException.class, IllegalStateException.class)
                    .withBackoff(BACKOFF_INITIAL_DELAY, BACKOFF_MULTIPLIER, ChronoUnit.MILLIS)
                    .withMaxRetries(MAX_RETRIES)
                    .onRetry(e -> {
                        if (e.getLastFailure() != null) {
                            logger.log(Level.WARNING, "Retrying because of " + e.getLastFailure().getClass().getSimpleName());
                        } else {
                            logger.log(Level.WARNING, "Retrying");
                        }
                    }))
                .get(() -> CLIENT.newCall(request).execute());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrying request", e);
            throw new IOException(e);
        }
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
        Moshi moshi = MoshiSingleton.getInstance();
        // failOnUnknown() will make sure that API changes in SL are caught ASAP, so we can update SauceREST
        JsonAdapter<T> jsonAdapter = moshi.adapter(clazz).failOnUnknown();
        try {
            return jsonAdapter.fromJson(jsonResponse);
        } catch (IOException e) {
            throw new IOException("Error deserializing JSON response to " + clazz.getSimpleName() + " class", e);
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
        }
    }
}