package com.saucelabs.saucerest.integration;

import com.google.common.collect.ImmutableMap;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.Storage;
import com.saucelabs.saucerest.model.storage.getappfiles.GetAppStorageFilesResponse;
import com.saucelabs.saucerest.model.storage.getappgroups.GetAppStorageGroupsResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class StorageTest {
    private final ThreadLocal<Storage> storage = new ThreadLocal<>();
    @TempDir
    private Path tempDir;

    /**
     * Use this instead of {@link com.saucelabs.saucerest.integration.DataCenter} because not all regions support
     * app files yet.
     */
    enum Region {
        EU(), US();

        Region() {
        }
    }

    public void setup(Region region) {
        storage.set(new SauceREST(DataCenter.fromString(region.toString())).getStorage());
    }

    @BeforeAll
    public static void uploadAppFiles() throws IOException {
        Storage storageEU = new SauceREST(DataCenter.EU).getStorage();
        Storage storageUS = new SauceREST(DataCenter.US).getStorage();

        if (storageEU.getFiles(ImmutableMap.of("q", "DemoApp")).items.size() <= 3 ||
            storageUS.getFiles(ImmutableMap.of("q", "DemoApp")).items.size() <= 3) {
            for (StorageTestHelper.AppFile appFile : StorageTestHelper.AppFile.values()) {
                File file = new StorageTestHelper().getAppFile(appFile);
                new SauceREST(DataCenter.EU).getStorage().uploadFile(file);
                new SauceREST(DataCenter.US).getStorage().uploadFile(file);
            }
        }
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void uploadAppFileTest(Region region) throws IOException {
        setup(region);
        File file = new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.IPA);
        JSONObject response = storage.get().uploadFile(file);

        Assertions.assertEquals(file.getName(), response.getJSONObject("item").getString("name"));
        Assertions.assertEquals("", response.getJSONObject("item").getString("description"));
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void uploadAppFileWithFileNameTest(Region region) throws IOException {
        setup(region);
        File file = new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.IPA);
        JSONObject response = storage.get().uploadFile(file, "test-file-name.ipa");

        Assertions.assertEquals("test-file-name.ipa", response.getJSONObject("item").getString("name"));
        Assertions.assertEquals("", response.getJSONObject("item").getString("description"));
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void uploadAppFileWithFileNameAndDescriptionTest(Region region) throws IOException {
        setup(region);
        File file = new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.IPA);
        JSONObject response = storage.get().uploadFile(file, "test-file-name.ipa", "My App File Description");

        Assertions.assertEquals("test-file-name.ipa", response.getJSONObject("item").getString("name"));
        Assertions.assertEquals("My App File Description", response.getJSONObject("item").getString("description"));
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getAppFilesTest(Region region) throws IOException {
        setup(region);
        GetAppStorageFilesResponse getAppStorageFilesResponse = storage.get().getFiles();

        //Assertions.assertFalse(response.isEmpty());
        Assertions.assertNotNull(getAppStorageFilesResponse);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getAppFilesWithQueryParametersTest(Region region) throws IOException {
        setup(region);
        ImmutableMap<String, Object> queryParameters = ImmutableMap.of("q", "DemoApp", "per_page", "5");
        //JSONObject response = storage.get().getFiles(queryParameters);
        GetAppStorageFilesResponse getAppStorageFilesResponse = storage.get().getFiles(queryParameters);

        Assertions.assertNotNull(getAppStorageFilesResponse);
        Assertions.assertEquals(5, getAppStorageFilesResponse.perPage);
        Assertions.assertTrue(getAppStorageFilesResponse.links.self.contains("DemoApp"));
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getAppGroupsTest(Region region) throws IOException {
        setup(region);
        //JSONObject response = storage.get().getGroups();
        GetAppStorageGroupsResponse getAppStorageGroupsResponse = storage.get().getGroups();

        Assertions.assertNotNull(getAppStorageGroupsResponse);
        //Assertions.assertTrue(response.toMap().size() > 0);
        Assertions.assertTrue(getAppStorageGroupsResponse.items.size() > 0);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getAppGroupsWithQueryParametersTest(Region region) throws IOException {
        setup(region);
        ImmutableMap<String, Object> queryParameters = ImmutableMap.of("q", "DemoApp", "per_page", "5");
        //JSONObject response = storage.get().getGroups(queryParameters);
        GetAppStorageGroupsResponse getAppStorageGroupsResponse = storage.get().getGroups(queryParameters);

        Assertions.assertNotNull(getAppStorageGroupsResponse);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void updateAppGroupSettings(Region region) throws IOException {
        setup(region);

        // Call getGroups() to get the group ID first
        //JSONObject getGroupsResponse = storage.get().getGroups(ImmutableMap.of("kind", "ios"));
        GetAppStorageGroupsResponse getAppStorageGroupsResponse = storage.get().getGroups(ImmutableMap.of("kind", "ios"));
        //int groupId = getGroupsResponse.getJSONArray("items").getJSONObject(0).getInt("id");
        int groupId = getAppStorageGroupsResponse.items.get(0).id;
        //String jsonBody = "{\"settings\":{\"resigning\":{\"image_injection\":false}}}";
        Map<String, Object> rawData = ImmutableMap.of("settings", ImmutableMap.of("resigning", ImmutableMap.of("image_injection", false)));
        String jsonBody = new JSONObject(rawData).toString();

        JSONObject response = storage.get().updateAppStorageGroupSettings(groupId, jsonBody);

        Assertions.assertFalse(response.isEmpty());
        Assertions.assertFalse(response.getJSONObject("settings").getJSONObject("resigning").getBoolean("image_injection"));
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getAppGroupSettingsTest(Region region) throws IOException {
        setup(region);

        // Call getGroups() to get the group ID first
        //JSONObject getGroupsResponse = storage.get().getGroups();
        GetAppStorageGroupsResponse getAppStorageGroupsResponse = storage.get().getGroups();
        //int groupId = getGroupsResponse.getJSONArray("items").getJSONObject(0).getInt("id");
        int groupId = getAppStorageGroupsResponse.items.get(0).id;

        JSONObject response = storage.get().getGroupSettings(groupId);

        Assertions.assertFalse(response.isEmpty());
        Assertions.assertTrue(response.toMap().size() > 0);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void downloadAppTest(Region region) throws IOException {
        setup(region);

        // Call getFiles() to get a file ID so we can use it as a parameter
        GetAppStorageFilesResponse getAppStorageFilesResponse = storage.get().getFiles(ImmutableMap.of("q", "iOS-Real-Device-MyRNDemoApp.ipa"));
        //String fileId = getAppStorageFilesResponse.getJSONArray("items").getJSONObject(0).getString("id");
        String fileId = getAppStorageFilesResponse.items.get(0).id;

        storage.get().downloadFile(fileId, Paths.get(tempDir + "/iOS.ipa"));

        Assertions.assertTrue(Files.exists(Paths.get(tempDir.toString(), "iOS.ipa")));
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void updateAppFileDescription(Region region) throws IOException {
        setup(region);

        // Call getFiles() to get a file ID so we can use it as a parameter
        //JSONObject fileIdResponse = storage.get().getFiles(ImmutableMap.of("q", "iOS-Real-Device-MyRNDemoApp.ipa"));
        //String fileId = fileIdResponse.getJSONArray("items").getJSONObject(0).getString("id");
        GetAppStorageFilesResponse getAppStorageFilesResponse = storage.get().getFiles(ImmutableMap.of("q", "iOS-Real-Device-MyRNDemoApp.ipa"));
        String fileId = getAppStorageFilesResponse.items.get(0).id;

        storage.get().updateFileDescription(fileId, "Updated through Integration Test");

        //JSONObject file = storage.get().getFiles(ImmutableMap.of("file_id", fileId));
        GetAppStorageFilesResponse file = storage.get().getFiles(ImmutableMap.of("file_id", fileId));

        //Assertions.assertEquals("Updated through Integration Test", file.getJSONArray("items").getJSONObject(0).getString("description"));
        Assertions.assertEquals("Updated through Integration Test", file.items.get(0).description);

        storage.get().updateFileDescription(fileId, "");
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void deleteAppFile(Region region) throws IOException {
        setup(region);

        // Upload app file, save file ID
        JSONObject uploadResponse = storage.get().uploadFile(new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.APK));
        String fileId = uploadResponse.getJSONObject("item").getString("id");
        Assertions.assertNotNull(fileId);

        JSONObject deleteResponse = storage.get().deleteFile(fileId);
        String fileIdOfDeletedApp = deleteResponse.getJSONObject("item").getString("id");
        Assertions.assertEquals(fileIdOfDeletedApp, fileId);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void deleteAppGroup(Region region) throws IOException {
        setup(region);

        // Upload app file, save file ID
        JSONObject uploadResponse = storage.get().uploadFile(new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.APK_NATIVE));
        int groupId = uploadResponse.getJSONObject("item").getInt("group_id");
        Assertions.assertNotNull(groupId);

        JSONObject deleteResponse = storage.get().deleteFileGroup(groupId);
        int groupIdOfDeletedGroup = deleteResponse.getJSONObject("item").getInt("id");
        Assertions.assertEquals(groupIdOfDeletedGroup, groupId);
    }
}
