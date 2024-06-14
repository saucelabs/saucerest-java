package com.saucelabs.saucerest.api;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
import static com.saucelabs.saucerest.api.ResponseHandler.responseHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.saucelabs.saucerest.BuildUtils;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.ErrorExplainers;
import com.saucelabs.saucerest.HttpMethod;
import com.saucelabs.saucerest.SauceException;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractEndpoint {
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEndpoint.class);
  private static final String JSON_MEDIA_TYPE = "application/json";
  private static final int MAX_RETRIES = 5;
  private static final int BACKOFF_INITIAL_DELAY = 30;
  private static final int BACKOFF_MULTIPLIER = 500;

  protected static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();

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
    return request(url, httpMethod, null);
  }

  public Response request(String url, HttpMethod httpMethod, Object body) throws IOException {
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

  private Request createRequest(String url, HttpMethod httpMethod, Object body) {
    Request.Builder chain = new Request.Builder().url(url).header("User-Agent", userAgent);

    if (credentials != null) {
      chain.header("Authorization", credentials);
    }

    if (body != null) {
      MediaType mediaType = MediaType.parse(JSON_MEDIA_TYPE);
      RequestBody requestBody = RequestBody.create(body instanceof String ? (String) body : GSON.toJson(body), mediaType);
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
      LOGGER.debug(
          "Retrying request {} {} because of HTTP error {}",
          request.method(),
          request.url(),
          response.code());
      response = retryRequest(request);
    } else {
      LOGGER.trace(
          "Not retrying request {} {} because of HTTP error {}",
          request.method(),
          request.url(),
          response.code());
    }

    if (!response.isSuccessful()) {
      LOGGER.warn(
          "Request {} {} failed with response code {} and message \"{}\"",
          request.method(),
          request.url(),
          response.code(),
          response.message());
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
      LOGGER.debug("Retrying request {} {}", request.method(), request.url());
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
                              LOGGER.warn(
                                  "Retrying because of: {}",
                                  e.getLastFailure().getClass().getSimpleName());
                            } else {
                              LOGGER.warn("Retrying");
                            }
                          }))
              .get(() -> getHttpClient().newCall(request).execute());
    } catch (Exception e) {
      LOGGER.error("Error retrying request", e);
      throw new IOException(String.format("Error retrying request: %s", e.getMessage()), e);
    }
    LOGGER.debug("Request {} {} succeeded after retry", request.method(), request.url());
    return response;
  }

  protected <T> List<T> deserializeListFromJSONObject(Response response, Class<T> elementClass) throws IOException {
    Type type = TypeToken.getParameterized(Map.class, Object.class,
            TypeToken.getParameterized(List.class, elementClass).getType()).getType();
    Map<Object, List<T>> fullJson = deserializeJSON(response, type);
    return fullJson.values().iterator().next();
  }

  /**
   * This method is used to deserialize a JSON object response from an API endpoint.
   *
   * @param response HTTP response containing JSON object from API endpoint
   * @param clazz The class to deserialize the JSON object into
   * @param <T> The type of the object to deserialize
   * @return The deserialized object
   * @throws IOException If the JSON object cannot be deserialized
   */
  protected <T> T deserializeJSONObject(Response response, Class<T> clazz) throws IOException {
    return deserializeJSON(response, clazz);
  }

  /**
   * This method is used to deserialize a JSON array response from an API endpoint.
   *
   * @param response HTTP response containing JSON array from API endpoint
   * @param elementClass The class to deserialize the JSON array elements into
   * @param <T> The type of the object to deserialize
   * @return The deserialized list of objects
   */
  protected <T> List<T> deserializeJSONArray(Response response, Class<T> elementClass) throws IOException {
      return deserializeJSON(response, TypeToken.getParameterized(List.class, elementClass).getType());
  }

  protected <T> T deserializeJSON(Response response, Type typeOfT) throws IOException {
    if (response.body() == null) {
      throw new IOException("Response body is null");
    }
    String jsonResponse = response.body().string();
    try {
      return GSON.fromJson(jsonResponse, typeOfT);
    } catch (JsonSyntaxException e) {
      LOGGER.warn("Could not deserialize JSON response: {}{}", System.lineSeparator(), jsonResponse);
      throw e;
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
