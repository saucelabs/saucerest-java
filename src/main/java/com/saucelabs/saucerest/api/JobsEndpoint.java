package com.saucelabs.saucerest.api;

import com.saucelabs.saucerest.*;
import com.saucelabs.saucerest.model.jobs.GetJobsParameters;
import com.saucelabs.saucerest.model.jobs.Job;
import com.saucelabs.saucerest.model.jobs.JobAssets;
import com.saucelabs.saucerest.model.jobs.UpdateJobParameter;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.awaitility.Awaitility;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;

public class JobsEndpoint extends AbstractEndpoint {

    public JobsEndpoint(DataCenter dataCenter) {
        super(dataCenter);
    }

    public JobsEndpoint(String apiServer) {
        super(apiServer);
    }

    public JobsEndpoint(String username, String accessKey, DataCenter dataCenter) {
        super(username, accessKey, dataCenter);
    }

    public JobsEndpoint(String username, String accessKey, String apiServer) {
        super(username, accessKey, apiServer);
    }

    /**
     * Get a list of recent jobs run by the specified user.
     *
     * @return {@link ArrayList} of {@link Job} objects
     * @throws IOException if the request fails
     */
    public ArrayList<Job> getJobs() throws IOException {
        String url = super.getBaseEndpoint() + "rest/v1/" + username + "/jobs";

        return new ArrayList<>(deserializeJSONArray(request(url, HttpMethod.GET), Job.class));
    }

    /**
     * Get a list of recent jobs run by the specified user.
     *
     * @param getJobsParameters {@link GetJobsParameters} object
     * @return {@link ArrayList} of {@link Job} objects
     * @throws IOException if the request fails
     */
    public ArrayList<Job> getJobs(GetJobsParameters getJobsParameters) throws IOException {
        String url = super.getBaseEndpoint() + "rest/v1/" + username + "/jobs";

        return new ArrayList<>(deserializeJSONArray(requestWithQueryParameters(url, HttpMethod.GET, getJobsParameters.toMap()), Job.class));
    }

    /**
     * Get detailed information about a specific job.
     *
     * @param jobID The Sauce Labs identifier of the job to be updated. You can look up job IDs using the {@link #getJobs()} endpoint.
     * @return {@link Job} object
     * @throws IOException if the request fails
     */
    public Job getJobDetails(String jobID) throws IOException {
        String url = getBaseEndpoint() + jobID;

        return deserializeJSONObject(request(url, HttpMethod.GET), Job.class);
    }

    /**
     * Edit job attributes based on parameters passed in the request, including setting the status and name of the job.
     * Any parameter for which a new value is provided in the request will replace the existing value.
     * For example, if you provide a set of tags, they will not be added to the current tags; they will replace them,
     * so make sure you pass the entire set you wish to assign.
     *
     * @param jobID              The Sauce Labs identifier of the job to be updated. You can look up job IDs using the {@link #getJobs()} endpoint.
     * @param updateJobParameter {@link UpdateJobParameter} object
     * @return {@link Job} object
     * @throws IOException if the request fails
     */
    public Job updateJob(String jobID, UpdateJobParameter updateJobParameter) throws IOException {
        String url = getBaseEndpoint() + jobID;

        return deserializeJSONObject(request(url, HttpMethod.PUT, updateJobParameter.toMap()), Job.class);
    }

    /**
     * Stop a specific job.
     *
     * @param jobID The Sauce Labs identifier of the job to stop. You can look up job IDs using the Get Jobs endpoint
     * @return {@link Job} object
     * @throws IOException if the request fails
     */
    public Job stopJob(String jobID) throws IOException {
        String url = getBaseEndpoint() + jobID + "/stop";

        return deserializeJSONObject(request(url, HttpMethod.PUT), Job.class);
    }

    /**
     * Delete a job and all of its assets from the Sauce Labs test history.
     *
     * @param jobID ID of the job to be deleted
     * @return {@link Response} object instead of a deserialized object because the response body is empty
     * @throws IOException if the request fails
     */
    public Response deleteJob(String jobID) throws IOException {
        String url = getBaseEndpoint() + jobID;

        return request(url, HttpMethod.DELETE);
    }

