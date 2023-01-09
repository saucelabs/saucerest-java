package com.saucelabs.saucerest.api;

import com.google.common.collect.ImmutableMap;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.model.storage.*;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

public class Storage extends AbstractEndpoint {
    public Storage(DataCenter dataCenter) {
        super(dataCenter);
    }

    public Storage(String apiServer) {
        super(apiServer);
    }

    /**
     * Get files without providing query parameters. Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/storage/#get-app-storage-files">here</a>
     *
     * @return {@link GetAppFiles}
     * @throws IOException API request failed
     */
    public GetAppFiles getFiles() throws IOException {
        String url = getBaseEndpoint() + "/files";

        return getResponseClass(getResponseObject(url), GetAppFiles.class);
    }

    /**
     * Use parameter names from
     * <a href="https://docs.saucelabs.com/dev/api/storage/#get-app-storage-files">here</a>
     * The parameters can be built manually or by using {@link StorageParameter}
     *
     * @param params Query parameters for this request
     * @return {@link GetAppFiles}
     * @throws IOException API request failed
     */
    public GetAppFiles getFiles(Map<String, Object> params) throws IOException {
        String url = getBaseEndpoint() + "/files";

        return getResponseClass(getResponseObject(url, params), GetAppFiles.class);
    }

    /**
     * Get groups without providing query parameters. Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/storage/#get-app-storage-groups">here</a>
     *
     * @return {@link GetAppStorageGroups}
     * @throws IOException API request failed
     */
    public GetAppStorageGroups getGroups() throws IOException {
        String url = getBaseEndpoint() + "/groups";

        return getResponseClass(getResponseObject(url), GetAppStorageGroups.class);
    }

    /**
     * Use parameter names from
     * <a href="https://docs.saucelabs.com/dev/api/storage/#get-app-storage-groups">here</a>
     * The parameters can be built manually or by using {@link StorageParameter}
     *
     * @param params Query parameters for this request
     * @return {@link GetAppStorageGroups}
     * @throws IOException API request failed
     */
    public GetAppStorageGroups getGroups(Map<String, Object> params) throws IOException {
        String url = getBaseEndpoint() + "/groups";

        return getResponseClass(getResponseObject(url, params), GetAppStorageGroups.class);
    }

    /**
     * Get settings of a group. Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/storage/#get-app-storage-group-settings">here</a>
     *
     * @param groupId The ID of the group
     * @return {@link GetAppStorageGroupSettings}
     * @throws IOException API request failed
     */
    public GetAppStorageGroupSettings getGroupSettings(int groupId) throws IOException {
        String url = getBaseEndpoint() + "/groups/" + groupId + "/settings";

        return getResponseClass(getResponseObject(url), GetAppStorageGroupSettings.class);
    }

    /**
     * Use parameter names from here
     * <a href="https://docs.saucelabs.com/dev/api/storage/#get-app-storage-group-settings">here</a>
     *
     * @param groupId  The ID of the group
     * @param jsonBody The app group's settings
     * @return {@link EditAppGroupSettings}
     * @throws IOException API request failed
     */
    public EditAppGroupSettings updateAppStorageGroupSettings(int groupId, String jsonBody) throws IOException {
        String url = getBaseEndpoint() + "/groups/" + groupId + "/settings";

        return getResponseClass(putResponse(url, jsonBody), EditAppGroupSettings.class);
    }

    public EditAppGroupSettings updateAppStorageGroupSettings(int groupId, EditAppGroupSettings editAppGroupSettings) throws IOException {
        String url = getBaseEndpoint() + "/groups/" + groupId + "/settings";

        return getResponseClass(putResponse(url, editAppGroupSettings.toJson()), EditAppGroupSettings.class);
    }

    /**
     * Upload a file to Sauce Labs app storage. Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/storage/#upload-file-to-app-storage">here</a>
     *
     * @param file The file to be uploaded
     * @return {@link UploadFileApp}
     * @throws IOException API request failed
     */
    public UploadFileApp uploadFile(File file) throws IOException {
        return uploadFile(file, "", "");
    }

