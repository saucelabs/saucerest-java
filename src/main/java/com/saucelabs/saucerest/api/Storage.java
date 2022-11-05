package com.saucelabs.saucerest.api;

import com.google.common.collect.ImmutableMap;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.model.storage.deleteappfile.DeleteAppFile;
import com.saucelabs.saucerest.model.storage.deletegroupappfiles.DeleteAppGroupFiles;
import com.saucelabs.saucerest.model.storage.editappgroupsettings.EditAppGroupSettings;
import com.saucelabs.saucerest.model.storage.editfiledescription.EditFileDescription;
import com.saucelabs.saucerest.model.storage.getappfiles.GetAppFiles;
import com.saucelabs.saucerest.model.storage.getappgroups.GetAppStorageGroupsResponse;
import com.saucelabs.saucerest.model.storage.getappgroupsettings.GetAppStorageGroupSettings;
import com.saucelabs.saucerest.model.storage.uploadfileapp.UploadFileApp;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class Storage extends AbstractEndpoint {
    public Storage(DataCenter dataCenter) {
        super(dataCenter);
    }

    public Storage(String apiServer) {
        super(apiServer);
    }

    public GetAppFiles getFiles() throws IOException {
        String url = getBaseEndpoint() + "/files";

        return getResponseClass(getResponseObject(url), GetAppFiles.class);
    }

    /**
     * Use parameter names from here
     * <a href="https://docs.saucelabs.com/dev/api/storage/#get-app-storage-files">https://docs.saucelabs.com/dev/api/storage/#get-app-storage-files</a>
     *
     * @param params query parameters for this request
     * @return
     * @throws IOException
     */
    public GetAppFiles getFiles(ImmutableMap<String, Object> params) throws IOException {
        String url = getBaseEndpoint() + "/files";

        return getResponseClass(getResponseObject(url, params), GetAppFiles.class);
    }

    public GetAppStorageGroupsResponse getGroups() throws IOException {
        String url = getBaseEndpoint() + "/groups";

        return getResponseClass(getResponseObject(url), GetAppStorageGroupsResponse.class);
    }

    /**
     * Use parameter names from here
     * <a href="https://docs.saucelabs.com/dev/api/storage/#get-app-storage-groups">https://docs.saucelabs.com/dev/api/storage/#get-app-storage-groups</a>
     *
     * @param params
     * @return
     * @throws IOException
     */
    public GetAppStorageGroupsResponse getGroups(ImmutableMap<String, Object> params) throws IOException {
        String url = getBaseEndpoint() + "/groups";

        return getResponseClass(getResponseObject(url, params), GetAppStorageGroupsResponse.class);
    }

    public GetAppStorageGroupSettings getGroupSettings(int groupId) throws IOException {
        String url = getBaseEndpoint() + "/groups/" + groupId + "/settings";

        return getResponseClass(getResponseObject(url), GetAppStorageGroupSettings.class);
    }

    /**
     * Use parameter names from here
     * <a href="https://docs.saucelabs.com/dev/api/storage/#get-app-storage-group-settings">https://docs.saucelabs.com/dev/api/storage/#get-app-storage-group-settings</a>
     *
     * @param groupId
     * @param jsonBody
     * @return
     * @throws IOException
     */
    public EditAppGroupSettings updateAppStorageGroupSettings(int groupId, String jsonBody) throws IOException {
        String url = getBaseEndpoint() + "/groups/" + groupId + "/settings";

        return getResponseClass(putResponse(url, jsonBody), EditAppGroupSettings.class);
    }

    public UploadFileApp uploadFile(File file) throws IOException {
        return uploadFile(file, "", "");
    }

    public UploadFileApp uploadFile(File file, String fileName) throws IOException {
        return uploadFile(file, fileName, "");
    }

    public UploadFileApp uploadFile(File file, String fileName, String description) throws IOException {
        String url = getBaseEndpoint() + "/upload";

        return getResponseClass(postMultipartResponse(url, file, fileName, description), UploadFileApp.class);
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

    public EditFileDescription updateFileDescription(String fileId, String description) throws IOException {
        String url = getBaseEndpoint() + "/files/" + fileId;

        JSONObject json = new JSONObject(ImmutableMap.of("item", ImmutableMap.of("description", description)));

        return getResponseClass(putResponse(url, json.toString()), EditFileDescription.class);
    }

    public DeleteAppFile deleteFile(String fileId) throws IOException {
        String url = getBaseEndpoint() + "/files/" + fileId;

        return getResponseClass(deleteResponse(url), DeleteAppFile.class);
    }

    public DeleteAppGroupFiles deleteFileGroup(int groupId) throws IOException {
        String url = getBaseEndpoint() + "/groups/" + groupId;

        return getResponseClass(deleteResponse(url), DeleteAppGroupFiles.class);
    }

    private String getBaseEndpoint() {
        return baseURL + "v1/storage";
    }

    /**
     * Need a upload-specific post method for the additional parameters.
     *
     * @param url  Sauce Labs API endpoint
     * @param file mobile app file
     * @return
     * @throws IOException
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
