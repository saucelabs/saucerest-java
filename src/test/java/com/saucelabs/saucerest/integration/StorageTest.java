package com.saucelabs.saucerest.integration;

import com.google.common.collect.ImmutableMap;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.Storage;
import com.saucelabs.saucerest.model.storage.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class StorageTest {
    private final ThreadLocal<Storage> storage = new ThreadLocal<>();

    private static final Storage euCentralStorage = new SauceREST(DataCenter.EU_CENTRAL).getStorage();
    private static final Storage usWestStorage = new SauceREST(DataCenter.US_WEST).getStorage();
    @TempDir
    private Path tempDir;

    /**
     * Use this instead of {@link com.saucelabs.saucerest.integration.DataCenter} because not all regions support
     * app files yet.
     */
    enum Region {
        EU_CENTRAL, US_WEST
    }

    private EditAppGroupSettings getAppGroupResetSettings(EditAppGroupSettings.Builder.Platform platform) {
        if (platform.equals(EditAppGroupSettings.Builder.Platform.ANDROID)) {
            Settings settings = new Settings.Builder()
                .setInstrumentation(new Instrumentation.Builder()
                    .setBiometrics(true)
                    .setImageInjection(true)
                    .setNetworkCapture(true)
                    .build())
                .setInstrumentationEnabled(true)
                .setOrientation("Portrait")
                .setSetupDeviceLock(true)
                .setAudioCapture(true)
                .build();

            return new EditAppGroupSettings.Builder(platform)
                .setSettings(settings)
                .build();
        } else if (platform.equals(EditAppGroupSettings.Builder.Platform.IOS)) {
            Settings settings = new Settings.Builder()
                .setResigning(new Resigning.Builder()
                    .setBiometrics(true)
                    .setImageInjection(true)
                    .setNetworkCapture(true)
                    .build())
                .setResigningEnabled(true)
                .setAudioCapture(true)
                .build();

            return new EditAppGroupSettings.Builder(platform)
                .setSettings(settings)
                .build();
        }

        return null;
    }

    public void setup(Region region) {
        storage.set(new SauceREST(DataCenter.fromString(region.toString())).getStorage());
    }

    @AfterEach
    public void resetAppGroupSettings() throws IOException {
        for (ItemInteger itemInteger : euCentralStorage.getGroups().items) {
            if (EditAppGroupSettings.Builder.Platform.fromString(itemInteger.recent.kind).equals(EditAppGroupSettings.Builder.Platform.ANDROID)) {
                try {
                    euCentralStorage.updateAppStorageGroupSettings(itemInteger.id, Objects.requireNonNull(getAppGroupResetSettings(EditAppGroupSettings.Builder.Platform.ANDROID)));
                } catch (IOException ignored) {
                    System.out.println("Failed to reset app group settings for " + itemInteger.recent.name + " (" + itemInteger.id + ")" + " in EU Central");
                }
            } else if (EditAppGroupSettings.Builder.Platform.fromString(itemInteger.recent.kind).equals(EditAppGroupSettings.Builder.Platform.IOS)) {
                try {
                    euCentralStorage.updateAppStorageGroupSettings(itemInteger.id, Objects.requireNonNull(getAppGroupResetSettings(EditAppGroupSettings.Builder.Platform.IOS)));
                } catch (IOException ignored) {
                    System.out.println("Failed to reset app group settings for " + itemInteger.recent.name + " (" + itemInteger.id + ")" + " in EU Central");
                }
            }
        }

        for (ItemInteger itemInteger : usWestStorage.getGroups().items) {
            if (EditAppGroupSettings.Builder.Platform.fromString(itemInteger.recent.kind).equals(EditAppGroupSettings.Builder.Platform.ANDROID)) {
                try {
                    usWestStorage.updateAppStorageGroupSettings(itemInteger.id, Objects.requireNonNull(getAppGroupResetSettings(EditAppGroupSettings.Builder.Platform.ANDROID)));
                } catch (IOException ignored) {
                    System.out.println("Failed to reset app group settings for " + itemInteger.recent.name + " (" + itemInteger.id + ")" + " in US West");
                }
            } else if (EditAppGroupSettings.Builder.Platform.fromString(itemInteger.recent.kind).equals(EditAppGroupSettings.Builder.Platform.IOS)) {
                try {
                    usWestStorage.updateAppStorageGroupSettings(itemInteger.id, Objects.requireNonNull(getAppGroupResetSettings(EditAppGroupSettings.Builder.Platform.IOS)));
                } catch (IOException ignored) {
                    System.out.println("Failed to reset app group settings for " + itemInteger.recent.name + " (" + itemInteger.id + ")" + " in US West");
                }
            }
        }
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
    public void getAppFilesTestWithBuilder(Region region) throws IOException {
        setup(region);

        StorageParameter storageParameter = new StorageParameter.Builder()
            .setQ("DemoApp")
            .setKind(new String[]{"android"})
            .build();

        GetAppFiles getAppFiles = storage.get().getFiles(storageParameter.toMap());

        Assertions.assertNotNull(getAppFiles.items);
        Assertions.assertNotNull(getAppFiles.totalItems);
        getAppFiles.items.forEach(item -> Assertions.assertEquals("android", item.kind));
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getAppFilesTestWithTwoKinds(Region region) throws IOException {
        setup(region);

        StorageParameter storageParameter = new StorageParameter.Builder()
            .setKind(new String[]{"android", "ios"})
            .build();

        GetAppFiles getAppFiles = storage.get().getFiles(storageParameter.toMap());

        Assertions.assertNotNull(getAppFiles.items);
        Assertions.assertNotNull(getAppFiles.totalItems);
        getAppFiles.items.forEach(item -> Assertions.assertTrue(item.kind.equals("android") || item.kind.equals("ios")));
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
        StorageParameter storageParameter = new StorageParameter.Builder()
            .setQ("DemoApp")
            .setPerPage("5")
            .build();

        GetAppStorageGroups getAppStorageGroups = storage.get().getGroups(storageParameter.toMap());

        Assertions.assertNotNull(getAppStorageGroups);
        getAppStorageGroups.items.forEach(item -> Assertions.assertTrue(item.name.contains("demoapp")));
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void updateAppGroupSettings(Region region) throws IOException {
        setup(region);

        // Get group ID first
        GetAppStorageGroupsParameters groupsParameters = new GetAppStorageGroupsParameters.Builder()
            .setKind("ios")
            .setQ("com.saucelabs.mydemoapp.ios")
            .build();

        GetAppStorageGroups getAppStorageGroups = storage.get().getGroups(groupsParameters.toMap());
        int groupId = getAppStorageGroups.items.get(0).id;

        Settings settings = new Settings.Builder()
            .setAudioCapture(true)
            .build();

        EditAppGroupSettings editAppGroupSettings1 = new EditAppGroupSettings.Builder(EditAppGroupSettings.Builder.Platform.IOS)
            .setSettings(settings)
            .build();

        EditAppGroupSettings editAppGroupSettings = storage.get().updateAppStorageGroupSettings(groupId, editAppGroupSettings1.toJson());

        Assertions.assertTrue(editAppGroupSettings.settings.audioCapture);
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
        GetAppFiles getAppFiles = storage.get().getFiles(new StorageParameter.Builder().setQ("iOS-Real-Device-MyRNDemoApp.ipa").build().toMap());
        String fileId = getAppFiles.items.get(0).id;

        storage.get().downloadFile(fileId, Paths.get(tempDir + "/iOS.ipa"));

        Assertions.assertTrue(Files.exists(Paths.get(tempDir.toString(), "iOS.ipa")));
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void updateAppFileDescription(Region region) throws IOException {
        setup(region);

        // Call getFiles() to get a file ID so we can use it as a parameter
        GetAppFiles getAppFiles = storage.get().getFiles(new StorageParameter.Builder().setQ("iOS-Real-Device-MyRNDemoApp.ipa").build().toMap());
        String fileId = getAppFiles.items.get(0).id;

        storage.get().updateFileDescription(fileId, "Updated through Integration Test");
        GetAppFiles file = storage.get().getFiles(new StorageParameter.Builder().setFileId(new String[]{fileId}).build().toMap());

        Assertions.assertEquals("Updated through Integration Test", file.items.get(0).description);

        storage.get().updateFileDescription(fileId, "");
        file = storage.get().getFiles(new StorageParameter.Builder().setFileId(new String[]{fileId}).build().toMap());

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