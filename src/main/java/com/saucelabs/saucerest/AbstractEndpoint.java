package com.saucelabs.saucerest;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public abstract class AbstractEndpoint {
    protected final String baseURL = "https://api.us-west-1.saucelabs.com";
    protected final String username;
    protected final String accessKey;
    protected final String credentials;

    public AbstractEndpoint() {
        this.username = System.getenv("SAUCE_USERNAME");
        this.accessKey = System.getenv("SAUCE_ACCESS_KEY");
        this.credentials = Credentials.basic(username, accessKey);
    }

    public JSONObject getResponseObject(String url) throws IOException {
        Response response = getResponse(url);
        return new JSONObject(response.body().string());
    }

    public okio.BufferedSource getStream(String url) throws IOException {
        Response response = getResponse(url);
        return response.body().source();
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

    public void deleteResponse(String url) throws IOException {
        Request request = new Request.Builder()
            .header("Authorization", credentials)
            .url(url)
            .delete()
            .build();

        makeRequest(request);
    }

    private Response getResponse(String url) throws IOException {
        Request request = new Request.Builder()
            .header("Authorization", credentials)
            .url(url)
            .build();
        return makeRequest(request);
    }

    private Response makeRequest(Request request) throws IOException {
        Response response = new OkHttpClient().newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new RuntimeException("Unexpected code " + response);
        }
        return response;
    }
}
