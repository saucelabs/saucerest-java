package com.saucelabs.saucerest;

import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.FailsafeException;
import net.jodah.failsafe.RetryPolicy;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.rmi.UnexpectedException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static com.saucelabs.saucerest.DataCenter.US;

/**
 * Simple Java API that invokes the Sauce REST API.  The full list of the Sauce REST API
 * functionality is available from
 * <a href="https://docs.saucelabs.com/reference/rest-api">https://docs.saucelabs.com/reference/rest-api</a>.
 *
 * @author Ross Rowe
 */
public class SauceREST implements Serializable {

    /**
     * Logger instance.
     */
    private static final Logger logger = Logger.getLogger(SauceREST.class.getName());
    /**
     * 10 seconds in milliseconds.
     */
    private static final long HTTP_READ_TIMEOUT_SECONDS = TimeUnit.SECONDS.toMillis(System.getenv("SAUCE_HTTP_READ_TIMEOUT_SECONDS") != null ? Integer.parseInt(System.getenv("SAUCE_HTTP_READ_TIMEOUT_SECONDS")) : 10);
    /**
     * 10 seconds in milliseconds.
     */
    private static final long HTTP_CONNECT_TIMEOUT_SECONDS = TimeUnit.SECONDS.toMillis(10);
    /**
     * The username to use when performing HTTP requests to the Sauce REST API.
     */
    protected String username;
    /**
     * The access key to use when performing HTTP requests to the Sauce REST API.
     */
    protected String accessKey;

    /**
     * Date format used as part of the file name for downloaded files.
     */
    private static final String DATE_FORMAT = "yyyyMMdd_HHmmSS";

    private static String extraUserAgent = "";

    private final String server;
    private final String edsServer;
    private final String appServer;

    private final String restApiEndpoint;

    /**
     * Retry policy default values.
     */
    private int maxDuration;
    private int maxRetries;
    private int delay;
    private int maxDelay;
    private int delayFactor;
    private ChronoUnit chronoUnit;
    private List<Class<? extends Throwable>> throwableList;

    /**
     * Constructs a new instance of the SauceREST class, uses US as the default data center
     *
     * @param username  The username to use when performing HTTP requests to the Sauce REST API
     * @param accessKey The access key to use when performing HTTP requests to the Sauce REST API
     */
    public SauceREST(String username, String accessKey) {
        this(username, accessKey, US);
    }

    /**
     * Constructs a new instance of the SauceREST class, matching the datacenter string to datacenter object
     *
     * @param username   The username to use when performing HTTP requests to the Sauce REST API
     * @param accessKey  The access key to use when performing HTTP requests to the Sauce REST API
     * @param dataCenter the data center to use
     */
    public SauceREST(String username, String accessKey, String dataCenter) {
        this(username, accessKey, DataCenter.fromString(dataCenter));
    }

    /**
     * Constructs a new instance of the SauceREST class.
     *
     * @param username   The username to use when performing HTTP requests to the Sauce REST API
     * @param accessKey  The access key to use when performing HTTP requests to the Sauce REST API
     * @param dataCenter the datacenter to use
     */
    public SauceREST(String username, String accessKey, DataCenter dataCenter) {
        this(username, accessKey, dataCenter, 15, -1, 1, 5, ChronoUnit.SECONDS,
            2, Collections.singletonList(SauceException.NotYetDone.class));
    }

    public SauceREST(String username, String accessKey, DataCenter dataCenter, int maxDuration, int maxRetries,
                     int delay, int maxDelay, ChronoUnit chronoUnit, int delayFactor,
                     List<Class<? extends Throwable>> throwableList) {
        this.username = username;
        this.accessKey = accessKey;
        this.server = buildUrl(dataCenter.server(), "SAUCE_REST_ENDPOINT", "saucerest-java.base_url");
        this.appServer = buildUrl(dataCenter.appServer(), "SAUCE_REST_APP_ENDPOINT", "saucerest-java.base_app_url");
        this.edsServer = buildUrl(dataCenter.edsServer(), "SAUCE_REST_EDS_ENDPOINT", "saucerest-java.base_eds_url");
        this.restApiEndpoint = server + "rest/v1/";
        this.maxDuration = maxDuration;
        this.maxRetries = maxRetries;
        this.delay = delay;
        this.maxDelay = maxDelay;
        this.chronoUnit = chronoUnit;
        this.delayFactor = delayFactor;
        this.throwableList = throwableList;
    }

    /**
     * Build URL with environment variable, or system property, or default URL.
     *
     * @param defaultUrl         default URL if no URL is found in environment variables and system properties
     * @param envVarName         the name of the environment variable that may contain URL
     * @param systemPropertyName the name of the system property that may contain URL
     * @return URL to use
     */
    private String buildUrl(String defaultUrl, String envVarName, String systemPropertyName) {
        String envVar = System.getenv(envVarName);
        return envVar != null ? envVar : System.getProperty(systemPropertyName, defaultUrl);
    }

    private RetryPolicy<Object> getRetryPolicy() {
        return new RetryPolicy<>()
            .handle(this.throwableList)
            .withMaxDuration(Duration.ofSeconds(this.maxDuration))
            .withMaxRetries(this.maxRetries)
            .withBackoff(this.delay, this.maxDelay, this.chronoUnit, this.delayFactor);
    }

    public static String getExtraUserAgent() {
        return extraUserAgent;
    }

    public static void setExtraUserAgent(String extraUserAgent) {
        SauceREST.extraUserAgent = extraUserAgent;
    }

    /**
     * Returns username assigned to this interface
     *
     * @return Returns username assigned to this interface
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Returns server assigned to this interface
     *
     * @return Returns server assigned to this interface
     */
    public String getServer() {
        return this.server;
    }

    /**
     * Returns eds server assigned to this interface
     *
     * @return Returns eds server assigned to this interface
     */
    public String getEdsServer() {
        return this.edsServer;
    }

    /**
     * Returns app server assigned to this interface
     *
     * @return Returns app server assigned to this interface
     */
    public String getAppServer() {
        return this.appServer;
    }

    /**
     * Returns REST API endpoint assigned to this interface
     *
     * @return Returns REST API endpoint assigned to this interface
     */
    public String getRestApiEndpoint() {
        return this.restApiEndpoint;
    }

    /**
     * Build the url to be
     *
     * @param endpoint Endpoint url, example "info/platforms/appium"
     * @return URL to use in direct fetch functions
     */
    protected URL buildURL(String endpoint) {
        return buildEndpoint(restApiEndpoint, endpoint, "URL");
    }

    private URL buildHarUrl(String jobId) {
        return this.buildEDSURL(jobId + "/network.har");
    }

    /**
     * Build URLs for the EDS server
     *
     * @param endpoint Endpoint url, example "info/platforms/appium"
     * @return URL to use in direct fetch functions
     */
    protected URL buildEDSURL(String endpoint) {
        return buildEndpoint(edsServer, endpoint, "EDS URL");
    }

    private URL buildEndpoint(String server, String endpoint, String urlDescription) {
        try {
            return new URL(new URL(server), endpoint);
        } catch (MalformedURLException e) {
            logger.log(Level.WARNING, e, () -> "Error constructing Sauce " + urlDescription);
            return null;
        }
    }

    protected String getUserAgent() {
        String userAgent = "SauceREST/" + BuildUtils.getCurrentVersion();
        if (!"".equals(getExtraUserAgent())) {
            userAgent = userAgent + " " + getExtraUserAgent();
        }
        logger.log(Level.FINEST, "userAgent is set to {0}", userAgent);
        return userAgent;
    }