    /**
     * Get a list of files associated with a specific test, such as the logs, video, and screenshots.
     *
     * @param jobID The Sauce Labs identifier of the job for which you are retrieving the asset list. You can look up job IDs using the {@link #getJobs()} endpoint.
     * @return {@link JobAssets} object
     * @throws IOException if the request fails
     */
    public JobAssets listJobAssets(String jobID) throws IOException {
        String url = getBaseEndpoint() + jobID + "/assets";

        waitForFinishedTest(jobID);
        waitForBasicTestAssets(jobID);

        return deserializeJSONObject(request(url, HttpMethod.GET), JobAssets.class);
    }

    /**
     * Retrieve one of the asset files associated with a job, such as a log file, video, or screenshot.
     * The response contains the output of the requested file.
     *
     * @param jobID     The Sauce Labs identifier of the job for which you are retrieving the asset list. You can look up job IDs using the {@link #getJobs()} endpoint.
     * @param path      Path including filename where the asset file should be stored
     * @param testAsset {@link TestAsset} object
     * @throws IOException if the request fails
     */
    public void downloadJobAsset(String jobID, Path path, TestAsset testAsset) throws IOException {
        String url = getBaseEndpoint() + jobID + "/assets/" + testAsset.label;

        try (Response response = request(url, HttpMethod.GET)) {
            FileUtils.writeByteArrayToFile(new File(path.toFile().toURI()), Objects.requireNonNull(response.body()).bytes());
        }
    }

    /**
     * Retrieves all of the screenshot files for the specified job and downloads them as a single ZIP file.
     *
     * @param jobID The Sauce Labs identifier of the job for which you are retrieving the asset list. You can look up job IDs using the {@link #getJobs()} endpoint.
     * @param path  Path including filename where the asset file should be stored
     * @throws IOException if the request fails
     */
    public void downloadAllScreenshots(String jobID, Path path) throws IOException {
        String url = getBaseEndpoint() + jobID + "/assets/screenshots.zip";

        try (Response response = request(url, HttpMethod.GET)) {
            FileUtils.writeByteArrayToFile(new File(path.toFile().toURI()), Objects.requireNonNull(response.body()).bytes());
        }
    }

    /**
     * Download all available assets for a specific job.
     *
     * @param jobID The ID of the job
     * @param path  Path to the directory where the assets should be stored
     * @throws IOException if the request fails
     */
    public void downloadAllAssets(String jobID, Path path) throws IOException {
        JobAssets jobAssets = listJobAssets(jobID);
        Map<String, String> assets = jobAssets.getAvailableAssets();
        boolean isAppium = getJobDetails(jobID).automationBackend.equalsIgnoreCase(AutomationBackend.APPIUM.label);

        for (Map.Entry<String, String> entry : assets.entrySet()) {
            String assetLabel = entry.getValue();
            String filename = assetLabel;

            switch (assetLabel) {
                // the API always returns the Appium/Selenium log as selenium-server.log even when using Appium
                // this is a workaround to get the correct filename when downloading the log
                case "selenium-server.log":
                    filename = isAppium ? TestAsset.APPIUM_LOG.label : TestAsset.SELENIUM_LOG.label;
                    break;
                default:
                    break;
            }

            Path assetPath = path.resolve(filename);
            downloadJobAsset(jobID, assetPath, TestAsset.get(assetLabel).get());
        }

        Path screenshotsPath = path.resolve("screenshots.zip");
        downloadAllScreenshots(jobID, screenshotsPath);
    }