    /**
     * Upload a file to Sauce Labs app storage. Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/storage/#upload-file-to-app-storage">here</a>
     *
     * @param file     The file to be uploaded
     * @param fileName Set a different filename in Sauce Labs. Default is the filename of the file.
     * @return {@link UploadFileApp}
     * @throws IOException API request failed
     */
    public UploadFileApp uploadFile(File file, String fileName) throws IOException {
        return uploadFile(file, fileName, "");
    }

    /**
     * Upload a file to Sauce Labs app storage. Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/storage/#upload-file-to-app-storage">here</a>
     *
     * @param file        The file to be uploaded
     * @param fileName    Set a different filename in Sauce Labs. Default is the filename of the file.
     * @param description Set a description for this file. Default is empty.
     * @return {@link UploadFileApp}
     * @throws IOException API request failed
     */
    public UploadFileApp uploadFile(File file, String fileName, String description) throws IOException {
        String url = getBaseEndpoint() + "/upload";

        return getResponseClass(postMultipartResponse(url, file, fileName, description), UploadFileApp.class);
    }

    /**
     * Download file from Sauce Labs App Storage. Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/storage/#download-a-file-from-app-storage">here</a>
     *
     * @param fileId The file ID of the file to download
     * @param path   Where to save the file including filename and extension
     * @throws IOException API request failed
     */
    public void downloadFile(String fileId, Path path) throws IOException {
        String url = getBaseEndpoint() + "/download/" + fileId;

        try (Response response = getResponse(url)) {
            FileUtils.writeByteArrayToFile(new File(path.toFile().toURI()), Objects.requireNonNull(response.body()).bytes());
        }
    }

    /**
     * Update a files description. Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/storage/#edit-a-stored-files-description">here</a>
     *
     * @param fileId      The file ID of the file description to change
     * @param description The description to add or update
     * @return {@link EditFileDescription}
     * @throws IOException API request failed
     */
    public EditFileDescription updateFileDescription(String fileId, String description) throws IOException {
        String url = getBaseEndpoint() + "/files/" + fileId;

        JSONObject json = new JSONObject(ImmutableMap.of("item", ImmutableMap.of("description", description)));

        return getResponseClass(putResponse(url, json.toString()), EditFileDescription.class);
    }

    /**
     * Delete a file from app storage. Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/storage/#delete-an-app-storage-file">here</a>
     *
     * @param fileId The ID of the file to delete
     * @return {@link DeleteAppFile}
     * @throws IOException API request failed
     */
    public DeleteAppFile deleteFile(String fileId) throws IOException {
        String url = getBaseEndpoint() + "/files/" + fileId;

        return getResponseClass(deleteResponse(url), DeleteAppFile.class);
    }

    /**
     * Delete a file group. Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/storage/#delete-a-group-of-app-storage-files">here</a>
     *
     * @param groupId The ID of the group to delete
     * @return {@link DeleteAppGroupFiles}
     * @throws IOException API request failed
     */
    public DeleteAppGroupFiles deleteFileGroup(int groupId) throws IOException {
        String url = getBaseEndpoint() + "/groups/" + groupId;

        return getResponseClass(deleteResponse(url), DeleteAppGroupFiles.class);
    }

    /**
     * The base endpoint of the Storage endpoint APIs.
     */
    private String getBaseEndpoint() {
        return baseURL + "v1/storage";
    }

    /**
     * Need an upload-specific post method for the additional parameters.
     *
     * @param url         Sauce Labs API endpoint
     * @param file        App file
     * @param fileName    A different filename for the uploaded app. Default is its local filename
     * @param description An optional description of the app
     * @return A string with the response as a string
     * @throws IOException API request failed
     */
    private String postMultipartResponse(String url, File file, String fileName, String description) throws IOException {
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

            return response.body().string();
        }
    }

    /**
     * Can't use {@link AbstractEndpoint#getResponseObject(String)} because it would write the whole response into memory.
     * This would be a problem if the app file to be downloaded is larger than 1MB.
     *
     * @param url The URL to download the app file
     * @return {@link Response}
     */
    private Response getResponse(String url) throws IOException {
        Request request = new Request.Builder()
            .header("Authorization", credentials)
            .url(url)
            .build();

        return makeRequest(request);
    }
}
