package com.saucelabs.saucerest.integration;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.saucelabs.saucerest.*;
import org.awaitility.Awaitility;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.saucelabs.saucerest.DataCenter.EU;
import static com.saucelabs.saucerest.DataCenter.US;

@ExtendWith(AfterBeforeParameterResolver.class)
public class JobTest {
    private ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();
    private Job job;
    @TempDir
    private Path tempDir;

    // Yes, duplicating instead of using DataCenter enum to restrict and control where these tests run.
    enum DataCenter {
        USWEST("https://ondemand.us-west-1.saucelabs.com/wd/hub"),
        EU("https://ondemand.eu-central-1.saucelabs.com/wd/hub");

        public final String label;

        DataCenter(String label) {
            this.label = label;
        }

    }

    public void createDriver(DataCenter param, TestInfo testInfo) throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        MutableCapabilities sauceOptions = new MutableCapabilities();

        sauceOptions.setCapability("username", System.getenv("SAUCE_USERNAME"));
        sauceOptions.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));

        if (testInfo != null) {
            sauceOptions.setCapability("name", testInfo.getTestMethod().get().getName());
        }

        options.setPlatformName("Windows 10");
        options.setCapability("sauce:options", sauceOptions);

        URL url = new URL(param.label);
        driver.set(new RemoteWebDriver(url, options));

        if (DataCenter.EU == param) {
            job = new SauceREST(EU).getJob(EU, driver.get().getSessionId().toString());
        } else if (DataCenter.USWEST == param) {
            job = new SauceREST(US).getJob(US, driver.get().getSessionId().toString());
        }
    }

    @BeforeEach
    public void setup(DataCenter dataCenter, TestInfo testInfo) throws MalformedURLException {
        // Ugly hack to allow testing for "null" as test name in the metadata part of the test result page
        if (testInfo.getTestMethod().get().getName().equalsIgnoreCase("getDetails")) {
            createDriver(dataCenter, null);
        } else {
            createDriver(dataCenter, testInfo);
        }
    }

    @AfterEach
    public void quitDriver() {
        if (driver != null) {
            driver.get().quit();
        }
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getDetails(DataCenter param, TestInfo testInfo) throws IOException {
        JSONObject response = job.getDetails();

        Map<String, Object> testDetails = response.toMap();

        Assertions.assertEquals("googlechrome", testDetails.get("browser"));
        Assertions.assertEquals("Windows 10", testDetails.get("os"));
        Assertions.assertEquals("webdriver", testDetails.get("automation_backend"));
        Assertions.assertEquals("team", testDetails.get("public"));
        Assertions.assertTrue((Boolean) testDetails.get("record_screenshots"));
        Assertions.assertTrue((Boolean) testDetails.get("record_video"));
        Assertions.assertTrue(((List<String>) testDetails.get("tags")).isEmpty());
        Assertions.assertNull(testDetails.get("error"));
        Assertions.assertNull(testDetails.get("selenium_version"));
        Assertions.assertNull(testDetails.get("name"));
        Assertions.assertNull(testDetails.get("assigned_tunnel_id"));
        Assertions.assertNull(testDetails.get("passed"));
        Assertions.assertNull(testDetails.get("custom-data"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void changeName(DataCenter param, TestInfo testInfo) throws IOException {
        String newName = "Newly Changed Name";
        JSONObject response = job.changeName(newName);

        Assertions.assertEquals(newName, response.get("name"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void changeBuild(DataCenter param, TestInfo testInfo) throws IOException {
        String newName = "Newly Changed Build";
        JSONObject response = job.changeBuild(newName);

        Assertions.assertEquals(newName, response.get("build"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void changeVisibility(DataCenter param, TestInfo testInfo) throws IOException {
        JSONObject response = job.changeVisibility(JobVisibility.PRIVATE);

        Assertions.assertEquals(JobVisibility.PRIVATE.value, response.get("public"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void changeResultsTrue(DataCenter param, TestInfo testInfo) throws IOException {
        JSONObject response = job.changeResults(true);

        Assertions.assertEquals("passed", response.get("consolidated_status"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void changeResultsFalse(DataCenter param, TestInfo testInfo) throws IOException {
        JSONObject response = job.changeResults(false);

        Assertions.assertEquals("failed", response.get("consolidated_status"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void passed(DataCenter param, TestInfo testInfo) throws IOException {
        JSONObject response = job.passed();

        Assertions.assertEquals("passed", response.get("consolidated_status"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void failed(DataCenter param, TestInfo testInfo) throws IOException {
        JSONObject response = job.failed();

        Assertions.assertEquals("failed", response.get("consolidated_status"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void addTags(DataCenter param, TestInfo testInfo) throws IOException {
        List<String> tags = ImmutableList.of("tag1", "tag2", "tag3");
        JSONObject response = job.addTags(tags);

        Assertions.assertEquals(tags, response.toMap().get("tags"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void addCustomData(DataCenter param, TestInfo testInfo) throws IOException {
        Map<String, Object> data = ImmutableMap.of("key1", "value1", "key2", "value2");
        JSONObject response = job.addCustomData(data);

        Assertions.assertEquals(data, response.toMap().get("custom-data"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void stop(DataCenter param, TestInfo testInfo) throws IOException {
        driver = null;
        JSONObject response = job.stop();

        Assertions.assertEquals("in progress", response.get("status"));

        Assertions.assertDoesNotThrow(() -> {
            Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> "complete".equals(job.getDetails().get("status")));
        });
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void delete(DataCenter param, TestInfo testInfo) throws IOException {
        job.stop();
        job.delete();
        driver = null;
        // Need to implement Job.list() to assert success
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void listAssets(DataCenter param, TestInfo testInfo) throws IOException {
        driver.get().get("https://www.saucedemo.com");
        job.stop();
        driver = null;

        JSONObject response = job.availableAssets();

        Assertions.assertFalse(response.toMap().values().isEmpty());
        response.toMap().values().stream()
            .map(asset -> asset instanceof ArrayList ? "screenshots.zip" : (String) asset)
            .forEach(asset -> Assertions.assertTrue(TestAsset.get(asset).isPresent()));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void downloadAsset(DataCenter param, TestInfo testInfo) throws IOException {
        driver.get().get("https://www.saucedemo.com");
        job.stop();
        driver = null;

        job.download(TestAsset.SCREENSHOTS, Paths.get(tempDir.toString()));
        job.download(TestAsset.VIDEO, Paths.get(tempDir.toString()));
        job.download(TestAsset.SELENIUM_LOG, Paths.get(tempDir.toString()));
        job.download(TestAsset.SAUCE_LOG, Paths.get(tempDir.toString()));

        Assertions.assertTrue(Files.exists(Paths.get(tempDir.toString(), "screenshots.zip")));
        Assertions.assertTrue(Files.exists(Paths.get(tempDir.toString(), "log.json")));
        Assertions.assertTrue(Files.exists(Paths.get(tempDir.toString(), "selenium-server.log")));
        Assertions.assertTrue(Files.exists(Paths.get(tempDir.toString(), "video.mp4")));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void downloadCustomAsset(DataCenter param, TestInfo testInfo) throws IOException {
        driver.get().get("https://www.saucedemo.com");
        job.stop();
        driver = null;

        job.download(TestAsset.SELENIUM_LOG, Paths.get(tempDir.toString(), "customName.log"));

        Assertions.assertTrue(Files.exists(Paths.get(tempDir.toString(), "customName.log")));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void downloadAllAssets(DataCenter param, TestInfo testInfo) throws IOException {
        driver.get().get("https://www.saucedemo.com");
        job.stop();
        driver = null;

        job.downloadAllAssets(tempDir);

        Assertions.assertTrue(Files.exists(Paths.get(tempDir.toString(), "screenshots.zip")));
        Assertions.assertTrue(Files.exists(Paths.get(tempDir.toString(), "log.json")));
        Assertions.assertTrue(Files.exists(Paths.get(tempDir.toString(), "selenium-server.log")));
        Assertions.assertTrue(Files.exists(Paths.get(tempDir.toString(), "video.mp4")));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void downloadAndPrependAllAssets(DataCenter param, TestInfo testInfo) throws IOException {
        driver.get().get("https://www.saucedemo.com");
        job.stop();

        String currentDefault = driver.get().getSessionId() + "_" +
            (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date()) + "_";
        driver = null;

        job.downloadAllAssets(tempDir, currentDefault);

        Assertions.assertTrue(Files.exists(Paths.get(tempDir.toString(), currentDefault + "screenshots.zip")));
        Assertions.assertTrue(Files.exists(Paths.get(tempDir.toString(), currentDefault + "log.json")));
        Assertions.assertTrue(Files.exists(Paths.get(tempDir.toString(), currentDefault + "selenium-server.log")));
        Assertions.assertTrue(Files.exists(Paths.get(tempDir.toString(), currentDefault + "video.mp4")));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void deleteAllAssets(DataCenter param, TestInfo testInfo) throws IOException {
        driver.get().get("https://www.saucedemo.com");
        job.stop();
        driver = null;

        Assertions.assertFalse(job.availableAssets().toMap().values().isEmpty());

        job.deleteAllAssets();

        Assertions.assertThrows(RuntimeException.class, () -> {
            Awaitility.await()
                .atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofMillis(500))
                .until(() -> job.availableAssets() == null);
        });
    }

}
