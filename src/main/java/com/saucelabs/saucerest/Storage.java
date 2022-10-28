package com.saucelabs.saucerest;

import com.google.common.collect.ImmutableMap;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

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

    /**
     * Use parameter names from here
     * <a href="https://docs.saucelabs.com/dev/api/storage/#get-app-storage-group-settings">https://docs.saucelabs.com/dev/api/storage/#get-app-storage-group-settings</a>
     * @param groupId
     * @param jsonBody
     * @return
     * @throws IOException
     */
    public JSONObject updateAppStorageGroupSettings(int groupId, String jsonBody) throws IOException {
        String url = getBaseEndpoint() + "/groups/" + groupId + "/settings";
        return putResponse(url, jsonBody);
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

    /**
     * Download file from Sauce Labs App Storage.
     * @param fileId of the file to download
     * @param path where to save the file including fileName and extension
     * @throws IOException
     */
    public void downloadFile(String fileId, Path path) throws IOException {
        String url = getBaseEndpoint() + "/download/" + fileId;

        try (Response response = getResponse(url)) {
            FileUtils.writeByteArrayToFile(new File(path.toFile().toURI()), Objects.requireNonNull(response.body()).bytes());
        }
    }

    public JSONObject updateFileDescription(String fileId, String description) throws IOException {
        String url = getBaseEndpoint() + "/files/" + fileId;

        JSONObject json = new JSONObject(ImmutableMap.of("item", ImmutableMap.of("description", description)));

        return putResponse(url, json.toString());
    }

    public JSONObject deleteFile(String fileId) throws IOException {
        String url = getBaseEndpoint() + "/files/" + fileId;

        return deleteResponse(url);
    }

    public JSONObject deleteFileGroup(int groupId) throws IOException {
        String url = getBaseEndpoint() + "/groups/" + groupId;

        return deleteResponse(url);
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

    /**
     * Can't use {@link AbstractEndpoint#getResponseObject(String)} because it would write the whole response into memory.
     * This would be a problem if the app file to be downloaded is larger than 1MB.
     * @param url
     * @return
     */
    private Response getResponse(String url) throws IOException {
        Request request = new Request.Builder()
            .header("Authorization", credentials)
            .url(url)
            .build();

        return makeRequest(request);
    }
}
