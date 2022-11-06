package com.saucelabs.saucerest.integration;

import com.google.common.collect.ImmutableMap;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.Storage;
import com.saucelabs.saucerest.model.storage.*;
import org.awaitility.Awaitility;
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
import java.util.concurrent.TimeUnit;

public class StorageTest {
    private final ThreadLocal<Storage> storage = new ThreadLocal<>();

    private static final Storage storageEU = new SauceREST(DataCenter.EU).getStorage();
    private static final Storage storageUS = new SauceREST(DataCenter.US).getStorage();
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
    public static void uploadAppFiles() {
        for (StorageTestHelper.AppFile appFile : StorageTestHelper.AppFile.values()) {
            Thread t = new Thread(() -> {
                File file = new StorageTestHelper().getAppFile(appFile);
                try {
                    storageEU.uploadFile(file);
                    storageUS.uploadFile(file);
                } catch (IOException ignored) {
                }
            });
            t.start();
        }

        Awaitility.await()
            .atMost(5, TimeUnit.MINUTES)
            .until(() ->
                storageEU.getGroups().items.size() == 4 &&
                    storageUS.getGroups().items.size() == 4);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void uploadAppFileTest(Region region) throws IOException {
        setup(region);
        File ipaFile = new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.IPA);
        UploadFileApp uploadFileApp = storage.get().uploadFile(ipaFile);

        Assertions.assertEquals(ipaFile.getName(), uploadFileApp.item.name);
        Assertions.assertEquals("", uploadFileApp.item.description);
        Assertions.assertEquals("ios", uploadFileApp.item.kind);

        File apkFile = new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.APK);
        uploadFileApp = storage.get().uploadFile(apkFile);

        Assertions.assertEquals(apkFile.getName(), uploadFileApp.item.name);
        Assertions.assertEquals("", uploadFileApp.item.description);
        Assertions.assertEquals("android", uploadFileApp.item.kind);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void uploadAppFileWithFileNameTest(Region region) throws IOException {
        setup(region);
        File ipaFile = new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.IPA);
        UploadFileApp uploadFileApp = storage.get().uploadFile(ipaFile, "test-file-name.ipa");

        Assertions.assertEquals("test-file-name.ipa", uploadFileApp.item.name);
        Assertions.assertEquals("", uploadFileApp.item.description);
        Assertions.assertEquals("ios", uploadFileApp.item.kind);

