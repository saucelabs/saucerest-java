package com.saucelabs.saucerest.api;

import com.google.common.collect.ImmutableMap;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.JobVisibility;
import com.saucelabs.saucerest.TestAsset;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import org.awaitility.Awaitility;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

public class Job extends AbstractEndpoint {
    private final String jobID;

    public Job(DataCenter dataCenter, String sessionId) {
        super(dataCenter);
        this.jobID = sessionId;
    }

    public Job(String apiServer, String sessionId) {
        super(apiServer);
        this.jobID = sessionId;
    }

    public JSONObject getDetails() throws IOException {
        return new JSONObject(getResponseObject(getBaseEndpoint()));
    }

    // Change Details

    public JSONObject changeName(String testName) throws IOException {
        return updateDetails(ImmutableMap.of("name", testName));
    }

    public JSONObject changeBuild(String buildName) throws IOException {
        return updateDetails(ImmutableMap.of("build", buildName));
    }

    public JSONObject changeVisibility(JobVisibility visibility) throws IOException {
        return updateDetails(ImmutableMap.of("public", visibility.value));
    }

    public JSONObject changeResults(Boolean passed) throws IOException {
        return updateDetails(ImmutableMap.of("passed", passed));
    }

    public JSONObject passed() throws IOException {
        return changeResults(true);
    }

    public JSONObject failed() throws IOException {
        return changeResults(false);
    }

    public JSONObject addTags(List<String> tagsList) throws IOException {
        return updateDetails(ImmutableMap.of("tags", tagsList));
    }

    public JSONObject addCustomData(Map<String, Object> customData) throws IOException {
        return updateDetails(ImmutableMap.of("custom-data", customData));
    }

    public JSONObject stop() throws IOException {
        String url = getBaseEndpoint() + "/stop";

        //return putResponse(url, new HashMap<>());
        return new JSONObject(putResponse(url, new HashMap<>()));
    }

    // Note: This works, but docs indicate it should be /rest/v1.1/jobs/{job_id} which doesn't work
    public void delete() throws IOException {
        waitForFinishedTest();
        deleteResponse(getBaseEndpoint());
    }

    public JSONObject availableAssets() throws IOException {
        String url = getBaseEndpoint() + "/assets";

        waitForFinishedTest();
        return new JSONObject(getResponseObject(url));
    }

    public boolean isAssetAvailable(TestAsset asset) throws IOException {
        Collection<Object> availableAssets = availableAssets().toMap().values();
        return availableAssets.stream()
            .map(item -> item instanceof ArrayList ? "screenshots.zip" : (String) item)
            .anyMatch(item -> item.equals(asset.label));
    }

    public void download(TestAsset asset, Path location) throws IOException {
        if (!isAssetAvailable(asset)) {
            throw new FileNotFoundException("Can not find " + asset.label + " in list of available assets");
        }

        downloadKnownAsset(asset, location, "");
    }

    public void downloadAllAssets(Path location) throws IOException {
        downloadAllAssets(location, "");
    }

    public void downloadAllAssets(Path location, String prepend) throws IOException {
        JSONObject jsonObject = availableAssets();
        jsonObject.toMap().values().stream()
            .map(asset -> asset instanceof ArrayList ? "screenshots.zip" : (String) asset)
            .forEach((assetName) -> {
                downloadKnownAsset(TestAsset.get(assetName).get(), location, prepend);
            } );
    }

    public void deleteAllAssets() throws IOException {
        String url = getBaseEndpoint() + "/assets";

        deleteResponse(url);
    }

    private JSONObject updateDetails(Map<String, Object> updates) throws IOException {
        return new JSONObject(putResponse(getBaseEndpoint(), updates));
    }

    private String getBaseEndpoint() {
        return baseURL + "/rest/v1/" + username + "/jobs/" + jobID;
    }

    private void downloadKnownAsset(TestAsset asset, Path location, String prepend) {
        if (!location.getFileName().toString().contains(".")) {
            location = Paths.get(location.toString(), prepend + asset.label);
        }

        String url = getBaseEndpoint() + "/assets/" + asset.label;

        try {
            BufferedSource stream = getStream(url);
            BufferedSink sink = Okio.buffer(Okio.sink(location));
            sink.writeAll(Objects.requireNonNull(stream));
            sink.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitForFinishedTest() {
        String url = getBaseEndpoint() + "/assets";

        Awaitility.await()
            .ignoreExceptionsMatching(e -> e.getMessage().contains("Bad Request"))
            .atMost(Duration.ofSeconds(20))
            .pollInterval(Duration.ofSeconds(1))
            .until(() -> getResponseObject(url) != null);
    }
}