    public String doJSONPOST(URL url, JSONObject body) throws SauceException {
        HttpURLConnection postBack = null;
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;

        try {
            postBack = openConnection("POST", url);

            if (postBack instanceof HttpsURLConnection) {
                SauceSSLSocketFactory factory = new SauceSSLSocketFactory();
                ((HttpsURLConnection) postBack).setSSLSocketFactory(factory);
            }
            postBack.setRequestProperty("Content-Type", "application/json");

            logger.log(Level.FINE, "POSTing to {0}", url);
            logger.log(Level.FINE, body.toString(2));   // PrettyPrint JSON with an indent of 2

            postBack.getOutputStream().write(body.toString().getBytes());

            reader = new BufferedReader(new InputStreamReader(postBack.getInputStream()));

            String inputLine;
            logger.log(Level.FINEST, "Building string from response.");
            while ((inputLine = reader.readLine()) != null) {
                logger.log(Level.FINEST, "  {0}", inputLine);
                builder.append(inputLine);
            }
        } catch (IOException e) {
            try {
                if (postBack.getResponseCode() == 401) {
                    logger.log(Level.SEVERE, "Error POSTing to {0}: Unauthorized (401)", url);
                    throw new SauceException.NotAuthorized();
                }
            } catch (IOException e1) {
                logger.log(Level.SEVERE, e, () -> "Error POSTing to " + url + " and getting status code: ");
            }

            logger.log(Level.SEVERE, e, () -> "Error POSTing to " + url + ":");
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            logger.log(Level.SEVERE, e, () -> "Error POSTing to " + url + ":");
        } finally {
            closeInputStream(postBack);
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error closing Sauce input stream", e);
            }
        }
        return builder.toString();
    }


    /**
     * Marks a Sauce job as 'passed'.
     *
     * @param jobId the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     */
    public void jobPassed(String jobId) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("passed", true);
        updateJobInfo(jobId, updates);
    }

    /**
     * Marks a Sauce job as 'failed'.
     *
     * @param jobId the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     */
    public void jobFailed(String jobId) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("passed", false);
        updateJobInfo(jobId, updates);
    }

    /**
     * Adds the provided tags to the Sauce job
     *
     * @param jobId the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param tags  the tags to be added to the job, provided as a list of strings
     */
    public void addTags(String jobId, List<String> tags) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("tags", tags);
        updateJobInfo(jobId, updates);
    }

    private AutomationBackend getAutomationBackend(String jobId) {
        JSONObject jsonObject = new JSONObject(getJobInfo(jobId));
        String automationBackend = jsonObject.getString("automation_backend");

        return Stream.of(AutomationBackend.values())
            .filter(backend -> backend.label.equalsIgnoreCase(automationBackend))
            .findFirst()
            .orElse(null);
    }

    /**
     * Returns a JSON object containing all available assets for a given Sauce job ID.
     *
     * @param jobId the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @return JSON object with available assets for a given job ID
     * @throws IOException if something else goes wrong during asset retrieval
     */
    public BufferedInputStream getAvailableAssets(String jobId) throws IOException {
        URL restEndpoint = buildURL(username + "/jobs/" + jobId + "/assets");
        return downloadFileData(jobId, restEndpoint);
    }

    /**
     * Downloads all available assets for a given Sauce job ID to the <code>location</code>.
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the assets should be downloaded to
     * @throws IOException if file cannot be saved
     */
    public void downloadAllAssets(String jobId, String location) throws IOException {
        boolean hasScreenshots = false;
        boolean isAppiumBackend = getAutomationBackend(jobId) == AutomationBackend.APPIUM;
        JSONObject jsonObject;
        try (BufferedInputStream stream = getAvailableAssets(jobId)) {
            jsonObject = new JSONObject(IOUtils.toString(stream, StandardCharsets.UTF_8));
        }

        // redundant key because JSON response has 2 video keys with the same content: video.mp4 and video.
        // removing one prevents us from downloading the video twice
        // TODO: 2020-10-27 YY: remove this when response is fixed and does not send video.mp4 anymore
        jsonObject.remove("video.mp4");

        if (jsonObject.keySet().contains("screenshots")) {
            hasScreenshots = true;
            // rather download all screenshots using /screenshots.zip than a request per screenshot
            jsonObject.remove("screenshots");
        }

        Iterator<String> keys = jsonObject.keys();

        // iterate response and download assets
        while (keys.hasNext()) {
            String key = keys.next();
            // key:value of this JSONObject are of type string
            if (jsonObject.get(key) instanceof String) {

                String assetName = jsonObject.getString(key);
                String overwriteFilename;
                if (isAppiumBackend && "selenium-log".equalsIgnoreCase(key)) {
                    // this is the appium-server log from a VDC (Emu/Sim) test. This makes sure it is aligned with the
                    // naming used in the web UI
                    overwriteFilename = "appium-server.log";
                } else {
                    overwriteFilename = assetName;
                }
                saveAsset(jobId, assetName, location, getDefaultFileName(jobId, overwriteFilename));
            } else {
                // well, let's hope this case does not happen.
                logger.log(Level.WARNING, "No valid JSON response found.");
            }
        }

        if (hasScreenshots) {
            downloadScreenshots(jobId, location, null);
        }
    }

    /**
     * Downloads the device log for Android Emulator or iOS Simulator.
     *
     * @param jobId      the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location   represents the base directory where the device log should be downloaded to
     * @param isEmulator to determine what device log to download
     * @return True if the device log was downloaded successfully; Otherwise false
     */
    public boolean downloadDeviceLog(String jobId, String location, boolean isEmulator) {
        String filename = isEmulator ? "logcat.log" : "ios-syslog.log";

        return downloadDeviceLog(jobId, location, filename, isEmulator);
    }

    /**
     * Downloads the device log for Android Emulator or iOS Simulator. The file will be stored in a directory
     * specified by the <code>location</code> field.
     *
     * @param jobId      the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location   represents the base directory where the device log should be downloaded to
     * @param filename   represents the filename to store the content
     * @param isEmulator to determine what device log to download
     * @return True if the device log was downloaded successfully; Otherwise false
     */
    public boolean downloadDeviceLog(String jobId, String location, String filename, boolean isEmulator) {
        return handleErrorAtDownloadGracefully(() -> downloadDeviceLogOrThrow(jobId, location, filename, isEmulator));
    }

    /**
     * Downloads the screenshots for a Sauce job to the filesystem. The file will be stored in a directory
     * specified by the <code>location</code> field.
     * <p>
     *
     * @param jobId      the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location   represents the base directory where the device log should be downloaded to
     * @param isEmulator to determine what device log to download
     * @throws FileNotFoundException                                if the log is missing or doesn't exist
     * @throws com.saucelabs.saucerest.SauceException.NotAuthorized if credentials are wrong or missing
     * @throws IOException                                          if something else goes wrong during asset retrieval
     */
    public void downloadDeviceLogOrThrow(String jobId, String location, boolean isEmulator) throws SauceException.NotAuthorized, IOException {
        String filename = (isEmulator ? "logcat" : "ios-syslog") + ".log";
        downloadDeviceLogOrThrow(jobId, location, filename, isEmulator);
    }

    /**
     * Downloads the screenshots for a Sauce job to the filesystem. The file will be stored in a directory
     * specified by the <code>location</code> field.
     * <p>
     *
     * @param jobId      the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location   represents the base directory where the device log should be downloaded to
     * @param filename   represents the filename to store the content
     * @param isEmulator to determine what device log to download
     * @throws FileNotFoundException                                if the log is missing or doesn't exist
     * @throws com.saucelabs.saucerest.SauceException.NotAuthorized if credentials are wrong or missing
     * @throws IOException                                          if something else goes wrong during asset retrieval
     */
    public void downloadDeviceLogOrThrow(String jobId, String location, String filename, boolean isEmulator) throws SauceException.NotAuthorized, IOException {
        String assetName = (isEmulator ? "logcat" : "ios-syslog") + ".log";
        saveAssetOrThrowException(jobId, assetName, location, filename);
    }

    /**
     * Downloads the screenshots as a zip for a Sauce job to the filesystem. The file will be stored in a directory
     * specified by the <code>location</code> field.
     * <p>
     * Jobs are only available for jobs which finished without a Sauce side error
     * <p>
     * If an IOException is encountered during operation, this method will fail _silently_. Prefer {@link #downloadScreenshotsOrThrow(String, String)}
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the screenshots should be downloaded to
     * @return True if the screenshots was downloaded successfully; Otherwise false
     */
    public boolean downloadScreenshots(String jobId, String location) {
        return downloadScreenshots(jobId, location, "screenshots.zip");
    }

    /**
     * Downloads the screenshots as a zip for a Sauce job to the filesystem. The file will be stored in a directory
     * specified by the <code>location</code> field.
     * <p>
     * Jobs are only available for jobs which finished without a Sauce side error
     * <p>
     * If an IOException is encountered during operation, this method will fail _silently_. Prefer {@link #downloadScreenshotsOrThrow(String, String)}
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the screenshots should be downloaded to
     * @param filename represents the filename to store the content
     * @return True if the screenshots was downloaded successfully; Otherwise false
     */
    public boolean downloadScreenshots(String jobId, String location, String filename) {
        return handleErrorAtDownloadGracefully(() -> downloadScreenshotsOrThrow(jobId, location, filename));
    }

    /**
     * Downloads the screenshots for a Sauce job to the filesystem. The file will be stored in a directory
     * specified by the <code>location</code> field.
     * <p>
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the screenshots should be downloaded to
     * @throws FileNotFoundException                                if the log is missing or doesn't exist
     * @throws com.saucelabs.saucerest.SauceException.NotAuthorized if credentials are wrong or missing
     * @throws IOException                                          if something else goes wrong during asset retrieval
     */
    public void downloadScreenshotsOrThrow(String jobId, String location) throws SauceException.NotAuthorized, IOException {
        downloadScreenshotsOrThrow(jobId, location, "screenshots.zip");
    }

    /**
     * Downloads the screenshots for a Sauce job to the filesystem. The file will be stored in a directory
     * specified by the <code>location</code> field.
     * <p>
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the screenshots should be downloaded to
     * @param filename represents the filename to store the content
     * @throws FileNotFoundException                                if the log is missing or doesn't exist
     * @throws com.saucelabs.saucerest.SauceException.NotAuthorized if credentials are wrong or missing
     * @throws IOException                                          if something else goes wrong during asset retrieval
     */
    public void downloadScreenshotsOrThrow(String jobId, String location, String filename) throws SauceException.NotAuthorized, IOException {
        saveAssetOrThrowException(jobId, "screenshots.zip", location, filename);
    }

    /**
     * Downloads the video for a Sauce job to the filesystem. The file will be stored in a directory
     * specified by the <code>location</code> field.
     * <p>
     * Jobs are only available for jobs which finished without a Sauce side error, and for which the 'recordVideo' capability
     * is not set to false.
     * <p>
     * If an IOException is encountered during operation, this method will fail _silently_. Prefer {@link #downloadVideoOrThrow(String, String)}
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the video should be downloaded to
     * @return True if the video was downloaded successfully; Otherwise false
     */
    public boolean downloadVideo(String jobId, String location) {
        return downloadVideo(jobId, location, "video.mp4");
    }

    /**
     * Downloads the video for a Sauce job to the filesystem. The file will be stored in a directory
     * specified by the <code>location</code> field.
     * <p>
     * Jobs are only available for jobs which finished without a Sauce side error, and for which the 'recordVideo' capability
     * is not set to false.
     * <p>
     * If an IOException is encountered during operation, this method will fail _silently_. Prefer {@link #downloadVideoOrThrow(String, String)}
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the video should be downloaded to
     * @param fileName represents the filename to store the content
     * @return True if the video was downloaded successfully; Otherwise false
     */
    public boolean downloadVideo(String jobId, String location, String fileName) {
        return saveAsset(jobId, "video.mp4", location, fileName);
    }

    /**
     * Downloads the video for a Sauce job and returns it.
     * <p>
     * Will probably eat your memory.
     *
     * @param jobId the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @return A BufferedInputStream containing the video info
     * @throws IOException if there is a problem fetching the data
     */
    public BufferedInputStream downloadVideo(String jobId) throws IOException {
        return downloadAssetData(jobId, "video.mp4");
    }

    /**
     * TODO: 2020-02-27 I think this should be called "downloadVideo" and "attemptVideoDownload" should be the silent failure method - Dylan
     * Downloads the video for a Sauce job to the filesystem. The file will be stored in a directory
     * specified by the <code>location</code> field.
     * <p>
     * Jobs are only available for jobs which finished without a Sauce side error, and for which the 'recordVideo' capability
     * is not set to false.
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the video should be downloaded to
     * @throws FileNotFoundException                                if the log is missing or doesn't exist
     * @throws com.saucelabs.saucerest.SauceException.NotAuthorized if credentials are wrong or missing
     * @throws IOException                                          if something else goes wrong during asset retrieval
     */
    public void downloadVideoOrThrow(String jobId, String location) throws SauceException.NotAuthorized, IOException {
        downloadVideoOrThrow(jobId, location, "video.mp4");
    }

    /**
     * TODO: 2020-02-27 I think this should be called "downloadVideo" and "attemptVideoDownload" should be the silent failure method - Dylan
     * Downloads the video for a Sauce job to the filesystem. The file will be stored in a directory
     * specified by the <code>location</code> field.
     * <p>
     * Jobs are only available for jobs which finished without a Sauce side error, and for which the 'recordVideo' capability
     * is not set to false.
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the video should be downloaded to
     * @param fileName represents the filename to store the content
     * @throws FileNotFoundException                                if the log is missing or doesn't exist
     * @throws com.saucelabs.saucerest.SauceException.NotAuthorized if credentials are wrong or missing
     * @throws IOException                                          if something else goes wrong during asset retrieval
     */
    public void downloadVideoOrThrow(String jobId, String location, String fileName) throws SauceException.NotAuthorized, IOException {
        saveAssetOrThrowException(jobId, "video.mp4", location, fileName);
    }

    /**
     * Downloads the log file for a Sauce Job to the filesystem. The file will be stored in a
     * directory specified by the <code>location</code> field.
     * <p>
     * If an IOException is encountered during operation, this method will fail _silently_.  Prefer {@link #downloadLogOrThrow(String, String)}
     * <p>
     * TODO: 2020-02-27 I think this should be called "attemptLogDownload" - Dylan
     * @param jobId    the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the video should be downloaded to
     * @return True if the Log file downloads successfully; Otherwise false.
     *
     * @deprecated Use {@link #downloadSauceLabsLog(String, String)}
     */
    @Deprecated boolean downloadLog(String jobId, String location) {
        return downloadLog(jobId, location, null);
    }

    /**
     * TODO: 27/2/20 I think this should be called "attemptLogDownload" - Dylan
     * Downloads the log file for a Sauce Job to the filesystem. The file will be stored in a
     * directory specified by the <code>location</code> field.
     * <p>
     * If an IOException is encountered during operation, this method will fail _silently_.  Prefer {@link #downloadLogOrThrow(String, String)}
     *
     * @param jobId    the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the video should be downloaded to
     * @param fileName represents the filename to store the content
     * @return True if the Log file downloads successfully; Otherwise false.
     *
     * @deprecated Use {@link #downloadSauceLabsLog(String, String, String)}
     */
    @Deprecated boolean downloadLog(String jobId, String location, String fileName) {
        return saveAsset(jobId, "selenium-server.log", location, fileName);
    }

    /**
     * Downloads the log file for a Sauce Job and returns it.
     *
     * @param jobId the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
     * @return a BufferedInputStream containing the logfile
     * @throws IOException if there is a problem fetching the file
     *
     * @deprecated Use {@link #downloadSauceLabsLog(String)}
     */
    @Deprecated BufferedInputStream downloadLog(String jobId) throws IOException {
        return downloadServerLog(jobId);
    }

    /**
     * TODO: 2020-02-27 I think this should be called "downloadLog" and "attemptLogDownload" should be the silent failure method - Dylan
     * Downloads the log file for a Sauce Job to the filesystem. The file will be stored in a
     * directory specified by the <code>location</code> field.
     *
     * @param jobId    the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the video should be downloaded to
     * @throws FileNotFoundException                                if the log is missing or doesn't exist
     * @throws com.saucelabs.saucerest.SauceException.NotAuthorized if credentials are wrong or missing
     * @throws IOException                                          if something else goes wrong during asset retrieval
     *
     * @deprecated
     */
    @Deprecated public void downloadLogOrThrow(String jobId, String location) throws SauceException.NotAuthorized, IOException {
        downloadLogOrThrow(jobId, location, null);
    }

    /**
     * TODO: 27/2/20 I think this should be called "downloadLog" and "attemptLogDownload" should be the silent failure method - Dylan
     * Downloads the log file for a Sauce Job to the filesystem. The file will be stored in a
     * directory specified by the <code>location</code> field.
     *
     * @param jobId    the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the video should be downloaded to
     * @param fileName represents the filename to store the content
     * @throws FileNotFoundException                                if the log is missing or doesn't exist
     * @throws com.saucelabs.saucerest.SauceException.NotAuthorized if credentials are wrong or missing
     * @throws IOException                                          if something else goes wrong during asset retrieval
     * @deprecated
     */
    @Deprecated public void downloadLogOrThrow(String jobId, String location, String fileName) throws SauceException.NotAuthorized, IOException {
        saveAssetOrThrowException(jobId, "selenium-server.log", location, fileName);
    }

    /**
     * Downloads the log file for a Sauce Job and returns it.
     *
     * @param jobId the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
     * @return a BufferedInputStream containing the logfile
     * @throws IOException if there is a problem fetching the file
     * @deprecated Use {@link #downloadDeviceLog(String, String, boolean)}
     */
    @Deprecated public BufferedInputStream downloadJsonLog(String jobId) throws IOException {
        return downloadSauceLabsLog(jobId);
    }

    /**
     * Downloads the log file for a Sauce Job to the filesystem. The file will be stored in
     * a directory specified by the <code>location</code> field.
     *
     * @param jobId    the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the video should be downloaded to
     * @return True if the Log file downloads successfully; Otherwise false.
     * @deprecated Use {@link #downloadDeviceLog(String, String, boolean)}
     */
    @Deprecated public boolean downloadJsonLog(String jobId, String location) {
        return downloadJsonLog(jobId, location, null);
    }

    /**
     * Downloads the log file for a Sauce Job to the filesystem. The file will be stored in
     * a directory specified by the <code>location</code> field.
     *
     * @param jobId    the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the video should be downloaded to
     * @param fileName represents the filename to store the content
     * @return True if the Log file downloads successfully; Otherwise false.
     * @deprecated Use {@link #downloadDeviceLog(String, String, boolean)}
     */
    @Deprecated public boolean downloadJsonLog(String jobId, String location, String fileName) {
        return downloadSauceLabsLog(jobId, location, fileName);
    }

    /**
     * TODO: 2020-02-27 I think this should be called "attemptLogDownload" - Dylan
     * Downloads the server log for a Sauce Labs job to the filesystem. The file will be stored in a
     * directory specified by the <code>location</code> field.
     * <p>
     * If an IOException is encountered during operation, this method will fail _silently_.  Prefer {@link #downloadServerLogOrThrow(String, String)}
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the video should be downloaded to
     * @return True if the Log file downloads successfully; Otherwise false.
     */
    public boolean downloadServerLog(String jobId, String location) {
        return downloadServerLog(jobId, location, "selenium-server.log");
    }

    /**
     * TODO: 2020-02-27 I think this should be called "attemptLogDownload" - Dylan
     * Downloads the server log for a Sauce Labs job to the filesystem. The file will be stored in a
     * directory specified by the <code>location</code> field.
     * <p>
     * If an IOException is encountered during operation, this method will fail _silently_.  Prefer {@link #downloadServerLogOrThrow(String, String)}
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the video should be downloaded to
     * @param fileName represents the filename to store the content
     * @return True if the Log file downloads successfully; Otherwise false.
     */
    public boolean downloadServerLog(String jobId, String location, String fileName) {
        return handleErrorGracefully("Failed to save file", () -> downloadServerLogOrThrow(jobId, location, fileName));
    }

    /**
     * Downloads the server log for a Sauce Labs job and returns it.
     *
     * @param jobId the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @return a BufferedInputStream containing the logfile
     * @throws IOException if there is a problem fetching the file
     */
    public BufferedInputStream downloadServerLog(String jobId) throws IOException {
        return downloadAssetData(jobId, "selenium-server.log");
    }

    /**
     * TODO: 2020-02-27 I think this should be called "downloadLog" and "attemptLogDownload" should be the silent failure method - Dylan
     * Downloads the log file for a Sauce job to the filesystem.  The file will be stored in a
     * directory specified by the <code>location</code> field.
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the video should be downloaded to
     * @throws FileNotFoundException                                if the log is missing or doesn't exist
     * @throws com.saucelabs.saucerest.SauceException.NotAuthorized if credentials are wrong or missing
     * @throws IOException                                          if something else goes wrong during asset retrieval
     */
    public void downloadServerLogOrThrow(String jobId, String location) throws SauceException.NotAuthorized, IOException {
        downloadServerLogOrThrow(jobId, location, "selenium-server.log");
    }

    /**
     * TODO: 2020-02-27 I think this should be called "downloadLog" and "attemptLogDownload" should be the silent failure method - Dylan
     * Downloads the log file for a Sauce job to the filesystem.  The file will be stored in a
     * directory specified by the <code>location</code> field.
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the video should be downloaded to
     * @param fileName represents the filename to store the content
     * @throws FileNotFoundException                                if the log is missing or doesn't exist
     * @throws com.saucelabs.saucerest.SauceException.NotAuthorized if credentials are wrong or missing
     * @throws IOException                                          if something else goes wrong during asset retrieval
     */
    public void downloadServerLogOrThrow(String jobId, String location, String fileName) throws SauceException.NotAuthorized, IOException {
        URL restEndpoint = this.buildURL(username + "/jobs/" + jobId + "/assets/selenium-server.log");
        saveServerLogFileOrThrow(jobId, location, fileName, restEndpoint);
    }

    /**
     * Downloads the log file for a Sauce Labs job and returns it.
     *
     * @param jobId the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @return a BufferedInputStream containing the logfile
     * @throws IOException if there is a problem fetching the file
     */
    public BufferedInputStream downloadSauceLabsLog(String jobId) throws IOException {
        return downloadAssetData(jobId, "log.json");
    }

    /**
     * Downloads the log file for a Sauce job to the filesystem.  The file will be stored in
     * a directory specified by the <code>location</code> field.
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the video should be downloaded to
     * @return True if the Log file downloads successfully; Otherwise false.
     */
    public boolean downloadSauceLabsLog(String jobId, String location) {
        return downloadSauceLabsLog(jobId, location, "log.json");
    }

    /**
     * Downloads the log file for a Sauce job to the filesystem.  The file will be stored in
     * a directory specified by the <code>location</code> field.
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the video should be downloaded to
     * @param fileName represents the filename to store the content
     * @return True if the Log file downloads successfully; Otherwise false.
     */
    public boolean downloadSauceLabsLog(String jobId, String location, String fileName) {
        return saveAsset(jobId, "log.json", location, fileName);
    }

    /**
     * Download the automator log file for a Sauce job to the filesystem. The file will be stored in a directory
     * specified by the <code>location</code> field.
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the video should be downloaded to
     * @return True if the Log file downloads successfully; Otherwise false.
     */
    public boolean downloadAutomatorLog(String jobId, String location) {
        return downloadAutomatorLog(jobId, location, "automator.log");
    }

    /**
     * Download the automator log file for a Sauce job to the filesystem. The file will be stored in a directory
     * specified by the <code>location</code> field.
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the video should be downloaded to
     * @param filename represents the filename to store the content
     * @return True if the Log file downloads successfully; Otherwise false.
     */
    public boolean downloadAutomatorLog(String jobId, String location, String filename) {
        return saveAsset(jobId, "automator.log", location, filename);
    }

    /**
     * TODO: 2020-02-27 I think this should be renamed "attemptHARDownload" - Dylan
     * Downloads the HAR file for a Sauce job to the filesystem.  The file will be stored in a
     * directory specified by the <code>location</code> field.
     * <p>
     * This will only work for jobs which support Extended Debugging, which were started with the
     * 'extendedDebugging' capability set to true.
     * <p>
     * If an IOException is encountered during operation, this method will fail _silently_.  Prefer {@link #downloadHAROrThrow(String, String)}
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the HAR file should be downloaded to
     * @return True if the HAR file downloads successfully, otherwise false
     */
    public boolean downloadHAR(String jobId, String location) {
        return downloadHAR(jobId, location, "network.har");
    }

    /**
     * TODO: 27/2/20 I think this should be renamed "attemptHARDownload" - Dylan
     * Downloads the HAR file for a Sauce job to the filesystem.  The file will be stored in a
     * directory specified by the <code>location</code> field.
     * <p>
     * This will only work for jobs which support Extended Debugging, which were started with the
     * 'extendedDebugging' capability set to true.
     * <p>
     * If an IOException is encountered during operation, this method will fail _silently_.  Prefer {@link #downloadHAROrThrow(String, String)}
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the HAR file should be downloaded to
     * @param fileName represents the filename to store the content
     * @return True if the HAR file downloads successfully, otherwise false
     */
    public boolean downloadHAR(String jobId, String location, String fileName) {
        URL restEndpoint = buildHarUrl(jobId);
        return saveFile(jobId, location, fileName, restEndpoint);
    }

    /**
     * TODO: 2020-02-27 I think this should be called "downloadHAR" and attemptHARDownload should be the silent failure method - Dylan
     * Downloads the HAR file for a Sauce job to the filesystem.  The file will be stored in a
     * directory specified by the <code>location</code> field.
     * <p>
     * This will only work for jobs which support Extended Debugging, which were started with the
     * 'extendedDebugging' capability set to true.
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the HAR file should be downloaded to
     * @throws FileNotFoundException                                When HAR File is unavailable or doesn't exist.
     * @throws com.saucelabs.saucerest.SauceException.NotAuthorized if credentials are wrong or missing
     * @throws IOException                                          if something else goes wrong during asset retrieval
     */
    public void downloadHAROrThrow(String jobId, String location) throws SauceException.NotAuthorized, IOException {
        downloadHAROrThrow(jobId, location, "network.har");
    }

    /**
     * TODO: 27/2/20 I think this should be called "downloadHAR" and attemptHARDownload should be the silent failure method - Dylan
     * Downloads the HAR file for a Sauce job to the filesystem.  The file will be stored in a
     * directory specified by the <code>location</code> field.
     * <p>
     * This will only work for jobs which support Extended Debugging, which were started with the
     * 'extendedDebugging' capability set to true.
     *
     * @param jobId    the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @param location represents the base directory where the HAR file should be downloaded to
     * @param fileName represents the filename to store the content
     * @throws FileNotFoundException                                When HAR File is unavailable or doesn't exist.
     * @throws com.saucelabs.saucerest.SauceException.NotAuthorized if credentials are wrong or missing
     * @throws IOException                                          if something else goes wrong during asset retrieval
     */
    public void downloadHAROrThrow(String jobId, String location, String fileName) throws SauceException.NotAuthorized, IOException {
        URL restEndpoint = buildHarUrl(jobId);
        saveFileOrThrowException(jobId, location, fileName, restEndpoint);
    }

    /**
     * Downloads the HAR file for a Sauce job.
     * <p>
     * This will only work for jobs which support Extended Debugging, which were started with the
     * 'extendedDebugging' capability set to true.
     *
     * @param jobId the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @return A BufferedInputStream containing the HAR data, unparsed
     * @throws IOException if there is a problem fetching the HAR file
     */
    public BufferedInputStream getHARDataStream(String jobId) throws IOException {
        logger.log(Level.FINEST, "getHARDataStream for {0}", jobId);
        URL restEndpoint = buildHarUrl(jobId);
        return downloadFileData(jobId, restEndpoint);
    }

    /**
     * Downloads the HAR file for a Sauce job, and returns it wrapped in a JSONTokener.
     * <p>
     * Pass this JSONTokener to a JSONObject when you wish to read JSON.  The stream will be read as
     * soon as a JSONObject is created.
     * <p>
     * This will only work for jobs which support Extended Debugging, which were started with the
     * 'extendedDebugging' capability set to true.
     *
     * @param jobId the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @return A JSONTokener containing the HAR data, tokenized
     * @throws IOException   if there is a problem fetching the HAR file
     * @throws JSONException if encoding can't be determined or there's an IO problem
     */
    public JSONTokener getHARData(String jobId) throws IOException, JSONException {
        logger.log(Level.FINEST, "getHARData for {0}", jobId);
        URL restEndpoint = buildHarUrl(jobId);

        BufferedInputStream har_stream = downloadFileData(jobId, restEndpoint);
        return new JSONTokener(har_stream);
    }

    /**
     * Returns the HTTP response for invoking https://saucelabs.com/rest/v1/path.
     *
     * @param path path to append to the url
     * @return HTTP response contents
     */
    public String retrieveResults(String path) {
        URL restEndpoint = buildURL(path);
        return retrieveResults(restEndpoint);
    }

    /**
     * Returns a String (in JSON format) representing the details for a Sauce job.
     *
     * @param jobId the Sauce job ID to retrieve
     * @return String (in JSON format) representing the details for a Sauce job
     */
    public String getJobInfo(String jobId) {
        URL restEndpoint = buildURL(username + "/jobs/" + jobId);
        return retrieveResults(restEndpoint);
    }

    /**
     * Returns a String (in JSON format) representing the details for a Sauce job.
     *
     * @return String (in JSON format) representing the details for a Sauce job
     */
    public String getFullJobs() {
        return getFullJobs(20);
    }

    /**
     * Returns a String (in JSON format) representing the details for a Sauce job.
     *
     * @param limit Number of jobs to return
     * @return String (in JSON format) representing the details for a Sauce job
     */
    public String getFullJobs(int limit) {
        URL restEndpoint = buildURL(username + "/jobs?full=true&limit=" + limit);
        return retrieveResults(restEndpoint);
    }

    /**
     * Returns a String (in JSON format) representing the details for a Sauce job.
     *
     * @return String (in JSON format) representing the jobID for a sauce job
     */
    public String getJobs() {
        URL restEndpoint = buildURL(username + "/jobs");
        return retrieveResults(restEndpoint);
    }


    /**
     * Returns a String (in JSON format) representing the details for a Sauce job.
     *
     * @param limit Number of jobs to return(max of 500)
     * @return String (in JSON format) representing the jobID for a sauce job
     */
    public String getJobs(int limit) {
        URL restEndpoint = buildURL(username + "/jobs?limit=" + limit);
        return retrieveResults(restEndpoint);
    }

    /**
     * Returns a String (in JSON format) representing the details for a Sauce job.
     *
     * @param limit Number of jobs to return(max of 500)
     * @param to    value in Epoch time format denoting the time to end the job list search
     * @param from  value in Epoch time format denoting the time to start the search
     * @return String (in JSON format) representing the jobID for a sauce job
     */
    public String getJobs(int limit, long to, int from) {
        URL restEndpoint = buildURL(username + "/jobs?limit=" + limit + "&from=" + to + "&to=" + from);
        return retrieveResults(restEndpoint);
    }

    /**
     * @param restEndpoint the URL to perform a HTTP GET
     * @return Returns the response from invoking a HTTP GET for the restEndpoint
     */
    public String retrieveResults(URL restEndpoint) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {

            HttpURLConnection connection = openConnection("GET", restEndpoint);

            if (connection instanceof HttpsURLConnection) {
                SauceSSLSocketFactory factory = new SauceSSLSocketFactory();
                ((HttpsURLConnection) connection).setSSLSocketFactory(factory);
            }

            connection.setRequestProperty("charset", "utf-8");

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                builder.append(inputLine);
            }
        } catch (SocketTimeoutException e) {
            logger.log(Level.SEVERE, "Received a SocketTimeoutException when invoking Sauce REST API, check status.saucelabs.com for network outages", e);
        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            logger.log(Level.SEVERE, "Error retrieving Sauce Results", e);
        }
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error closing Sauce input stream", e);
        }
        return builder.toString();
    }

    private BufferedInputStream downloadAssetData(String jobId, String assetName) throws IOException {
        URL restEndpoint = buildURL(username + "/jobs/" + jobId + "/assets/" + assetName);
        return downloadFileData(jobId, restEndpoint);
    }

    /**
     * Returns the result of a HTTP GET to the value of the <code>restEndpoint</code> parameter, as a
     * BufferedInputStream suitable for consumption or saving to file.
     *
     * @param jobId        the Sauce job ID
     * @param restEndpoint the URL to perform a HTTP GET
     * @throws FileNotFoundException        if the requested resource is missing
     * @throws SauceException.NotAuthorized if the credentials are wrong
     * @throws IOException                  when something goes wrong fetching the data
     */
    private BufferedInputStream downloadFileData(String jobId, URL restEndpoint) throws SauceException.NotAuthorized, IOException {
        logger.log(Level.FINE, "Downloading asset {0} For Job {1}", new Object[] { restEndpoint, jobId });
        logger.log(Level.FINEST, "Opening connection for Job {0}", jobId);

        HttpURLConnection connection = null;

        try {
            connection = Failsafe.with(getRetryPolicy()).get(() -> setConnection(jobId, restEndpoint));
        } catch (FailsafeException e) {
            Throwable throwable = e.getCause();

            if (throwable instanceof FileNotFoundException) {
                throw (FileNotFoundException) throwable;
            } else if (throwable instanceof IOException) {
                throw (IOException) throwable;
            } else if (throwable instanceof SauceException.NotAuthorized) {
                throw (SauceException.NotAuthorized) throwable;
            }
        }

        logger.log(Level.FINEST, "Obtaining input stream for request issued for Job {0}", jobId);
        InputStream stream = connection.getInputStream();
        return new BufferedInputStream(stream);
    }

    private HttpURLConnection setConnection(String jobId, URL restEndpoint, String method) throws IOException {
        HttpURLConnection connection = openConnection(method, restEndpoint);

        int responseCode = connection.getResponseCode();
        logger.log(Level.FINEST, "{0} - {1} for: {2}", new Object[] { responseCode, restEndpoint, jobId });
        switch (responseCode) {
            case HttpURLConnection.HTTP_NOT_FOUND:

                String path = restEndpoint.getPath();
                String errorDetails = null;
                if (path.endsWith("mp4")) {
                    errorDetails = ErrorExplainers.videoMissing();
                } else if (path.endsWith("har")) {
                    errorDetails = ErrorExplainers.HARMissing();
                } else if (path.endsWith("log") || path.endsWith("json")) {
                    errorDetails = ErrorExplainers.LogNotFound();
                } else if (path.contains("tunnels")) {
                    errorDetails = ErrorExplainers.TunnelNotFound();
                    throw new SauceException.NotFound(String.join(System.lineSeparator(), errorDetails));
                }

                String error = ErrorExplainers.resourceMissing();
                throw new FileNotFoundException(
                    errorDetails != null ? String.join(System.lineSeparator(), error, errorDetails) : error);
            case HttpURLConnection.HTTP_UNAUTHORIZED:
                String errorReasons = "";
                if (username == null || username.isEmpty()) {
                    errorReasons = String.join(System.lineSeparator(), "Your username is empty or blank.");
                }

                if (accessKey == null || accessKey.isEmpty()) {
                    errorReasons = String.join(System.lineSeparator(), "Your access key is empty or blank.");
                }

                if (!errorReasons.isEmpty()) {
                    errorReasons = (String.join(System.lineSeparator(), errorReasons, ErrorExplainers.missingCreds()));
                } else {
                    errorReasons = ErrorExplainers.incorrectCreds(username, accessKey);
                }

                throw new SauceException.NotAuthorized(errorReasons);
            case HttpURLConnection.HTTP_BAD_REQUEST:
                String errorStream = IOUtils.toString(connection.getErrorStream(), StandardCharsets.UTF_8);

                if (!errorStream.isEmpty() && errorStream.contains("Job hasn't finished running")) {
                    throw new SauceException.NotYetDone(ErrorExplainers.JobNotYetDone());
                }
                break;
            case HttpURLConnection.HTTP_OK:
                break;
            default:
                logger.log(Level.WARNING, "Unknown response code received:{0}", responseCode);
                break;
        }

        return connection;
    }

    private HttpURLConnection setConnection(String jobId, URL restEndpoint) throws IOException {
        return setConnection(jobId, restEndpoint, "GET");
    }

    private HttpURLConnection setConnection(URL restEndpoint, String method) throws IOException {
        return setConnection("", restEndpoint, method);
    }

    private boolean saveAsset(String jobId, String assetName, String location, String fileName) {
        return handleErrorAtDownloadGracefully(() -> saveAssetOrThrowException(jobId, assetName, location, fileName));
    }

    private void saveAssetOrThrowException(String jobId, String assetName, String location, String fileName)
        throws IOException {
        URL restEndpoint = buildURL(username + "/jobs/" + jobId + "/assets/" + assetName);
        saveFileOrThrowException(jobId, location, fileName, restEndpoint);
    }

    /**
     * Stores the result of a HTTP GET to the value of the <code>restEndpoint</code> parameter, saving
     * the resulting file to the directory defined by the <code>location</code> parameter.
     * <p>
     * If an IOException is thrown during this process, this method will fail _silently_ (although it will record the error
     * at Level.WARNING.  Use {@link #saveFileOrThrowException(String, String, String, URL)} to fail with an exception.
     *
     * @param jobId        the Sauce job ID
     * @param location     represents the location that the result file should be stored in
     * @param restEndpoint the URL to perform a HTTP GET
     * @return Whether the request successfully fetched a resource or not
     */
    private boolean saveFile(String jobId, String location, String fileName, URL restEndpoint) {
        return handleErrorAtDownloadGracefully(() -> saveFileOrThrowException(jobId, location, fileName, restEndpoint));
    }

    private String getFileName(String fileName, String jobId, URL restEndpoint) {
        if (fileName == null || fileName.length() < 1) {
            return getDefaultFileName(jobId, FilenameUtils.getName(restEndpoint.getPath()));
        }
        return fileName.replace('/', '_');
    }

    private String getDefaultFileName(String jobId, String overwriteFilename) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return String.format("%s_%s_%s", jobId, format.format(new Date()), overwriteFilename);
    }

    private void saveFileOrThrowException(String jobId, String location, String fileName, URL restEndpoint) throws SauceException.NotAuthorized, IOException {
        logger.log(Level.FINEST, "Attempting to save asset {0} for Job {1} to {2}",
            new Object[] { restEndpoint, jobId, location });

        fileName = getFileName(fileName, jobId, restEndpoint);
        File targetFile = new File(location, fileName);
        System.out.println("Saving " + restEndpoint + " for Job " + jobId + " as " + targetFile);
        logger.log(Level.FINEST, "Saving {0} for Job {1} as {2}", new Object[] { restEndpoint, jobId, targetFile });

        try (BufferedInputStream in = downloadFileData(jobId, restEndpoint)) {
            FileUtils.copyInputStreamToFile(in, targetFile);
        }
    }

    /**
     * Currently Sauce Labs REST API endpoint /asset response contains selenium-server.log regardless if it is actually
     * appium-server.log or selenium-server.log. To workaround this we inspect the returned server log stream
     * and replace it with the appropriate name if necessary.
     *
     * @param jobId        Sauce Labs job ID of the session
     * @param location     where to save the file
     * @param fileName     the filename
     * @param restEndpoint the called endpoint
     * @throws SauceException.NotAuthorized thrown if username/access key are invalid
     * @throws IOException                  thrown when saving the file to disk fails
     */
    private void saveServerLogFileOrThrow(String jobId, String location, String fileName, URL restEndpoint) throws SauceException.NotAuthorized, IOException {
        logger.log(Level.FINEST, "Attempting to save asset {0} for Job {1} to {2}",
            new Object[] { restEndpoint, jobId, location });
        byte[] bytes;
        try (BufferedInputStream in = downloadFileData(jobId, restEndpoint)) {
            bytes = IOUtils.toByteArray(in);
        }

        fileName = getFileName(fileName, jobId, restEndpoint);

        // only change filename if it is default selenium-server.log and if the stream contains Appium
        if (fileName.contains("selenium-server.log") && new String(bytes, StandardCharsets.UTF_8).contains("Appium")) {
            fileName = fileName.replace("selenium-server", "appium-server");
        }

        File targetFile = new File(location, fileName);
        logger.log(Level.FINEST, "Saving {0} for Job {1} as {2}", new Object[] { restEndpoint, jobId, targetFile });

        FileUtils.writeByteArrayToFile(targetFile, bytes);
    }

    private boolean handleErrorAtDownloadGracefully(IOExecutable executable) {
        return handleErrorGracefully("Error downloading Sauce Results", executable);
    }

    private boolean handleErrorGracefully(String logMessage, IOExecutable executable) {
        try {
            executable.execute();
            return true;
        } catch (IOException e) {
            logger.log(Level.WARNING, logMessage, e);
            return false;
        }
    }

    /**
     * Adds an Authorization request property to the HTTP connection.
     *
     * @param connection HttpURLConnection instance which represents the current HTTP request
     */
    protected void addAuthenticationProperty(HttpURLConnection connection) {
        if (username != null && accessKey != null) {
            String auth = encodeAuthentication();
            logger.log(Level.FINE, "Encoded Authorization: {0}", auth);
            connection.setRequestProperty("Authorization", auth);
        }
    }

    /**
     * Invokes the Sauce REST API to update the details of a Sauce job, using the details included in
     * the <code>updates</code> parameter.
     *
     * @param jobId   the Sauce job ID to update
     * @param updates Map of attributes to update
     */
    public void updateJobInfo(String jobId, Map<String, Object> updates) {
        HttpURLConnection postBack = null;
        try {
            URL restEndpoint = buildURL(username + "/jobs/" + jobId);
            postBack = openConnection("PUT", restEndpoint);
            postBack.getOutputStream().write(new JSONObject(updates).toString().getBytes());
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error updating Sauce Results", e);
        }

        closeInputStream(postBack);
    }

    /**
     * Invokes the Sauce REST API to stop a running job.
     *
     * @param jobId the Sauce job ID
     */
    public void stopJob(String jobId) {
        HttpURLConnection postBack = null;

        try {
            URL restEndpoint = buildURL(username + "/jobs/" + jobId + "/stop");

            postBack = openConnection("PUT", restEndpoint);
            postBack.getOutputStream().write("".getBytes());
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error stopping Sauce Job", e);
        }

        closeInputStream(postBack);
    }

    /**
     * Invokes the Sauce REST API to delete a completed job from Sauce.
     *
     * @param jobId the Sauce job ID
     */
    public void deleteJob(String jobId) {
        HttpURLConnection postBack = null;

        try {
            URL restEndpoint = buildURL(username + "/jobs/" + jobId);

            postBack = openConnection("DELETE", restEndpoint);
            postBack.getOutputStream().write("".getBytes());
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error stopping Sauce Job", e);
        }

        closeInputStream(postBack);
    }

    private void closeInputStream(HttpURLConnection connection) {
        try {
            if (connection != null) {
                connection.getInputStream().close();
            }
        } catch (SocketTimeoutException e) {
            logger.log(Level.SEVERE, "Received a SocketTimeoutException when invoking Sauce REST API, check status.saucelabs.com for network outages", e);
        } catch (IOException e) {
            try {
                int responseCode = connection.getResponseCode();
                if (responseCode == 401) {
                    throw new SauceException.NotAuthorized();
                } else if (responseCode == 429) {
                    throw new SauceException.TooManyRequests();
                }
            } catch (IOException ex) {
                logger.log(Level.WARNING, "Error determining response code", e);
            }
            logger.log(Level.WARNING, "Error closing result stream", e);
        }
    }

    /**
     * Opens a connection to a url.
     *
     * @param url URL to connect to
     * @return HttpURLConnection instance representing the URL connection
     * @throws IOException when a bad url is provided
     */
    public HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection con;
        if ("true".equals(System.getenv("USE_PROXY"))) {
            logger.log(Level.SEVERE, "Using proxy: {0}:{1}",
                new Object[] { System.getenv("http.proxyHost"), System.getenv("http.proxyPort") });

            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(System.getenv("http.proxyHost"),
                Integer.parseInt(System.getenv("http.proxyPort"))));
            con = (HttpURLConnection) url.openConnection(proxy);
        } else {
            con = (HttpURLConnection) url.openConnection();
        }
        con.setReadTimeout((int) HTTP_READ_TIMEOUT_SECONDS);
        con.setConnectTimeout((int) HTTP_CONNECT_TIMEOUT_SECONDS);
        return con;
    }

    private HttpURLConnection openConnection(String method, URL url) throws IOException {
        HttpURLConnection connection = openConnection(url);
        connection.setRequestMethod(method);
        connection.setRequestProperty("User-Agent", this.getUserAgent());
        connection.setDoOutput(true);
        addAuthenticationProperty(connection);
        return connection;
    }

    /**
     * Uploads a file to Sauce storage.
     *
     * @param file the file to upload -param fileName uses file.getName() to store in sauce -param
     *             overwrite set to true
     * @return the md5 hash returned by sauce of the file
     * @throws IOException can be thrown when server returns an error (tcp or http status not in the
     *                     200 range)
     */
    public String uploadFile(File file) throws IOException {
        return uploadFile(file, file.getName());
    }

    /**
     * Uploads a file to Sauce storage.
     *
     * @param file     the file to upload
     * @param fileName name of the file in sauce storage -param overwrite set to true
     * @return the md5 hash returned by sauce of the file
     * @throws IOException can be thrown when server returns an error (tcp or http status not in the
     *                     200 range)
     */
    public String uploadFile(File file, String fileName) throws IOException {
        return uploadFile(file, fileName, true);
    }

    /**
     * @param file      the file to upload
     * @param fileName  name of the file in sauce storage
     * @param overwrite boolean flag to overwrite file in sauce storage if it exists
     * @return the md5 hash returned by sauce of the file
     * @throws IOException can be thrown when server returns an error (tcp or http status not in the
     *                     200 range)
     * @deprecated use {@link #uploadFile(File, String, boolean)}.
     * <p>
     * Uploads a file to Sauce storage.
     */
    @Deprecated
    public String uploadFile(File file, String fileName, Boolean overwrite) throws IOException {
        return uploadFile(file, fileName, overwrite.booleanValue());
    }

    /**
     * Uploads a file to Sauce storage.
     *
     * @param file      the file to upload
     * @param fileName  name of the file in sauce storage
     * @param overwrite boolean flag to overwrite file in sauce storage if it exists
     * @return the md5 hash returned by sauce of the file
     * @throws IOException can be thrown when server returns an error (tcp or http status not in the
     *                     200 range)
     */
    public String uploadFile(File file, String fileName, boolean overwrite) throws IOException {
        try (FileInputStream is = new FileInputStream(file)) {
            return uploadFile(is, fileName, overwrite);
        }
    }

    /**
     * @param is        Input stream of the file to be uploaded
     * @param fileName  name of the file in sauce storage
     * @param overwrite boolean flag to overwrite file in sauce storage if it exists
     * @return the md5 hash returned by sauce of the file
     * @throws IOException can be thrown when server returns an error (tcp or http status not in the
     *                     200 range)
     * @deprecated use {@link #uploadFile(InputStream, String, boolean)}.
     * Uploads a file to Sauce storage.
     */
    @Deprecated
    public String uploadFile(InputStream is, String fileName, Boolean overwrite) throws IOException {
        return uploadFile(is, fileName, overwrite.booleanValue());
    }

    /**
     * Uploads a file to Sauce storage.
     *
     * @param is        Input stream of the file to be uploaded
     * @param fileName  name of the file in sauce storage
     * @param overwrite boolean flag to overwrite file in sauce storage if it exists
     * @return the md5 hash returned by sauce of the file
     * @throws IOException can be thrown when server returns an error (tcp or http status not in the
     *                     200 range)
     */
    public String uploadFile(InputStream is, String fileName, boolean overwrite) throws IOException {
        try {
            URL restEndpoint = buildURL("storage/" + username + "/" + fileName + "?overwrite=" + overwrite);

            HttpURLConnection connection = openConnection("POST", restEndpoint);

            if (connection instanceof HttpsURLConnection) {
                SauceSSLSocketFactory factory = new SauceSSLSocketFactory();
                ((HttpsURLConnection) connection).setSSLSocketFactory(factory);
            }

            connection.setUseCaches(false);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("Content-Type", "application/octet-stream");

            try (DataOutputStream oos = new DataOutputStream(connection.getOutputStream())) {
                IOUtils.copy(is, oos);
            }

            String result;
            try (BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                result = IOUtils.toString(rd);
            }

            JSONObject sauceUploadResponse = new JSONObject(result);
            if (sauceUploadResponse.has("error")) {
                throw new UnexpectedException("Failed to upload to sauce-storage: "
                    + sauceUploadResponse.getString("error"));
            }
            return sauceUploadResponse.getString("md5");
        } catch (JSONException e) {
            throw new UnexpectedException("Failed to parse json response.", e);
        } catch (NoSuchAlgorithmException e) {
            throw new UnexpectedException("Failed to get algorithm.", e);
        } catch (KeyManagementException e) {
            throw new UnexpectedException("Failed to get key management.", e);
        }
    }

    /**
     * Generates a link to the job page on Saucelabs.com, which can be accessed without the user's
     * credentials. Auth token is HMAC/MD5 of the job ID with the key &lt;username&gt;:&lt;api key&gt;
     * (see <a href="http://saucelabs.com/docs/integration#public-job-links">http://saucelabs.com/docs/integration#public-job-links</a>).
     *
     * @param jobId the Sauce job ID, typically equal to the Selenium/WebDriver sessionId
     * @return link to the job page with authorization token
     */
    public String getPublicJobLink(String jobId) {
        try {
            String key = username + ":" + accessKey;
            String auth_token = SecurityUtils.hmacEncode("HmacMD5", jobId, key);
            return server + "jobs/" + jobId + "?auth=" + auth_token;
        } catch (IllegalArgumentException ex) {
            // someone messed up on the algorithm to hmacEncode
            // For available algorithms see {@link http://docs.oracle.com/javase/7/docs/api/javax/crypto/Mac.html}
            // we only want to use 'HmacMD5'
            logger.log(Level.WARNING, "Unable to create an authenticated public link to job:", ex);
            return "";
        }
    }

    /**
     * @return base64 encoded String representing the username/access key
     */
    protected String encodeAuthentication() {
        String auth = username + ":" + accessKey;
        auth = "Basic " + Base64.encodeBase64String(auth.getBytes());
        return auth;
    }

    /**
     * Invokes the Sauce REST API to delete a tunnel.
     *
     * @param tunnelId Identifier of the tunnel to delete
     * @return BufferedInputStream with response from server
     */
    public BufferedInputStream deleteTunnel(String tunnelId) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL restEndpoint = buildURL(username + "/tunnels/" + tunnelId);
            connection = setConnection(restEndpoint, "DELETE");
        } catch (IOException e) {
            Throwable throwable = e.getCause();

            if (throwable instanceof SauceException.NotAuthorized) {
                throw (SauceException.NotAuthorized) throwable;
            } else if (throwable instanceof SauceException.NotFound) {
                throw (SauceException.NotFound) throwable;
            }
            logger.log(Level.WARNING, "Error deleting tunnel", e);
        }

        InputStream stream = connection.getInputStream();
        return new BufferedInputStream(stream);
    }

    /**
     * Invokes the Sauce REST API to retrieve the details of the tunnels currently associated with the
     * user.
     *
     * @return String (in JSON format) representing the tunnel information
     */
    public String getTunnels() {
        URL restEndpoint = buildURL(username + "/tunnels");
        return retrieveResults(restEndpoint);
    }

    /**
     * Invokes the Sauce REST API to retrieve the details of the tunnel.
     *
     * @param tunnelId the Sauce Tunnel ID
     * @return String (in JSON format) representing the tunnel information
     */
    public String getTunnelInformation(String tunnelId) {
        URL restEndpoint = buildURL(username + "/tunnels/" + tunnelId);
        return retrieveResults(restEndpoint);
    }

    /**
     * Invokes the Sauce REST API to retrieve the concurrency details of the user.
     *
     * @return String (in JSON format) representing the concurrency information
     */
    public String getConcurrency() {
        URL restEndpoint = buildURL("users/" + username + "/concurrency");
        return retrieveResults(restEndpoint);
    }

    /**
     * Invokes the Sauce REST API to retrieve the activity details of the user.
     *
     * @return String (in JSON format) representing the activity information
     */
    public String getActivity() {
        URL restEndpoint = buildURL(username + "/activity");
        return retrieveResults(restEndpoint);
    }

    /**
     * Returns a String (in JSON format) representing the stored files list
     *
     * @return String (in JSON format) representing the stored files list
     */
    public String getStoredFiles() {
        URL restEndpoint = buildURL("storage/" + username);
        return retrieveResults(restEndpoint);
    }

    /**
     * Returns a String (in JSON format) representing the basic account information
     *
     * @return String (in JSON format) representing the basic account information
     */
    public String getUser() {
        URL restEndpoint = buildURL("users/" + username);
        return retrieveResults(restEndpoint);
    }

    /**
     * Returns a String (in JSON format) representing the list of objects describing all the OS and
     * browser platforms currently supported on Sauce Labs. (see <a href="https://docs.saucelabs.com/reference/rest-api/#get-supported-platforms">https://docs.saucelabs.com/reference/rest-api/#get-supported-platforms</a>).
     *
     * @param automationApi the automation API name
     * @return String (in JSON format) representing the supported platforms information
     */
    public String getSupportedPlatforms(String automationApi) {
        URL restEndpoint = buildURL("info/platforms/" + automationApi);
        return retrieveResults(restEndpoint);
    }

    /**
     * Retrieve jobs associated with a build
     *
     * @param build Build ID
     * @param limit Max jobs to return
     * @return String (in JSON format) representing jobs associated with a build
     */
    public String getBuildFullJobs(String build, int limit) {
        URL restEndpoint = buildURL(username + "/build/" + build + "/jobs?full=1" +
            (limit == 0 ? "" : "&limit=" + limit));
        return retrieveResults(restEndpoint);
    }

    public String getBuildFullJobs(String build) {
        return getBuildFullJobs(build, 0);
    }

    /**
     * Retrieve build info
     *
     * @param build Build name
     * @return String (in JSON format) representing the build
     */
    public String getBuild(String build) {
        URL restEndpoint = buildURL(username + "/builds/" + build); // yes, this goes to builds instead of build like the above
        return retrieveResults(restEndpoint);
    }

    /**
     * Record CI Usage to Sauce Labs
     *
     * @param platform        Platform string. Such as "jenkins", "bamboo", "teamcity"
     * @param platformVersion Version string. Such as "1.1.1"
     * @return if it was a success or not
     */
    public boolean recordCI(String platform, String platformVersion) {
        URL restEndpoint = buildURL("stats/ci");
        JSONObject obj = new JSONObject();
        try {
            obj.put("platform", platform);
            obj.put("platform_version", platformVersion);
        } catch (JSONException e) {
            // JSONException - If the key is null.
            logger.log(Level.SEVERE, "Error attempting to craft json:", e);
            return false;
        }

        try {
            doJSONPOST(restEndpoint, obj);
        } catch (SauceException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SauceREST)) {
            return super.equals(obj);
        }
        SauceREST sauceobj = (SauceREST) obj;
        return Objects.equals(sauceobj.username, this.username) &&
            Objects.equals(sauceobj.accessKey, this.accessKey) &&
            Objects.equals(sauceobj.server, this.server);
    }

    private interface IOExecutable {
        void execute() throws IOException;
    }
}
