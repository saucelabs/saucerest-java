package com.saucelabs.saucerest;

import com.google.common.collect.ImmutableMap;
import okhttp3.*;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class Storage extends AbstractEndpoint{
    public Storage(DataCenter dataCenter) {
        super(dataCenter);
    }

    public Storage(String apiServer) {
        super(apiServer);
    }

    public JSONObject getFiles() throws IOException {
        String url = getBaseEndpoint() + "/files";
        return getResponseObject(url);
    }

    /**
     * Use parameter names from here
     * <a href="https://docs.saucelabs.com/dev/api/storage/#get-app-storage-files">https://docs.saucelabs.com/dev/api/storage/#get-app-storage-files</a>
     * @param params query parameters for this request
     * @return
     * @throws IOException
     */
    public JSONObject getFiles(ImmutableMap<String, Object> params) throws IOException {
        String url = getBaseEndpoint() + "/files";
        return getResponseObject(url, params);
    }

    public JSONObject getGroups() throws IOException {
        String url = getBaseEndpoint() + "/groups";
        return getResponseObject(url);
    }

    /**
     * Use parameter names from here
     * <a href="https://docs.saucelabs.com/dev/api/storage/#get-app-storage-groups">https://docs.saucelabs.com/dev/api/storage/#get-app-storage-groups</a>
     * @param params
     * @return
     * @throws IOException
     */
    public JSONObject getGroups(ImmutableMap<String, Object> params) throws IOException {
        String url = getBaseEndpoint() + "/groups";
        return getResponseObject(url, params);
    }

    public JSONObject getGroupSettings(int groupId) throws IOException {
        String url = getBaseEndpoint() + "/groups/" + groupId + "/settings";
        return getResponseObject(url);
    }

    public JSONObject uploadFile(File file) throws IOException {
        return uploadFile(file, "", "");
    }

    public JSONObject uploadFile(File file, String fileName) throws IOException {
        return uploadFile(file, fileName, "");
    }

    public JSONObject uploadFile(File file, String fileName, String description) throws IOException {
        String url = getBaseEndpoint() + "/upload";

        return postMultipartResponse(url, file, fileName, description);
    }

    private String getBaseEndpoint() {
        return baseURL + "v1/storage";
    }

    /**
     * Need a upload-specific post method for the additional parameters.
     * @param url Sauce Labs API endpoint
     * @param file mobile app file
     * @return
     * @throws IOException
     */
    private JSONObject postMultipartResponse(String url, File file, String fileName, String description) throws IOException {
        RequestBody requestBody = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("name", fileName)
            .addFormDataPart("description", description)
            .addFormDataPart("payload", file.getName(),
                RequestBody.create(file, MediaType.parse("application/octet-stream")))
            .build();

        Request request = new Request.Builder()
            .header("Authorization", credentials)
            .url(url)
            .post(requestBody)
            .build();

        try (Response response = makeRequest(request)) {
            if (!response.isSuccessful()) {
                System.out.println(response.body().toString());
                throw new IOException("Unexpected code" + response);
            }
            return new JSONObject(response.body().string());
        }
    }
}