        File apkFile = new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.APK);
        uploadFileApp = storage.get().uploadFile(apkFile, "test-file-name.apk");

        Assertions.assertEquals("test-file-name.apk", uploadFileApp.item.name);
        Assertions.assertEquals("", uploadFileApp.item.description);
        Assertions.assertEquals("android", uploadFileApp.item.kind);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void uploadAppFileWithFileNameAndDescriptionTest(Region region) throws IOException {
        setup(region);
        File ipaFile = new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.IPA);
        UploadFileApp uploadFileApp = storage.get().uploadFile(ipaFile, "test-file-name.ipa", "My App File Description");

        Assertions.assertEquals("test-file-name.ipa", uploadFileApp.item.name);
        Assertions.assertEquals("My App File Description", uploadFileApp.item.description);

        File apkFile = new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.APK);
        uploadFileApp = storage.get().uploadFile(apkFile, "test-file-name.apk", "My App File Description");

        Assertions.assertEquals("test-file-name.apk", uploadFileApp.item.name);
        Assertions.assertEquals("My App File Description", uploadFileApp.item.description);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getAppFilesTest(Region region) throws IOException {
        setup(region);
        GetAppFiles getAppFiles = storage.get().getFiles();

        Assertions.assertNotNull(getAppFiles.items);
        Assertions.assertNotNull(getAppFiles.totalItems);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getAppFilesWithQueryParametersTest(Region region) throws IOException {
        setup(region);
        ImmutableMap<String, Object> queryParameters = ImmutableMap.of("q", "DemoApp", "per_page", "5");
        GetAppFiles getAppFiles = storage.get().getFiles(queryParameters);

        Assertions.assertNotNull(getAppFiles);
        Assertions.assertEquals(5, getAppFiles.perPage);
        Assertions.assertTrue(getAppFiles.links.self.contains("DemoApp"));

        queryParameters = ImmutableMap.of("kind", "android");
        getAppFiles = storage.get().getFiles(queryParameters);

        getAppFiles.items.forEach(item -> Assertions.assertEquals("android", item.kind));

        queryParameters = ImmutableMap.of("kind", "ios");
        getAppFiles = storage.get().getFiles(queryParameters);

        getAppFiles.items.forEach(item -> Assertions.assertEquals("ios", item.kind));
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getAppFilesWithQueryParametersTest_404(Region region) throws IOException {
        setup(region);
        ImmutableMap<String, Object> queryParameters = ImmutableMap.of("q", "abc123");
        GetAppFiles getAppFiles = storage.get().getFiles(queryParameters);

        Assertions.assertEquals(0, getAppFiles.items.size());
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getAppGroupsTest(Region region) throws IOException {
        setup(region);
        GetAppStorageGroups getAppStorageGroups = storage.get().getGroups();

        Assertions.assertNotNull(getAppStorageGroups);
        Assertions.assertTrue(getAppStorageGroups.items.size() > 0);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getAppGroupsWithQueryParametersTest(Region region) throws IOException {
        setup(region);
        ImmutableMap<String, Object> queryParameters = ImmutableMap.of("q", "DemoApp", "per_page", "5");
        GetAppStorageGroups getAppStorageGroups = storage.get().getGroups(queryParameters);

        Assertions.assertNotNull(getAppStorageGroups);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void updateAppGroupSettings(Region region) throws IOException {
        setup(region);

        // Call getGroups() to get the group ID first
        GetAppStorageGroups getAppStorageGroups = storage.get().getGroups(ImmutableMap.of("kind", "ios"));
        int groupId = getAppStorageGroups.items.get(0).id;
        Map<String, Object> rawData = ImmutableMap.of("settings", ImmutableMap.of("resigning", ImmutableMap.of("image_injection", false)));
        String jsonBody = new JSONObject(rawData).toString();

        EditAppGroupSettings editAppGroupSettings = storage.get().updateAppStorageGroupSettings(groupId, jsonBody);

        Assertions.assertNotNull(editAppGroupSettings);
        Assertions.assertFalse(editAppGroupSettings.settings.resigning.imageInjection);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getAppGroupSettingsTest(Region region) throws IOException {
        setup(region);

        // Call getGroups() to get the group ID first
        GetAppStorageGroups getAppStorageGroups = storage.get().getGroups();
        int groupId = getAppStorageGroups.items.get(0).id;

        GetAppStorageGroupSettings getGroupSettings = storage.get().getGroupSettings(groupId);

        Assertions.assertNotNull(getGroupSettings);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void downloadAppTest(Region region) throws IOException {
        setup(region);

        // Call getFiles() to get a file ID so we can use it as a parameter
        GetAppFiles getAppFiles = storage.get().getFiles(ImmutableMap.of("q", "iOS-Real-Device-MyRNDemoApp.ipa"));
        String fileId = getAppFiles.items.get(0).id;

        storage.get().downloadFile(fileId, Paths.get(tempDir + "/iOS.ipa"));

        Assertions.assertTrue(Files.exists(Paths.get(tempDir.toString(), "iOS.ipa")));
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void updateAppFileDescription(Region region) throws IOException {
        setup(region);

        // Call getFiles() to get a file ID so we can use it as a parameter
        GetAppFiles getAppFiles = storage.get().getFiles(ImmutableMap.of("q", "iOS-Real-Device-MyRNDemoApp.ipa"));
        String fileId = getAppFiles.items.get(0).id;

        storage.get().updateFileDescription(fileId, "Updated through Integration Test");
        GetAppFiles file = storage.get().getFiles(ImmutableMap.of("file_id", fileId));

        Assertions.assertEquals("Updated through Integration Test", file.items.get(0).description);

        storage.get().updateFileDescription(fileId, "");
        file = storage.get().getFiles(ImmutableMap.of("file_id", fileId));

        Assertions.assertEquals("", file.items.get(0).description);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void deleteAppFile(Region region) throws IOException {
        setup(region);

        // Upload app file, save file ID
        UploadFileApp uploadFileApp = storage.get().uploadFile(new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.APK));
        String fileId = uploadFileApp.item.id;
        Assertions.assertNotNull(fileId);

        DeleteAppFile deleteAppFile = storage.get().deleteFile(fileId);
        String fileIdOfDeletedApp = deleteAppFile.item.id;

        Assertions.assertEquals(fileIdOfDeletedApp, fileId);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void deleteAppGroup(Region region) throws IOException {
        setup(region);

        // Upload app file, save file ID
        UploadFileApp uploadFileApp = storage.get().uploadFile(new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.APK_NATIVE));
        int groupId = uploadFileApp.item.groupId;

        DeleteAppGroupFiles deleteAppGroupFiles = storage.get().deleteFileGroup(groupId);
        int groupIdOfDeletedGroup = deleteAppGroupFiles.item.id;
        Assertions.assertEquals(groupIdOfDeletedGroup, groupId);
    }
}
