package com.saucelabs.saucerest.api;

import com.saucelabs.saucerest.BuildUtils;
import com.saucelabs.saucerest.DataCenter;
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
    public String getResponseObject(String url, Map<String, Object> params) throws IOException {
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

        Request.Builder chain = new Request.Builder();

        if (credentials != null) {
            chain = chain.header("Authorization", credentials);
        }

        Request request = chain
            .header("User-Agent", userAgent)
            .url(urlBuilder.build().toString())
            .build();

        try (Response response = makeRequest(request)) {
            return response.body().string();
        }
    }

    public okio.BufferedSource getStream(String url) throws IOException {
        Response response = getResponse(url);
        return response.body().source();
    }

    public String postResponse(String url, Map<String, Object> payload) throws IOException {
        return postResponse(url, payload, MediaType.parse("application/json"));
    }

    public String postResponse(String url, Map<String, Object> payload, MediaType mediaType) throws IOException {
        String json = new JSONObject(payload).toString();

        Request.Builder chain = new Request.Builder();

        if (credentials != null) {
            chain = chain.header("Authorization", credentials);
        }

        Request request = chain
            .header("User-Agent", userAgent)
            .url(url)
            .post(RequestBody.create(json, mediaType))
            .build();

        try (Response response = makeRequest(request)) {
            return response.body().string();
        }
    }

    public String postResponse(String url) throws IOException {
        Request.Builder chain = new Request.Builder();

        if (credentials != null) {
            chain = chain.header("Authorization", credentials);
        }

        Request request = chain
            .header("User-Agent", userAgent)
            .url(url)
            .post(RequestBody.create("", MediaType.parse("application/json")))
            .build();

        try (Response response = makeRequest(request)) {
            return response.body().string();
        }
    }

    public String putResponse(String url, Map<String, Object> payload) throws IOException {
        String json = new JSONObject(payload).toString();

        Request.Builder chain = new Request.Builder();

        if (credentials != null) {
            chain = chain.header("Authorization", credentials);
        }

        Request request = chain
            .url(url)
            .put(RequestBody.create(json, MediaType.parse("application/json")))
            .build();

        try (Response response = makeRequest(request)) {
            return response.body().string();
        }
    }

    public String putResponse(String url, String payload) throws IOException {
        String json = new JSONObject(payload).toString();

        Request.Builder chain = new Request.Builder();

        if (credentials != null) {
            chain = chain.header("Authorization", credentials);
        }

        Request request = chain
            .url(url)
            .put(RequestBody.create(json, MediaType.parse("application/json")))
            .build();

        try (Response response = makeRequest(request)) {
            return response.body().string();
        }
    }

    public String patchResponse(String url, Map<String, Object> payload) throws IOException {
        String json = new JSONObject(payload).toString();

        Request.Builder chain = new Request.Builder();

        if (credentials != null) {
            chain = chain.header("Authorization", credentials);
        }

        Request request = chain
            .url(url)
            .patch(RequestBody.create(json, MediaType.parse("application/json")))
            .build();

        try (Response response = makeRequest(request)) {
            return response.body().string();
        }
    }

    public String patchResponse(String url, String payload) throws IOException {
        String json = new JSONObject(payload).toString();

        Request.Builder chain = new Request.Builder();

        if (credentials != null) {
            chain = chain.header("Authorization", credentials);
        }

        Request request = chain
            .url(url)
            .patch(RequestBody.create(json, MediaType.parse("application/json")))
            .build();

        try (Response response = makeRequest(request)) {
            return response.body().string();
        }
    }

    public String deleteResponse(String url) throws IOException {
        Request.Builder chain = new Request.Builder();

        if (credentials != null) {
            chain = chain.header("Authorization", credentials);
        }

        Request request = chain
            .url(url)
            .delete()
            .build();

        try (Response response = makeRequest(request)) {
            return response.body().string();
        }
    }

    private Response getResponse(String url) throws IOException {
        Request.Builder chain = new Request.Builder();

        if (credentials != null) {
            chain = chain.header("Authorization", credentials);
        }

        Request request = chain
            .url(url)
            .build();

        return makeRequest(request);
    }

    protected Response makeRequest(Request request) throws IOException {
        OkHttpClient client;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(300, TimeUnit.SECONDS);
        builder.readTimeout(300, TimeUnit.SECONDS);
        builder.writeTimeout(300, TimeUnit.SECONDS);
        client = builder.build();
        Response response = client.newCall(request).execute();

        Integer responseCode = response.code();
        Integer responseCodeLength = String.valueOf(responseCode).length();

        if (responseCodeLength == 3 && (responseCode == 429 || String.valueOf(responseCode).startsWith("5"))) {
            Response finalResponse = response;
            response = Failsafe.with(
                    new RetryPolicy<>()
                        .handle(RuntimeException.class)
                        .withBackoff(30, 500, ChronoUnit.MILLIS)
                        .withMaxRetries(2)
                        .onRetry(e -> logger.log(Level.WARNING, () -> "Retrying because of " + finalResponse.code())))
                .get(() -> client.newCall(request).execute());
        }

        if (!response.isSuccessful()) {
            responseHandler(this, response);
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
        Moshi moshi = new Moshi.Builder().build();
        // failOnUnknown() will make sure that API changes in SL are caught ASAP, so we can update SauceREST
        JsonAdapter<T> jsonAdapter = moshi.adapter(clazz).failOnUnknown();
        return jsonAdapter.fromJson(jsonResponse);
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
        Moshi moshi = new Moshi.Builder().build();

        Type listPlatform = Types.newParameterizedType(List.class, clazz);
        JsonAdapter<List<T>> adapter = moshi.adapter(listPlatform);
        return adapter.fromJson(jsonResponse);
    }
}