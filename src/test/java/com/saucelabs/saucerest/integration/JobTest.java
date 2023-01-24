package com.saucelabs.saucerest.integration;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.saucelabs.saucerest.AfterBeforeParameterResolver;
import com.saucelabs.saucerest.JobVisibility;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.TestAsset;
import com.saucelabs.saucerest.api.Job;
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

import static com.saucelabs.saucerest.DataCenter.EU_CENTRAL;
import static com.saucelabs.saucerest.DataCenter.US_WEST;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(AfterBeforeParameterResolver.class)
public class JobTest {
    private ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();
    private Job job;
    @TempDir
    private Path tempDir;

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

        if (DataCenter.EU_CENTRAL == param) {
            job = new SauceREST(EU_CENTRAL).getJob(driver.get().getSessionId().toString());
        } else if (DataCenter.US_WEST == param) {
            job = new SauceREST(US_WEST).getJob(driver.get().getSessionId().toString());
        }
        // TODO: add this after API endpoints are supported in APAC
        /* else if (DataCenter.APAC_SOUTHEAST == param) {
            job = new SauceREST(APAC_SOUTHEAST).getJob(driver.get().getSessionId().toString());
        } */
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

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getDetails(DataCenter param, TestInfo testInfo) throws IOException {
        JSONObject response = job.getDetails();

        Map<String, Object> testDetails = response.toMap();

        assertAll(
            () -> assertEquals("googlechrome", testDetails.get("browser")),
            () -> assertEquals("Windows 10", testDetails.get("os")),
            () -> assertEquals("webdriver", testDetails.get("automation_backend")),
            () -> assertEquals("team", testDetails.get("public")),
            () -> assertTrue((Boolean) testDetails.get("record_screenshots")),
            () -> assertTrue((Boolean) testDetails.get("record_video")),
            () -> assertTrue(((List<String>) testDetails.get("tags")).isEmpty()),
            () -> assertNull(testDetails.get("error")),
            () -> assertNull(testDetails.get("selenium_version")),
            () -> assertNull(testDetails.get("name")),
            () -> assertNull(testDetails.get("assigned_tunnel_id")),
            () -> assertNull(testDetails.get("passed")),
            () -> assertNull(testDetails.get("custom-data"))
        );
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void changeName(DataCenter param, TestInfo testInfo) throws IOException {
        String newName = "Newly Changed Name";
        JSONObject response = job.changeName(newName);

        assertEquals(newName, response.get("name"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void changeBuild(DataCenter param, TestInfo testInfo) throws IOException {
        String newName = "Newly Changed Build";
        JSONObject response = job.changeBuild(newName);

        assertEquals(newName, response.get("build"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void changeVisibility(DataCenter param, TestInfo testInfo) throws IOException {
        JSONObject response = job.changeVisibility(JobVisibility.PRIVATE);

        assertEquals(JobVisibility.PRIVATE.value, response.get("public"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void changeResultsTrue(DataCenter param, TestInfo testInfo) throws IOException {
        JSONObject response = job.changeResults(true);

        assertEquals("passed", response.get("consolidated_status"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void changeResultsFalse(DataCenter param, TestInfo testInfo) throws IOException {
        JSONObject response = job.changeResults(false);

        assertEquals("failed", response.get("consolidated_status"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void passed(DataCenter param, TestInfo testInfo) throws IOException {
        JSONObject response = job.passed();

        assertEquals("passed", response.get("consolidated_status"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void failed(DataCenter param, TestInfo testInfo) throws IOException {
        JSONObject response = job.failed();

        assertEquals("failed", response.get("consolidated_status"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void addTags(DataCenter param, TestInfo testInfo) throws IOException {
        List<String> tags = ImmutableList.of("tag1", "tag2", "tag3");
        JSONObject response = job.addTags(tags);

        assertEquals(tags, response.toMap().get("tags"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void addCustomData(DataCenter param, TestInfo testInfo) throws IOException {
        Map<String, Object> data = ImmutableMap.of("key1", "value1", "key2", "value2");
        JSONObject response = job.addCustomData(data);

        assertEquals(data, response.toMap().get("custom-data"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void stop(DataCenter param, TestInfo testInfo) throws IOException {
        driver = null;
        JSONObject response = job.stop();

        assertEquals("in progress", response.get("status"));

        Assertions.assertDoesNotThrow(() ->
            Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .pollInterval(Duration.ofSeconds(1))
                .until(() -> "complete".equals(job.getDetails().get("status")))
        );
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
            .forEach(asset -> assertTrue(TestAsset.get(asset).isPresent()));
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

        assertAll(
            () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), "screenshots.zip"))),
            () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), "log.json"))),
            () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), "selenium-server.log"))),
            () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), "video.mp4")))
        );
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void downloadCustomAsset(DataCenter param, TestInfo testInfo) throws IOException {
        driver.get().get("https://www.saucedemo.com");
        job.stop();
        driver = null;

        job.download(TestAsset.SELENIUM_LOG, Paths.get(tempDir.toString(), "customName.log"));

        assertTrue(Files.exists(Paths.get(tempDir.toString(), "customName.log")));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void downloadAllAssets(DataCenter param, TestInfo testInfo) throws IOException {
        driver.get().get("https://www.saucedemo.com");
        job.stop();
        driver = null;

        job.downloadAllAssets(tempDir);

        assertAll(
            () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), "screenshots.zip"))),
            () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), "log.json"))),
            () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), "selenium-server.log"))),
            () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), "video.mp4")))
        );
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

        assertAll(
            () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), currentDefault + "screenshots.zip"))),
            () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), currentDefault + "log.json"))),
            () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), currentDefault + "selenium-server.log"))),
            () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), currentDefault + "video.mp4")))
        );
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void deleteAllAssets(DataCenter param, TestInfo testInfo) throws IOException {
        driver.get().get("https://www.saucedemo.com");
        job.stop();
        driver = null;

        Assertions.assertFalse(job.availableAssets().toMap().values().isEmpty());

        job.deleteAllAssets();

        Assertions.assertThrows(RuntimeException.class, () ->
            Awaitility.await()
                .atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofMillis(500))
                .until(() -> job.availableAssets() == null)
        );
    }

}
