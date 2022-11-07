package com.saucelabs.saucerest.api;

import com.saucelabs.saucerest.BuildUtils;
import com.saucelabs.saucerest.DataCenter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class AbstractEndpoint {
    protected final String userAgent = "SauceREST/" + BuildUtils.getCurrentVersion();
    protected final String baseURL;
    protected final String username;
    protected final String accessKey;
    protected final String credentials;

    public AbstractEndpoint(DataCenter dataCenter) {
        this.username = System.getenv("SAUCE_USERNAME");
        this.accessKey = System.getenv("SAUCE_ACCESS_KEY");
        this.credentials = Credentials.basic(username, accessKey);
        this.baseURL = dataCenter.apiServer;
    }

    public AbstractEndpoint(String apiServer) {
        this.username = System.getenv("SAUCE_USERNAME");
        this.accessKey = System.getenv("SAUCE_ACCESS_KEY");
        this.credentials = Credentials.basic(username, accessKey);
        this.baseURL = apiServer;
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
            urlBuilder.addQueryParameter(param.getKey(), param.getValue().toString());
        }

        Request request = new Request.Builder()
            .header("Authorization", credentials)
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

        Request request = new Request.Builder()
            .header("Authorization", credentials)
            .header("User-Agent", userAgent)
            .url(url)
            .post(RequestBody.create(json, mediaType))
            .build();

        try (Response response = makeRequest(request)) {
            return response.body().string();
        }
    }

    public String putResponse(String url, Map<String, Object> payload) throws IOException {
        String json = new JSONObject(payload).toString();

        Request request = new Request.Builder()
            .header("Authorization", credentials)
            .url(url)
            .put(RequestBody.create(json, MediaType.parse("application/json")))
            .build();

        try (Response response = makeRequest(request)) {
            return response.body().string();
        }
    }

    public String putResponse(String url, String payload) throws IOException {
        String json = new JSONObject(payload).toString();

        Request request = new Request.Builder()
            .header("Authorization", credentials)
            .url(url)
            .put(RequestBody.create(json, MediaType.parse("application/json")))
            .build();

        try (Response response = makeRequest(request)) {
            return response.body().string();
        }
    }

    public String deleteResponse(String url) throws IOException {
        Request request = new Request.Builder()
            .header("Authorization", credentials)
            .url(url)
            .delete()
            .build();

        try (Response response = makeRequest(request)) {
            return response.body().string();
        }
    }

    private Response getResponse(String url) throws IOException {
        Request request = new Request.Builder()
            .header("Authorization", credentials)
            .url(url)
            .build();

        return makeRequest(request);
    }

    Response makeRequest(Request request) throws IOException {
        OkHttpClient client;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(300, TimeUnit.SECONDS);
        builder.readTimeout(300, TimeUnit.SECONDS);
        builder.writeTimeout(300, TimeUnit.SECONDS);
        client = builder.build();
        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new RuntimeException("Unexpected code " + response);
        }
        return response;
    }

    protected <T> T getResponseClass(String jsonResponse, Class<T> clazz) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        // failOnUnknown() will make sure that API changes in SL are caught ASAP so we can update SauceREST
        JsonAdapter<T> jsonAdapter = moshi.adapter(clazz).failOnUnknown();
        return jsonAdapter.fromJson(jsonResponse);
    }

    /**
     * Transform a model class into JSON.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    protected <T> String toJson(Class<T> clazz) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<T> jsonAdapter = moshi.adapter(clazz);
        return jsonAdapter.toJson((T) clazz);
    }
}
