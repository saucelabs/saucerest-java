package com.saucelabs.saucerest.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceException;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.AbstractEndpoint;
import com.saucelabs.saucerest.api.StorageEndpoint;
import com.saucelabs.saucerest.model.storage.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class StorageEndpointTest {
  private final ThreadLocal<StorageEndpoint> storage = new ThreadLocal<>();
  private static final Logger logger = Logger.getLogger(AbstractEndpoint.class.getName());

  @TempDir private Path tempDir;

  private EditAppGroupSettings getAppGroupResetSettings(
      EditAppGroupSettings.Builder.Platform platform) {
    Settings.Builder settingsBuilder =
        new Settings.Builder()
            .setAudioCapture(true)
            .setResigningEnabled(platform == EditAppGroupSettings.Builder.Platform.IOS)
            .setInstrumentationEnabled(platform == EditAppGroupSettings.Builder.Platform.ANDROID);

    switch (platform) {
      case ANDROID:
        settingsBuilder
            .setOrientation("Portrait")
            .setSetupDeviceLock(true)
            .setInstrumentation(
                new Instrumentation.Builder()
                    .setBiometrics(true)
                    .setImageInjection(true)
                    .setNetworkCapture(true)
                    .build());
        break;
      case IOS:
        settingsBuilder.setResigning(
            new Resigning.Builder()
                .setBiometrics(true)
                .setImageInjection(true)
                .setNetworkCapture(true)
                .build());
        break;
      default:
        System.out.println("Unknown platform - cannot create AppGroupSettings");
        return null;
    }

    return new EditAppGroupSettings.Builder(platform).setSettings(settingsBuilder.build()).build();
  }

  public void setup(Region region) {
    storage.set(new SauceREST(DataCenter.fromString(region.toString())).getStorageEndpoint());
  }

  @AfterEach
  public void resetAppGroupSettings() {
    try {
      for (ItemInteger itemInteger : storage.get().getGroups().items) {
        EditAppGroupSettings.Builder.Platform platform =
            EditAppGroupSettings.Builder.Platform.fromString(itemInteger.recent.kind);
        if (platform.equals(EditAppGroupSettings.Builder.Platform.ANDROID)
            || platform.equals(EditAppGroupSettings.Builder.Platform.IOS)) {
          resetAppGroupSettingsForPlatform(itemInteger.id, platform);
        } else {
          System.out.println("Not an app - do nothing");
        }
      }
    } catch (IOException | SauceException e) {
      logger.warning("Failed to reset app group settings" + e.getMessage());
    }
  }

  private void resetAppGroupSettingsForPlatform(
      int id, EditAppGroupSettings.Builder.Platform platform) {
    try {
      storage
          .get()
          .updateAppStorageGroupSettings(
              id, Objects.requireNonNull(getAppGroupResetSettings(platform)));
    } catch (IOException ignored) {
      System.out.println(
          "Failed to reset app group settings for "
              + platform.name()
              + " ("
              + id
              + ")"
              + " in EU Central");
    }
  }

  @ParameterizedTest
  @EnumSource(Region.class)
  public void uploadAppFileTest(Region region) throws IOException {
    setup(region);
    File ipaFile = new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.IPA);
    UploadFileApp uploadFileApp = storage.get().uploadFile(ipaFile);

    assertEquals(ipaFile.getName(), uploadFileApp.item.name);
    assertEquals("", uploadFileApp.item.description);
    assertEquals("ios", uploadFileApp.item.kind);

    File apkFile = new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.APK);
    uploadFileApp = storage.get().uploadFile(apkFile);

    assertEquals(apkFile.getName(), uploadFileApp.item.name);
    assertEquals("", uploadFileApp.item.description);
    assertEquals("android", uploadFileApp.item.kind);
  }

  @ParameterizedTest
  @EnumSource(Region.class)
  public void uploadAppFileWithFileNameTest(Region region) throws IOException {
    setup(region);
    File ipaFile = new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.IPA);
    UploadFileApp uploadFileApp = storage.get().uploadFile(ipaFile, "test-file-name.ipa");

    assertEquals("test-file-name.ipa", uploadFileApp.item.name);
    assertEquals("", uploadFileApp.item.description);
    assertEquals("ios", uploadFileApp.item.kind);

    File apkFile = new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.APK);
    uploadFileApp = storage.get().uploadFile(apkFile, "test-file-name.apk");

    assertEquals("test-file-name.apk", uploadFileApp.item.name);
    assertEquals("", uploadFileApp.item.description);
    assertEquals("android", uploadFileApp.item.kind);
  }

  @ParameterizedTest
  @EnumSource(Region.class)
  public void uploadAppFileWithFileNameAndDescriptionTest(Region region) throws IOException {
    setup(region);
    File ipaFile = new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.IPA);
    UploadFileApp uploadFileApp =
        storage.get().uploadFile(ipaFile, "test-file-name.ipa", "My App File Description");

    assertEquals("test-file-name.ipa", uploadFileApp.item.name);
    assertEquals("My App File Description", uploadFileApp.item.description);

    File apkFile = new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.APK);
    uploadFileApp =
        storage.get().uploadFile(apkFile, "test-file-name.apk", "My App File Description");

    assertEquals("test-file-name.apk", uploadFileApp.item.name);
    assertEquals("My App File Description", uploadFileApp.item.description);
  }

  @ParameterizedTest
  @EnumSource(Region.class)
  public void getAppFilesTest(Region region) throws IOException {
    setup(region);
    GetAppFiles getAppFiles = storage.get().getFiles();

    assertNotNull(getAppFiles.items);
    assertNotNull(getAppFiles.totalItems);
  }

  @ParameterizedTest
  @EnumSource(Region.class)
  public void getAppFilesTestWithBuilder(Region region) throws IOException {
    setup(region);

    StorageParameter storageParameter =
        new StorageParameter.Builder().setQ("DemoApp").setKind(new String[] {"android"}).build();

    GetAppFiles getAppFiles = storage.get().getFiles(storageParameter.toMap());

    assertNotNull(getAppFiles.items);
    assertNotNull(getAppFiles.totalItems);
    getAppFiles.items.forEach(item -> assertEquals("android", item.kind));
  }

  @ParameterizedTest
  @EnumSource(Region.class)
  public void getAppFilesTestWithTwoKinds(Region region) throws IOException {
    setup(region);

    StorageParameter storageParameter =
        new StorageParameter.Builder().setKind(new String[] {"android", "ios"}).build();

    GetAppFiles getAppFiles = storage.get().getFiles(storageParameter.toMap());

    assertNotNull(getAppFiles.items);
    assertNotNull(getAppFiles.totalItems);
    getAppFiles.items.forEach(
        item -> assertTrue(item.kind.equals("android") || item.kind.equals("ios")));
  }

  @ParameterizedTest
  @EnumSource(Region.class)
  public void getAppFilesWithQueryParametersTest(Region region) throws IOException {
    setup(region);
    Map<String, Object> queryParameters = Map.of("q", "DemoApp", "per_page", "5");
    GetAppFiles getAppFiles = storage.get().getFiles(queryParameters);

    assertNotNull(getAppFiles);
    assertEquals(5, getAppFiles.perPage);
    assertTrue(getAppFiles.links.self.contains("DemoApp"));

    queryParameters = Map.of("kind", "android");
    getAppFiles = storage.get().getFiles(queryParameters);

    getAppFiles.items.forEach(item -> assertEquals("android", item.kind));

    queryParameters = Map.of("kind", "ios");
    getAppFiles = storage.get().getFiles(queryParameters);

    getAppFiles.items.forEach(item -> assertEquals("ios", item.kind));
  }

  @ParameterizedTest
  @EnumSource(Region.class)
  public void getAppFilesWithQueryParametersTest_404(Region region) throws IOException {
    setup(region);
    Map<String, Object> queryParameters = Map.of("q", "abc123");
    GetAppFiles getAppFiles = storage.get().getFiles(queryParameters);

    assertEquals(0, getAppFiles.items.size());
  }

  @ParameterizedTest
  @EnumSource(Region.class)
  public void getAppGroupsTest(Region region) throws IOException {
    setup(region);
    GetAppStorageGroups getAppStorageGroups = storage.get().getGroups();

    assertNotNull(getAppStorageGroups);
    assertTrue(getAppStorageGroups.items.size() > 0);
  }

  @ParameterizedTest
  @EnumSource(Region.class)
  public void getAppGroupsWithQueryParametersTest(Region region) throws IOException {
    setup(region);
    StorageParameter storageParameter =
        new StorageParameter.Builder().setQ("DemoApp").setPerPage("5").build();

    GetAppStorageGroups getAppStorageGroups = storage.get().getGroups(storageParameter.toMap());

    assertNotNull(getAppStorageGroups);
    getAppStorageGroups.items.forEach(
        item -> assertTrue(item.name.toLowerCase().contains("demoapp")));
  }

  @ParameterizedTest
  @EnumSource(Region.class)
  public void updateAppGroupSettings(Region region) throws IOException {
    setup(region);

    // Get group ID first
    GetAppStorageGroupsParameters groupsParameters =
        new GetAppStorageGroupsParameters.Builder()
            .setKind("ios")
            .setQ("com.saucelabs.mydemoapp.rn")
            .build();

    GetAppStorageGroups getAppStorageGroups = storage.get().getGroups(groupsParameters.toMap());
    int groupId = getAppStorageGroups.items.get(0).id;

    Settings settings = new Settings.Builder().setAudioCapture(true).build();

    EditAppGroupSettings editAppGroupSettings1 =
        new EditAppGroupSettings.Builder(EditAppGroupSettings.Builder.Platform.IOS)
            .setSettings(settings)
            .build();

    EditAppGroupSettings editAppGroupSettings =
        storage.get().updateAppStorageGroupSettings(groupId, editAppGroupSettings1);

    assertTrue(editAppGroupSettings.settings.audioCapture);
  }

  @ParameterizedTest
  @EnumSource(Region.class)
  public void getAppGroupSettingsTest(Region region) throws IOException {
    setup(region);

    // Call getGroups() to get the group ID first
    GetAppStorageGroups getAppStorageGroups = storage.get().getGroups();
    int groupId = getAppStorageGroups.items.get(0).id;

    GetAppStorageGroupSettings getGroupSettings = storage.get().getGroupSettings(groupId);

    assertNotNull(getGroupSettings);
  }

  @ParameterizedTest
  @EnumSource(Region.class)
  public void downloadAppTest(Region region) throws IOException {
    setup(region);

    // Call getFiles() to get a file ID so we can use it as a parameter
    GetAppFiles getAppFiles =
        storage
            .get()
            .getFiles(
                new StorageParameter.Builder()
                    .setQ("iOS-Real-Device-MyRNDemoApp.ipa")
                    .build()
                    .toMap());
    String fileId = getAppFiles.items.get(0).id;

    storage.get().downloadFile(fileId, Paths.get(tempDir + "/iOS.ipa"));

    assertTrue(Files.exists(Paths.get(tempDir.toString(), "iOS.ipa")));
    assertTrue(Files.size(Paths.get(tempDir.toString(), "iOS.ipa")) > 0);
  }

  @ParameterizedTest
  @EnumSource(Region.class)
  public void updateAppFileDescription(Region region) throws IOException {
    setup(region);

    // Call getFiles() to get a file ID so we can use it as a parameter
    GetAppFiles getAppFiles =
        storage
            .get()
            .getFiles(
                new StorageParameter.Builder()
                    .setQ("iOS-Real-Device-MyRNDemoApp.ipa")
                    .build()
                    .toMap());
    String fileId = getAppFiles.items.get(0).id;

    storage.get().updateFileDescription(fileId, "Updated through Integration Test");
    GetAppFiles file =
        storage
            .get()
            .getFiles(
                new StorageParameter.Builder().setFileId(new String[] {fileId}).build().toMap());

    assertEquals("Updated through Integration Test", file.items.get(0).description);

    storage.get().updateFileDescription(fileId, "");
    file =
        storage
            .get()
            .getFiles(
                new StorageParameter.Builder().setFileId(new String[] {fileId}).build().toMap());

    assertEquals("", file.items.get(0).description);
  }

  @ParameterizedTest
  @EnumSource(Region.class)
  public void deleteAppFile(Region region) throws IOException {
    setup(region);

    // Upload app file, save file ID
    UploadFileApp uploadFileApp =
        storage.get().uploadFile(new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.APK));
    String fileId = uploadFileApp.item.id;
    assertNotNull(fileId);

    DeleteAppFile deleteAppFile = storage.get().deleteFile(fileId);
    String fileIdOfDeletedApp = deleteAppFile.item.id;

    assertEquals(fileIdOfDeletedApp, fileId);
  }

  @ParameterizedTest
  @EnumSource(Region.class)
  public void deleteAppGroup(Region region) throws IOException {
    setup(region);

    // Upload app file, save file ID
    UploadFileApp uploadFileApp =
        storage
            .get()
            .uploadFile(new StorageTestHelper().getAppFile(StorageTestHelper.AppFile.APK_NATIVE));
    int groupId = uploadFileApp.item.groupId;

    DeleteAppGroupFiles deleteAppGroupFiles = storage.get().deleteFileGroup(groupId);
    int groupIdOfDeletedGroup = deleteAppGroupFiles.item.id;
    assertEquals(groupIdOfDeletedGroup, groupId);
  }

  @ParameterizedTest
  @EnumSource(Region.class)
  public void appNotFoundTest(Region region) throws IOException {
    setup(region);

    assertThrows(SauceException.NotFound.class, () -> storage.get().deleteFile("abc123"));
  }

  @ParameterizedTest
  @EnumSource(Region.class)
  public void appGroupNotFoundTest(Region region) throws IOException {
    setup(region);

    assertThrows(SauceException.NotFound.class, () -> storage.get().deleteFileGroup(123456789));
  }

  /**
   * Use this instead of {@link com.saucelabs.saucerest.DataCenter} because not all regions support
   * app files yet.
   */
  enum Region {
    EU_CENTRAL,
    US_WEST
  }
}
