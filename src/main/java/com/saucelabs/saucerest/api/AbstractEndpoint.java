package com.saucelabs.saucerest.api;

import static com.saucelabs.saucerest.api.ResponseHandler.responseHandler;

import com.saucelabs.saucerest.BuildUtils;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.ErrorExplainers;
import com.saucelabs.saucerest.HttpMethod;
import com.saucelabs.saucerest.MoshiSingleton;
import com.saucelabs.saucerest.SauceException;
import com.saucelabs.saucerest.model.AbstractModel;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

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
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;

import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEndpoint extends AbstractModel {
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEndpoint.class);
  private static final String JSON_MEDIA_TYPE = "application/json";
  private static final int MAX_RETRIES = 5;
  private static final int BACKOFF_INITIAL_DELAY = 30;
  private static final int BACKOFF_MULTIPLIER = 500;

  private OkHttpClient httpClient;
  protected final String userAgent = "SauceREST/" + BuildUtils.getCurrentVersion();
  protected final String baseURL;
  protected final String username;
  protected final String accessKey;
  protected final String credentials;
  protected final boolean needsAuthentication;

  protected AbstractEndpoint(DataCenter dataCenter) {
    this(dataCenter, true);
  }

  protected AbstractEndpoint(DataCenter dataCenter, boolean needsAuthentication) {
    this.username = System.getenv("SAUCE_USERNAME");
    this.accessKey = System.getenv("SAUCE_ACCESS_KEY");
    this.needsAuthentication = needsAuthentication;
    this.credentials = this.needsAuthentication ? initializeCredentials() : null;
    this.baseURL = dataCenter.apiServer;
  }

  protected AbstractEndpoint(String apiServer) {
    this(apiServer, true);
  }

  protected AbstractEndpoint(String apiServer, boolean needsAuthentication) {
    this.username = System.getenv("SAUCE_USERNAME");
    this.accessKey = System.getenv("SAUCE_ACCESS_KEY");
    this.needsAuthentication = needsAuthentication;
    this.credentials = this.needsAuthentication ? initializeCredentials() : null;
    this.baseURL = apiServer;
  }

  protected AbstractEndpoint(String username, String accessKey, DataCenter dataCenter) {
    this.username = username;
    this.accessKey = accessKey;
    this.needsAuthentication = true;
    this.credentials = initializeCredentials();
    this.baseURL = dataCenter.apiServer;
  }

  protected AbstractEndpoint(String username, String accessKey, String apiServer) {
    this.username = username;
    this.accessKey = accessKey;
    this.needsAuthentication = true;
    this.credentials = initializeCredentials();
    this.baseURL = apiServer;
  }

  private String initializeCredentials() {
    if ((username == null || accessKey == null)) {
      LOGGER.warn(
          "Credentials are null. Please set the SAUCE_USERNAME and SAUCE_ACCESS_KEY environment variables.");
      throw new SauceException.MissingCredentials(ErrorExplainers.missingCreds());
    }
    return Credentials.basic(username, accessKey);
  }

  protected String getBaseEndpoint() {
    return baseURL;
  }

  /**
   * Build a URL with query parameters.
   *
   * @param url Sauce Labs API endpoint
   * @param params Query parameters for GET request. If null, no query parameters will be added. Can
   *     be a string or an array of strings.
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

  public Response request(String url, HttpMethod httpMethod, Map<String, Object> body)
      throws IOException {
    return request(url, httpMethod, new JSONObject(body).toString());
  }

  public Response request(String url, HttpMethod httpMethod, String body) throws IOException {
    Request request = createRequest(url, httpMethod, body);
    return makeRequest(request);
  }

  public Response requestWithQueryParameters(
      String url, HttpMethod httpMethod, Map<String, Object> params) throws IOException {
    return request(buildUrl(url, params), httpMethod);
  }

  private OkHttpClient getHttpClient() {
    synchronized (this) {
      if (httpClient == null) {
        createHttpClient(HttpClientConfig.defaultConfig());
      }
    }
    return httpClient;
  }

  @SuppressWarnings("KotlinInternalInJava")
  public void createHttpClient(HttpClientConfig config) {
    OkHttpClient.Builder builder =
        new OkHttpClient.Builder()
            .connectTimeout(config.getConnectTimeout())
            .readTimeout(config.getReadTimeout())
            .writeTimeout(config.getWriteTimeout());

    if (config.getProxy() != null) {
      builder.proxy(config.getProxy());
    }
    if (config.getAuthenticator() != null) {
      builder.authenticator(config.getAuthenticator());
    }
    if (config.getInterceptor() != null) {
      builder.addInterceptor(config.getInterceptor());
    }

    httpClient = builder.build();
  }

  private Request createRequest(String url, HttpMethod httpMethod, String body) {
    Request.Builder chain = new Request.Builder().url(url).header("User-Agent", userAgent);

    if (credentials != null) {
      chain.header("Authorization", credentials);
    }

    if (body != null) {
      MediaType mediaType = MediaType.parse(JSON_MEDIA_TYPE);
      RequestBody requestBody =
          (body.isEmpty())
              ? RequestBody.create(body, mediaType)
              : RequestBody.create(new JSONObject(body).toString(), mediaType);
      chain.method(httpMethod.label, requestBody);
    } else {
      switch (httpMethod) {
        case POST:
        case PUT:
        case PATCH:
          chain.method(
              httpMethod.label,
              RequestBody.create(new byte[] {}, MediaType.parse(JSON_MEDIA_TYPE)));
          break;
        default:
          chain.method(httpMethod.label, null);
          break;
      }
    }
    LOGGER.trace("Request {} {} with body {}", httpMethod.label, url, body);
    return chain.build();
  }

  protected Response makeRequest(Request request) throws IOException {
    Response response;
    try {
      response = getHttpClient().newCall(request).execute();
    } catch (IOException e) {
      LOGGER.error("Error executing request", e);
      throw new IOException(String.format("Error executing request: %s", e.getMessage()), e);
    }

    if (shouldRetryOnHttpError(response)) {
      LOGGER.atDebug()
          .addArgument(request::method)
          .addArgument(request::url)
          .addArgument(response::code)
          .log("Retrying request {} {} because of HTTP error {}");
      response = retryRequest(request);
    } else {
        LOGGER.atTrace()
            .addArgument(request::method)
            .addArgument(request::url)
            .addArgument(response::code)
            .log("Not retrying request {} {} because of HTTP error {}");
    }

    if (!response.isSuccessful()) {
        LOGGER.atWarn()
            .addArgument(request::method)
            .addArgument(request::url)
            .addArgument(response::code)
            .addArgument(response::message)
            .log("Request {} {} failed with response code {} and message \"{}\"");
      responseHandler(this, response);
    }

    return response;
  }

  private boolean shouldRetryOnHttpError(Response response) {
    final int HTTP_TOO_MANY_REQUESTS = 429;
    final int HTTP_SERVER_ERROR_MIN = 500;
    final int HTTP_SERVER_ERROR_MAX = 599;
    int responseCode = response.code();
    boolean isHttpError =
        responseCode >= HTTP_SERVER_ERROR_MIN && responseCode <= HTTP_SERVER_ERROR_MAX;
    boolean isTooManyRequests = responseCode == HTTP_TOO_MANY_REQUESTS;
    return isHttpError || isTooManyRequests;
  }

  /**
   * Executes the given HTTP request and retries it if it fails due to a runtime exception,
   * IOException, or IllegalStateException.
   *
   * @param request The HTTP request to execute.
   * @return The HTTP response.
   * @throws IOException If an I/O error occurs while executing the request.
   */
  private Response retryRequest(Request request) throws IOException {
    Response response;
    try {
      LOGGER.atDebug()
          .addArgument(request::method)
          .addArgument(request::url)
          .log("Retrying request {} {}");
      response =
          Failsafe.with(
                  new RetryPolicy<>()
                      .handle(
                          RuntimeException.class, IOException.class, IllegalStateException.class)
                      .withBackoff(BACKOFF_INITIAL_DELAY, BACKOFF_MULTIPLIER, ChronoUnit.MILLIS)
                      .withMaxRetries(MAX_RETRIES)
                      .onRetry(
                          e -> {
                            if (e.getLastFailure() != null) {
                              LOGGER.atWarn()
                                  .addArgument(() -> e.getLastFailure().getClass().getSimpleName())
                                  .log("Retrying because of: {}");
                            } else {
                              LOGGER.warn("Retrying");
                            }
                          }))
              .get(() -> getHttpClient().newCall(request).execute());
    } catch (Exception e) {
      LOGGER.error("Error retrying request", e);
      throw new IOException(String.format("Error retrying request: %s", e.getMessage()), e);
    }
    LOGGER.atDebug()
        .addArgument(request::method)
        .addArgument(request::url)
        .log("Request {} {} succeeded after retry");
    return response;
  }

  /**
   * This method is used to deserialize a JSON object response from an API endpoint.
   *
   * @param jsonResponse JSON object response from API endpoint
   * @param clazz The class to deserialize the JSON object into
   * @param <T> The type of the object to deserialize
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
      throw new IOException(
          "Error deserializing JSON response to " + clazz.getSimpleName() + " class", e);
    } catch (JsonDataException e) {
      LOGGER.atWarn()
          .addArgument(System::lineSeparator)
          .addArgument(jsonResponse)
          .log("Could not deserialize JSON response:{}{}");
      throw e;
    }
  }

  protected <T> List<T> deserializeJSONObject(Response response, List<Class<? extends T>> clazz)
      throws IOException {
    if (response.body() != null) {
      return deserializeJSONObject(response.body().string(), clazz);
    } else {
      throw new IOException("Response body is null");
    }
  }

  protected <T> List<T> deserializeJSONObject(String jsonResponse, List<Class<? extends T>> clazz)
      throws IOException {
    Objects.requireNonNull(jsonResponse, "JSON response cannot be null");
    Objects.requireNonNull(clazz, "Class object cannot be null");

    Moshi moshi = MoshiSingleton.getInstance();
    JsonAdapter<List<T>> jsonAdapter =
        moshi.adapter(Types.newParameterizedType(List.class, clazz.get(0)));
    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonResponse.getBytes());
        JsonReader reader = JsonReader.of(Okio.buffer(Okio.source(inputStream)))) {
      reader.beginObject();
      reader.nextName();
      List<T> list = jsonAdapter.fromJson(reader);
      reader.endObject();
      return list;
    } catch (IOException e) {
      throw new IOException(
          "Error deserializing JSON response to " + clazz.get(0).getSimpleName() + " class", e);
    } catch (JsonDataException e) {
        LOGGER.atWarn()
            .addArgument(System::lineSeparator)
            .addArgument(jsonResponse)
            .log("Could not deserialize JSON response:{}{}");
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
   * @param clazz The class to deserialize the JSON array into
   * @param <T> The type of the object to deserialize
   * @return The deserialized list of objects
   * @throws IOException If the JSON array cannot be deserialized
   */
  protected <T> List<T> deserializeJSONArray(String jsonResponse, Class<T> clazz)
      throws IOException {
    Moshi moshi = MoshiSingleton.getInstance();

    Type listPlatform = Types.newParameterizedType(List.class, clazz);
    JsonAdapter<List<T>> jsonAdapter = moshi.adapter(listPlatform);
    try {
      return jsonAdapter.fromJson(jsonResponse);
    } catch (IOException e) {
      throw new IOException(
          "Error deserializing JSON response to " + clazz.getSimpleName() + " class", e);
    } catch (JsonDataException e) {
        LOGGER.atWarn()
            .addArgument(System::lineSeparator)
            .addArgument(jsonResponse)
            .log("Could not deserialize JSON response:{}{}");
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
        LOGGER.error("Error downloading file to {} with filename {}", path, fileName, e);
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
