package com.saucelabs.saucerest.api;

import com.saucelabs.saucerest.BuildUtils;
import com.saucelabs.saucerest.DataCenter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

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

    public JSONObject getResponseObject(String url) throws IOException {
        Response response = getResponse(url);
        return new JSONObject(response.body().string());
    }

    /**
     * Make a GET request with query parameters.
     *
     * @param url    Sauce Labs API endpoint
     * @param params query parameters for GET request
     * @return
     * @throws IOException
     */
    public JSONObject getResponseObject(String url, Map<String, Object> params) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();

        for (Map.Entry<String, Object> param : params.entrySet()) {
            urlBuilder.addQueryParameter(param.getKey(), param.getValue().toString());
        }

        Request request = new Request.Builder()
            .header("Authorization", credentials)
            .header("User-Agent", userAgent)
            .url(urlBuilder.build().toString())
            .build();

        Response response = makeRequest(request);
        return new JSONObject(response.body().string());
    }

    public okio.BufferedSource getStream(String url) throws IOException {
        Response response = getResponse(url);
        return response.body().source();
    }

    public JSONObject postResponse(String url, Map<String, Object> payload) throws IOException {
        return postResponse(url, payload, MediaType.parse("application/json"));
    }

    public JSONObject postResponse(String url, Map<String, Object> payload, MediaType mediaType) throws IOException {
        String json = new JSONObject(payload).toString();

        Request request = new Request.Builder()
            .header("Authorization", credentials)
            .header("User-Agent", userAgent)
            .url(url)
            .post(RequestBody.create(json, mediaType))
            .build();

        Response response = makeRequest(request);
        return new JSONObject(response.body().string());
    }

    public JSONObject putResponse(String url, Map<String, Object> payload) throws IOException {
        String json = new JSONObject(payload).toString();

        Request request = new Request.Builder()
            .header("Authorization", credentials)
            .url(url)
            .put(RequestBody.create(json, MediaType.parse("application/json")))
            .build();

        Response response = makeRequest(request);
        return new JSONObject(response.body().string());
    }

    public JSONObject putResponse(String url, String payload) throws IOException {
        String json = new JSONObject(payload).toString();

        Request request = new Request.Builder()
            .header("Authorization", credentials)
            .url(url)
            .put(RequestBody.create(json, MediaType.parse("application/json")))
            .build();

        Response response = makeRequest(request);
        return new JSONObject(response.body().string());
    }

    public JSONObject deleteResponse(String url) throws IOException {
        Request request = new Request.Builder()
            .header("Authorization", credentials)
            .url(url)
            .delete()
            .build();

        Response response = makeRequest(request);
        //return new JSONObject(response.body().string());
        return getJSONObject(response.body().string());
    }

    protected JSONObject getJSONObject(String responseBody) {
        try {
            new JSONObject(responseBody);
        } catch (JSONException exception) {
            if (exception.getMessage().startsWith("A JSONObject text must begin with '{'")) {
                return new JSONObject().put("response", new JSONArray(responseBody));
            }
        }
        return new JSONObject(responseBody);
    }

    private Response getResponse(String url) throws IOException {
        Request request = new Request.Builder()
            .header("Authorization", credentials)
            .url(url)
            .build();
        return makeRequest(request);
    }

    Response makeRequest(Request request) throws IOException {
        Response response = new OkHttpClient().newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new RuntimeException("Unexpected code " + response);
        }
        return response;
    }

    /**
     * Transforms a JSON response into the appropriate model.
     *
     * @param url
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    protected <T> T getResponseClass(String url, Class<T> clazz) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<T> jsonAdapter = moshi.adapter(clazz);
        return jsonAdapter.fromJson(getResponseObject(url).toString());
    }

    /**
     * Transforms a JSON response into the appropriate model.
     *
     * @param url
     * @param params
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    protected <T> T getResponseClass(String url, Map<String, Object> params, Class<T> clazz) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<T> jsonAdapter = moshi.adapter(clazz);
        return jsonAdapter.fromJson(getResponseObject(url, params).toString());
    }
}