    /**
     * This method download the Appium or Selenium server log for a virtual job. <br>
     * A virtual job is a test that is run on a desktop browser VM, emulator or simulator. <br>
     * To download a server log for a real device job, use the API in {@link RealDevicesEndpoint#downloadAppiumLog(String, String)}.
     *
     * @param jobID The ID of the job
     * @param path  Path including filename where the asset file should be stored
     * @throws IOException if the request fails
     */
    public void downloadServerLog(String jobID, Path path) throws IOException {
        // the API always returns the Appium/Selenium log as selenium-server.log even when using Appium
        // this is a workaround to get the correct filename when downloading the log
        if (getJobDetails(jobID).automationBackend.equalsIgnoreCase(AutomationBackend.APPIUM.label)) {
            path = path.resolveSibling(TestAsset.APPIUM_LOG.label);
        } else {
            path = path.resolveSibling(TestAsset.SELENIUM_LOG.label);
        }

        downloadJobAsset(jobID, path, TestAsset.SELENIUM_LOG);
    }

    /**
     * Sauce Labs retains job asset files for 30 days, after which, they are purged,
     * but you can delete the asset files for a job before that, if desired.
     * This request deletes all of the asset files associated with a job.
     * Deleting a single asset file is not supported at this time.
     *
     * @param jobID The Sauce Labs identifier of the job for which you are retrieving the asset list. You can look up job IDs using the {@link #getJobs()} endpoint.
     * @return {@link List} of {@link HashMap} objects which contain the asset name and its size
     * @throws IOException if the request fails
     */
    public List<HashMap<String, Integer>> deleteJobAssets(String jobID) throws IOException {
        String url = getBaseEndpoint() + jobID + "/assets";

        return deserializeJSONObject(request(url, HttpMethod.DELETE), List.class);
    }

    // Change Details

    public Job changeName(String jobID, String testName) throws IOException {
        return updateJob(jobID, new UpdateJobParameter.Builder().setName(testName).build());
    }

    public Job changeBuild(String jobID, String buildName) throws IOException {
        return updateJob(jobID, new UpdateJobParameter.Builder().setBuild(buildName).build());
    }

    public Job changeVisibility(String jobID, JobVisibility visibility) throws IOException {
        return updateJob(jobID, new UpdateJobParameter.Builder().setVisibility(visibility).build());
    }

    public Job changeResults(String jobID, Boolean passed) throws IOException {
        return updateJob(jobID, new UpdateJobParameter.Builder().setPassed(passed).build());
    }

    public Job passed(String jobID) throws IOException {
        return updateJob(jobID, new UpdateJobParameter.Builder().setPassed(true).build());
    }

    public Job failed(String jobID) throws IOException {
        return updateJob(jobID, new UpdateJobParameter.Builder().setPassed(false).build());
    }

    public Job addTags(String jobID, List<String> tagsList) throws IOException {
        return updateJob(jobID, new UpdateJobParameter.Builder().setTags(tagsList).build());
    }

    public Job addCustomData(String jobID, Map<String, String> customData) throws IOException {
        return updateJob(jobID, new UpdateJobParameter.Builder().setCustomData(customData).build());
    }

    @Override
    protected String getBaseEndpoint() {
        return super.getBaseEndpoint() + "rest/v1/" + username + "/jobs/";
    }

    private void waitForFinishedTest(String jobID) {
        String url = getBaseEndpoint() + jobID + "/assets";

        Awaitility.await()
            .ignoreExceptionsMatching(e -> e.getMessage().contains("Bad Request"))
            .atMost(Duration.ofSeconds(60))
            .pollInterval(Duration.ofSeconds(1))
            .until(() -> request(url, HttpMethod.GET).body() != null);
    }

    private void waitForBasicTestAssets(String jobID) {
        String url = getBaseEndpoint() + jobID + "/assets";

        Awaitility.await()
            .ignoreExceptionsMatching(e -> e.getClass().equals(JSONException.class))
            .atMost(Duration.ofMinutes(5))
            .pollInterval(Duration.ofSeconds(10))
            .until(
                () -> {
                    JSONObject response = new JSONObject(request(url, HttpMethod.GET).body().string());
                    return response.has("video") &&
                        response.has(TestAsset.SAUCE_LOG.jsonKey) &&
                        response.has("selenium-log");
                });
    }
}