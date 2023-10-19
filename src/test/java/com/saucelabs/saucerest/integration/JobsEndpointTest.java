package com.saucelabs.saucerest.integration;

import static com.saucelabs.saucerest.DataCenter.EU_CENTRAL;
import static com.saucelabs.saucerest.DataCenter.US_WEST;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.JobVisibility;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.TestAsset;
import com.saucelabs.saucerest.api.JobsEndpoint;
import com.saucelabs.saucerest.model.jobs.GetJobsParameters;
import com.saucelabs.saucerest.model.jobs.Job;
import com.saucelabs.saucerest.model.jobs.JobAssets;
import com.saucelabs.saucerest.model.jobs.UpdateJobParameter;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Response;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class JobsEndpointTest {
  private final ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();
  private final ThreadLocal<AndroidDriver> androidDriver = new ThreadLocal<>();
  private final ThreadLocal<IOSDriver> iosDriver = new ThreadLocal<>();
  private final ThreadLocal<JobsEndpoint> jobs = new ThreadLocal<>();
  private final ThreadLocal<String> sessionID = new ThreadLocal<>();
  @TempDir private Path tempDir;

  public void createBrowserDriver(DataCenter dataCenter, TestInfo testInfo)
      throws MalformedURLException {
    ChromeOptions options = new ChromeOptions();
    MutableCapabilities sauceOptions = new MutableCapabilities();
    URL url;

    sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
    sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));

    if (testInfo != null && testInfo.getTestMethod().isPresent()) {
      sauceOptions.setCapability("name", testInfo.getTestMethod().get().getName());
    }

    options.setPlatformName("Windows 10");
    options.setCapability("sauce:options", sauceOptions);

    if (DataCenter.EU_CENTRAL == dataCenter) {
      url = new URL("https://ondemand.eu-central-1.saucelabs.com/wd/hub");
      driver.set(new RemoteWebDriver(url, options));
      jobs.set(new SauceREST(EU_CENTRAL).getJobsEndpoint());
      sessionID.set(driver.get().getSessionId().toString());
    } else if (DataCenter.US_WEST == dataCenter) {
      url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");
      driver.set(new RemoteWebDriver(url, options));
      jobs.set(new SauceREST(US_WEST).getJobsEndpoint());
      sessionID.set(driver.get().getSessionId().toString());
    }
  }

  public void createEmulatorDriver(DataCenter dataCenter, TestInfo testInfo)
      throws MalformedURLException {
    MutableCapabilities capabilities = new MutableCapabilities();
    MutableCapabilities sauceOptions = new MutableCapabilities();
    URL url;

    sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
    sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
    sauceOptions.setCapability("appiumVersion", "2.0.0-beta66");

    if (testInfo != null && testInfo.getTestMethod().isPresent()) {
      sauceOptions.setCapability("name", testInfo.getTestMethod().get().getName());
    }

    capabilities.setCapability("platformName", "Android");
    capabilities.setCapability("appium:platformVersion", "13.0");
    capabilities.setCapability("browserName", "Chrome");
    capabilities.setCapability("appium:deviceName", "Android GoogleAPI Emulator");
    capabilities.setCapability("appium:automationName", "UiAutomator2");
    capabilities.setCapability("sauce:options", sauceOptions);

    if (DataCenter.EU_CENTRAL == dataCenter) {
      url = new URL("https://ondemand.eu-central-1.saucelabs.com/wd/hub");
      androidDriver.set(new AndroidDriver(url, capabilities));
      jobs.set(new SauceREST(EU_CENTRAL).getJobsEndpoint());
      sessionID.set(androidDriver.get().getSessionId().toString());
    } else if (DataCenter.US_WEST == dataCenter) {
      url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");
      androidDriver.set(new AndroidDriver(url, capabilities));
      jobs.set(new SauceREST(US_WEST).getJobsEndpoint());
      sessionID.set(androidDriver.get().getSessionId().toString());
    }
  }

  public void createSimulatorDriver(DataCenter dataCenter, TestInfo testInfo)
      throws MalformedURLException {
    MutableCapabilities capabilities = new MutableCapabilities();
    MutableCapabilities sauceOptions = new MutableCapabilities();
    URL url;

    sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
    sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
    sauceOptions.setCapability("appiumVersion", "2.0.0-beta66");

    if (testInfo != null && testInfo.getTestMethod().isPresent()) {
      sauceOptions.setCapability("name", testInfo.getTestMethod().get().getName());
    }

    capabilities.setCapability("platformName", "iOS");
    capabilities.setCapability("appium:platformVersion", "16.2");
    capabilities.setCapability("browserName", "Safari");
    capabilities.setCapability("appium:deviceName", "iPhone Simulator");
    capabilities.setCapability("appium:automationName", "XCUITest");
    capabilities.setCapability("sauce:options", sauceOptions);

    if (DataCenter.EU_CENTRAL == dataCenter) {
      url = new URL("https://ondemand.eu-central-1.saucelabs.com/wd/hub");
      iosDriver.set(new IOSDriver(url, capabilities));
      jobs.set(new SauceREST(EU_CENTRAL).getJobsEndpoint());
      sessionID.set(iosDriver.get().getSessionId().toString());
    } else if (DataCenter.US_WEST == dataCenter) {
      url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");
      iosDriver.set(new IOSDriver(url, capabilities));
      jobs.set(new SauceREST(US_WEST).getJobsEndpoint());
      sessionID.set(iosDriver.get().getSessionId().toString());
    }
  }

  public void setup(DataCenter dataCenter, TestInfo testInfo, TestType testType)
      throws MalformedURLException {
    switch (testType) {
      case BROWSER:
        // Ugly hack to allow testing for "null" as test name in the metadata part of the test
        // result page
        if (testInfo.getTestMethod().isPresent()
            && "getDetails".equalsIgnoreCase(testInfo.getTestMethod().get().getName())) {
          createBrowserDriver(dataCenter, null);
        } else {
          createBrowserDriver(dataCenter, testInfo);
        }
        break;
      case EMULATOR:
        // Ugly hack to allow testing for "null" as test name in the metadata part of the test
        // result page
        if ("getDetails".equalsIgnoreCase(testInfo.getTestMethod().get().getName())) {
          createEmulatorDriver(dataCenter, null);
        } else {
          createEmulatorDriver(dataCenter, testInfo);
        }
        break;
      case SIMULATOR:
        // Ugly hack to allow testing for "null" as test name in the metadata part of the test
        // result page
        if (testInfo.getTestMethod().get().getName().equalsIgnoreCase("getDetails")) {
          createSimulatorDriver(dataCenter, null);
        } else {
          createSimulatorDriver(dataCenter, testInfo);
        }
        break;
    }
  }

  @AfterEach
  public void quitDriver() {
    if (driver.get() != null) {
      driver.get().quit();
    }

    if (androidDriver.get() != null) {
      androidDriver.get().quit();
    }

    if (iosDriver.get() != null) {
      iosDriver.get().quit();
    }
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void getDetails(DataCenter param, TestInfo testInfo) throws IOException {
    runTest(param, testInfo);

    Job job = jobs.get().getJobDetails(sessionID.get());

    assertAll(
        () -> assertEquals("googlechrome", job.browser),
        () -> assertEquals("Windows 10", job.os),
        () -> assertEquals("webdriver", job.automationBackend),
        () -> assertEquals("team", job._public),
        () -> assertTrue(job.recordScreenshots),
        () -> assertTrue(job.recordVideo),
        () -> assertTrue(job.tags.isEmpty()),
        () -> assertNull(job.error),
        () -> assertNull(job.seleniumVersion),
        () -> assertNull(job.name),
        () -> assertNull(job.assignedTunnelId),
        () -> assertNull(job.passed),
        () -> assertNull(job.customData));
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void changeName(DataCenter param, TestInfo testInfo) throws IOException {
    runTest(param, testInfo);
    String newName = "Newly Changed Name";
    Job job = jobs.get().changeName(sessionID.get(), newName);

    assertEquals(newName, job.name);
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void changeBuild(DataCenter param, TestInfo testInfo) throws IOException {
    runTest(param, testInfo);
    String newName = "Newly Changed Build";
    Job job = jobs.get().changeBuild(sessionID.get(), newName);

    assertEquals(newName, job.build);
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void changeVisibility(DataCenter param, TestInfo testInfo) throws IOException {
    runTest(param, testInfo);

    Job job = jobs.get().changeVisibility(sessionID.get(), JobVisibility.PRIVATE);

    assertEquals(JobVisibility.PRIVATE.value, job._public);
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void changeResultsTrue(DataCenter param, TestInfo testInfo) throws IOException {
    runTest(param, testInfo);

    Job job = jobs.get().changeResults(sessionID.get(), true);

    assertEquals(true, job.passed);
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void changeResultsFalse(DataCenter param, TestInfo testInfo) throws IOException {
    runTest(param, testInfo);

    Job job = jobs.get().changeResults(sessionID.get(), false);

    assertEquals(false, job.passed);
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void passed(DataCenter param, TestInfo testInfo) throws IOException {
    runTest(param, testInfo);

    Job job = jobs.get().passed(sessionID.get());

    assertEquals("passed", job.consolidatedStatus);
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void failed(DataCenter param, TestInfo testInfo) throws IOException {
    runTest(param, testInfo);

    Job job = jobs.get().failed(sessionID.get());

    assertEquals("failed", job.consolidatedStatus);
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void addTags(DataCenter param, TestInfo testInfo) throws IOException {
    runTest(param, testInfo);
    List<String> tags = ImmutableList.of("tag1", "tag2", "tag3");

    Job job = jobs.get().addTags(sessionID.get(), tags);

    assertEquals(tags, job.tags);
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void addCustomData(DataCenter param, TestInfo testInfo) throws IOException {
    runTest(param, testInfo);
    Map<String, String> customData =
        ImmutableMap.of("key1", "value1", "key2", "value2", "key3", "value3");

    Job job = jobs.get().addCustomData(sessionID.get(), customData);

    assertEquals(customData, job.customData);
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void getJobsTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(DataCenter.fromString(dataCenter.toString()));
    JobsEndpoint job = sauceREST.getJobsEndpoint();
    List<Job> jobList = job.getJobs();

    assertFalse(jobList.isEmpty());
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void getJobsTestDirectly(DataCenter dataCenter) throws IOException {
    JobsEndpoint job = new JobsEndpoint(DataCenter.fromString(dataCenter.toString()));
    List<Job> jobList = job.getJobs();

    assertFalse(jobList.isEmpty());
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void getJobsWithParametersTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(DataCenter.fromString(dataCenter.toString()));
    JobsEndpoint jobsEndpoint = sauceREST.getJobsEndpoint();

    GetJobsParameters parameters = new GetJobsParameters.Builder().setLimit(10).build();

    ArrayList<Job> jobList = jobsEndpoint.getJobs(parameters);

    assertEquals(10, jobList.size());
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void getJobDetailsTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    JobsEndpoint jobsEndpoint = sauceREST.getJobsEndpoint();
    List<Job> jobList = jobsEndpoint.getJobs();

    Job jobDetails = jobsEndpoint.getJobDetails(jobList.get(0).id);

    assertEquals(jobList.get(0).id, jobDetails.id);
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void updateJobTest(DataCenter dataCenter, TestInfo testInfo) throws IOException {
    runTest(dataCenter, testInfo);

    Job jobDetailsOld = jobs.get().getJobDetails(sessionID.get());

    UpdateJobParameter parameters =
        new UpdateJobParameter.Builder()
            .setName("new name")
            .setBuild("new build")
            .setPassed(true)
            .setVisibility(JobVisibility.SHARE)
            .build();

    jobs.get().updateJob(sessionID.get(), parameters);

    Job jobDetailsNew = jobs.get().getJobDetails(sessionID.get());

    assertAll(
        () -> assertNotEquals(jobDetailsOld.name, jobDetailsNew.name),
        () -> assertNotEquals(jobDetailsOld.build, jobDetailsNew.build),
        () -> assertNotEquals(jobDetailsOld.passed, jobDetailsNew.passed),
        () -> assertNotEquals(jobDetailsOld._public, jobDetailsNew._public));
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void stopJobTest(DataCenter dataCenter, TestInfo testInfo) throws IOException {
    setup(dataCenter, testInfo, TestType.BROWSER);
    String sessionID;
    sessionID = driver.get().getSessionId().toString();

    assertEquals("in progress", jobs.get().getJobDetails(sessionID).status);

    Job job = jobs.get().stopJob(sessionID);

    assertEquals(sessionID, job.id);

    String finalSessionID = sessionID;

    Assertions.assertDoesNotThrow(
        () ->
            Awaitility.await()
                .atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofSeconds(1))
                .pollInSameThread()
                .until(() -> "complete".equals(jobs.get().getJobDetails(finalSessionID).status)));

    // After stopping a test calling driver.quit() is futile and will result in a TimeoutException
    driver.set(null);
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void deleteJobTest(DataCenter dataCenter, TestInfo testInfo) throws IOException {
    runTest(dataCenter, testInfo);

    try (Response response = jobs.get().deleteJob(sessionID.get())) {
      assertEquals(200, response.code());
    }
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void listJobAssetsTest(DataCenter dataCenter, TestInfo testInfo) throws IOException {
    runTest(dataCenter, testInfo);

    JobAssets jobAssets = jobs.get().listJobAssets(sessionID.get());

    assertAll(
        () -> assertFalse(jobAssets.video.isEmpty()),
        () -> assertFalse(jobAssets.sauceLog.isEmpty()),
        () -> assertFalse(jobAssets.screenshots.isEmpty()),
        () -> assertFalse(jobAssets.seleniumLog.isEmpty()),
        () -> assertFalse(jobAssets.videoMp4.isEmpty()));
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void listJobAssetsForEmulatorTest(DataCenter dataCenter, TestInfo testInfo)
      throws IOException {
    runEmulatorTest(dataCenter, testInfo);

    JobAssets jobAssets = jobs.get().listJobAssets(sessionID.get());

    assertAll(
        () -> assertFalse(jobAssets.video.isEmpty()),
        () -> assertFalse(jobAssets.sauceLog.isEmpty()),
        () -> assertFalse(jobAssets.screenshots.isEmpty()),
        () -> assertFalse(jobAssets.seleniumLog.isEmpty()),
        () -> assertFalse(jobAssets.videoMp4.isEmpty()));
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void listJobAssetsForSimulatorTest(DataCenter dataCenter, TestInfo testInfo)
      throws IOException {
    runSimulatorTest(dataCenter, testInfo);

    JobAssets jobAssets = jobs.get().listJobAssets(sessionID.get());

    assertAll(
        () -> assertFalse(jobAssets.video.isEmpty()),
        () -> assertFalse(jobAssets.sauceLog.isEmpty()),
        () -> assertFalse(jobAssets.screenshots.isEmpty()),
        () -> assertFalse(jobAssets.seleniumLog.isEmpty()),
        () -> assertFalse(jobAssets.videoMp4.isEmpty()));
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void getJobAssetFileTest(DataCenter dataCenter, TestInfo testInfo) throws IOException {
    runTest(dataCenter, testInfo);

    jobs.get()
        .downloadJobAsset(
            sessionID.get(),
            Paths.get(tempDir + "/" + TestAsset.SAUCE_LOG.label),
            TestAsset.SAUCE_LOG);

    assertTrue(Files.exists(Paths.get(tempDir.toString(), "log.json")));
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void getAllAssetsTest(DataCenter dataCenter, TestInfo testInfo) throws IOException {
    runTest(dataCenter, testInfo);

    jobs.get().downloadAllAssets(sessionID.get(), Paths.get(tempDir + "/"));

    assertAll(
        () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), TestAsset.SAUCE_LOG.label))),
        () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), TestAsset.VIDEO.label))),
        () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), TestAsset.SCREENSHOTS.label))),
        () ->
            assertTrue(Files.exists(Paths.get(tempDir.toString(), TestAsset.SELENIUM_LOG.label))));
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void getAllEmulatorAssetsTest(DataCenter dataCenter, TestInfo testInfo)
      throws IOException {
    runEmulatorTest(dataCenter, testInfo);

    jobs.get().downloadAllAssets(sessionID.get(), Paths.get(tempDir + "/"));

    assertAll(
        () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), TestAsset.SAUCE_LOG.label))),
        () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), TestAsset.VIDEO.label))),
        () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), TestAsset.APPIUM_LOG.label))),
        () ->
            assertFalse(Files.exists(Paths.get(tempDir.toString(), TestAsset.SELENIUM_LOG.label))),
        () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), TestAsset.LOGCAT_LOG.label))));
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void getAllSimulatorAssetsTest(DataCenter dataCenter, TestInfo testInfo)
      throws IOException {
    runSimulatorTest(dataCenter, testInfo);

    jobs.get().downloadAllAssets(sessionID.get(), Paths.get(tempDir + "/"));

    assertAll(
        () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), TestAsset.SAUCE_LOG.label))),
        () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), TestAsset.VIDEO.label))),
        () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), TestAsset.APPIUM_LOG.label))),
        () ->
            assertFalse(Files.exists(Paths.get(tempDir.toString(), TestAsset.SELENIUM_LOG.label))),
        () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), TestAsset.SYSLOG_LOG.label))));
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void getAllScreenshotsTest(DataCenter dataCenter, TestInfo testInfo) throws IOException {
    runTest(dataCenter, testInfo);

    jobs.get()
        .downloadAllScreenshots(
            sessionID.get(), Paths.get(tempDir + "/" + TestAsset.SCREENSHOTS.label));

    assertTrue(Files.exists(Paths.get(tempDir.toString(), "screenshots.zip")));
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void deleteJobAssetsTest(DataCenter dataCenter, TestInfo testInfo) throws IOException {
    runTest(dataCenter, testInfo);

    List<HashMap<String, Integer>> response = jobs.get().deleteJobAssets(sessionID.get());

    assertFalse(response.isEmpty());
  }

  private void runTest(DataCenter dataCenter, TestInfo testInfo) throws MalformedURLException {
    setup(dataCenter, testInfo, TestType.BROWSER);
    quitDriver();
    Awaitility.await()
        .atMost(Duration.ofSeconds(20))
        .pollInterval(Duration.ofSeconds(1))
        .pollInSameThread()
        .until(() -> "complete".equals(jobs.get().getJobDetails(sessionID.get()).status));
  }

  private void runEmulatorTest(DataCenter dataCenter, TestInfo testInfo)
      throws MalformedURLException {
    setup(dataCenter, testInfo, TestType.EMULATOR);
    quitDriver();
    Awaitility.await()
        .atMost(Duration.ofSeconds(20))
        .pollInterval(Duration.ofSeconds(1))
        .pollInSameThread()
        .until(() -> "complete".equals(jobs.get().getJobDetails(sessionID.get()).status));
  }

  private void runSimulatorTest(DataCenter dataCenter, TestInfo testInfo)
      throws MalformedURLException {
    setup(dataCenter, testInfo, TestType.SIMULATOR);
    quitDriver();
    Awaitility.await()
        .atMost(Duration.ofSeconds(20))
        .pollInterval(Duration.ofSeconds(1))
        .pollInSameThread()
        .until(() -> "complete".equals(jobs.get().getJobDetails(sessionID.get()).status));
  }

  public enum TestType {
    EMULATOR,
    SIMULATOR,
    BROWSER
  }
}
