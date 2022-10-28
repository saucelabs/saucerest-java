package com.saucelabs.saucerest.integration;

import com.google.common.collect.ImmutableMap;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.Storage;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.File;
import java.io.IOException;

public class StorageTest {
    private final ThreadLocal<Storage> storage = new ThreadLocal<>();

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
        for (StorageTestHelper.AppFile appFile : StorageTestHelper.AppFile.values()) {
            File file = new StorageTestHelper().getAppFile(appFile);
            new SauceREST(DataCenter.EU).getStorage().uploadFile(file);
            new SauceREST(DataCenter.US).getStorage().uploadFile(file);
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
        JSONObject response = storage.get().getFiles();

        Assertions.assertFalse(response.isEmpty());
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getAppFilesWithQueryParametersTest(Region region) throws IOException {
        setup(region);
        ImmutableMap<String, Object> queryParameters = ImmutableMap.of("q", "DemoApp", "per_page", "5");
        JSONObject response = storage.get().getFiles(queryParameters);

        Assertions.assertFalse(response.isEmpty());
        Assertions.assertEquals(5, (int) response.toMap().get("per_page"));
        Assertions.assertTrue(response.toMap().get("links").toString().contains("DemoApp"));
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getAppGroupsTest(Region region) throws IOException {
        setup(region);
        JSONObject response = storage.get().getGroups();

        Assertions.assertFalse(response.isEmpty());
        Assertions.assertTrue(response.toMap().size() > 0);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getAppGroupsWithQueryParametersTest(Region region) throws IOException {
        setup(region);
        ImmutableMap<String, Object> queryParameters = ImmutableMap.of("q", "DemoApp", "per_page", "5");
        JSONObject response = storage.get().getGroups(queryParameters);

        Assertions.assertFalse(response.isEmpty());
        Assertions.assertTrue(response.toMap().size() > 0);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getAppGroupSettingsTest(Region region) throws IOException {
        setup(region);

        // Call getGroups() to get the group ID first
        JSONObject getGroupsResponse = storage.get().getGroups();
        int groupId = getGroupsResponse.getJSONArray("items").getJSONObject(0).getInt("id");

        JSONObject response = storage.get().getGroupSettings(groupId);

        Assertions.assertFalse(response.isEmpty());
        Assertions.assertTrue(response.toMap().size() > 0);
    }
}
