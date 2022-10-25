package com.saucelabs.saucerest.integration;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.saucelabs.saucerest.Job;
import com.saucelabs.saucerest.JobVisibility;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.TestAsset;
import org.apache.commons.io.FileUtils;
import org.awaitility.Awaitility;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JobTest {
    private RemoteWebDriver driver;
    private Job job;

    @BeforeEach
    public void createDriver() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.setPlatformName("Windows 10");
        Map<String, Object> sauceOptions = ImmutableMap.of(
            "username", System.getenv("SAUCE_USERNAME"),
            "accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        options.setCapability("sauce:options", sauceOptions);

        URL url = new URL("https://ondemand.us-west-1.saucelabs.com/wd/hub");
        driver = new RemoteWebDriver(url, options);
        job = new SauceREST().getJob(driver.getSessionId());
    }

    @AfterEach
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void getDetails() throws IOException {
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

    @Test
    public void changeName() throws IOException {
        String newName = "Newly Changed Name";
        JSONObject response = job.changeName(newName);

        Assertions.assertEquals(newName, response.get("name"));
    }

    @Test
    public void changeBuild() throws IOException {
        String newName = "Newly Changed Build";
        JSONObject response = job.changeBuild(newName);

        Assertions.assertEquals(newName, response.get("build"));
    }

    @Test
    public void changeVisibility() throws IOException {
        JSONObject response = job.changeVisibility(JobVisibility.PRIVATE);

        Assertions.assertEquals(JobVisibility.PRIVATE.value, response.get("public"));
    }

    @Test
    public void changeResultsTrue() throws IOException {
        JSONObject response = job.changeResults(true);

        Assertions.assertEquals("passed", response.get("consolidated_status"));
    }

    @Test
    public void changeResultsFalse() throws IOException {
        JSONObject response = job.changeResults(false);

        Assertions.assertEquals("failed", response.get("consolidated_status"));
    }

    @Test
    public void passed() throws IOException {
        JSONObject response = job.passed();

        Assertions.assertEquals("passed", response.get("consolidated_status"));
    }

    @Test
    public void failed() throws IOException {
        JSONObject response = job.failed();

        Assertions.assertEquals("failed", response.get("consolidated_status"));
    }

    @Test
    public void addTags() throws IOException {
        List<String> tags = ImmutableList.of("tag1", "tag2", "tag3");
        JSONObject response = job.addTags(tags);

        Assertions.assertEquals(tags, response.toMap().get("tags"));
    }

    @Test
    public void addCustomData() throws IOException {
        Map<String, Object> data = ImmutableMap.of("key1", "value1", "key2", "value2");
        JSONObject response = job.addCustomData(data);

        Assertions.assertEquals(data, response.toMap().get("custom-data"));
    }

    @Test
    public void stop() throws IOException {
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

    @Test
    public void delete() throws IOException {
        job.stop();
        job.delete();
        driver = null;
        // Need to implement Job.list() to assert success
    }

    @Test
    public void listAssets() throws IOException {
        driver.get("https://www.saucedemo.com");
        job.stop();
        driver = null;

        JSONObject response = job.availableAssets();

        Assertions.assertFalse(response.toMap().values().isEmpty());
        response.toMap().values().stream()
            .map(asset -> asset instanceof ArrayList ? "screenshots.zip" : (String) asset)
            .forEach(asset -> Assertions.assertTrue(TestAsset.get(asset).isPresent()));
    }

    @Test
    public void downloadAsset() throws IOException {
        driver.get("https://www.saucedemo.com");
        job.stop();
        driver = null;

        String directory = "src/test/assets";
        FileUtils.forceMkdir(new File(directory));

        try {
            job.download(TestAsset.SCREENSHOTS, Paths.get(directory));
            job.download(TestAsset.VIDEO, Paths.get(directory));
            job.download(TestAsset.SELENIUM_LOG, Paths.get(directory));
            job.download(TestAsset.SAUCE_LOG, Paths.get(directory));

            Assertions.assertTrue(Files.exists(Paths.get(directory, "screenshots.zip")));
            Assertions.assertTrue(Files.exists(Paths.get(directory, "log.json")));
            Assertions.assertTrue(Files.exists(Paths.get(directory, "selenium-server.log")));
            Assertions.assertTrue(Files.exists(Paths.get(directory, "video.mp4")));
        } finally {
            FileUtils.cleanDirectory(new File("src/test/assets/"));
        }
    }

    @Test
    public void downloadCustomAsset() throws IOException {
        driver.get("https://www.saucedemo.com");
        job.stop();
        driver = null;

        String directory = "src/test/assets";
        FileUtils.forceMkdir(new File(directory));

        try {
            job.download(TestAsset.SELENIUM_LOG, Paths.get(directory, "customName.log"));

            Assertions.assertTrue(Files.exists(Paths.get(directory, "customName.log")));
        } finally {
            FileUtils.cleanDirectory(new File("src/test/assets/"));
        }
    }

    @Test
    public void downloadAllAssets() throws IOException {
        driver.get("https://www.saucedemo.com");
        job.stop();
        driver = null;

        String directory = "src/test/assets";
        FileUtils.forceMkdir(new File(directory));

        try {
            job.downloadAllAssets(Paths.get(directory));

            Assertions.assertTrue(Files.exists(Paths.get(directory, "screenshots.zip")));
            Assertions.assertTrue(Files.exists(Paths.get(directory, "log.json")));
            Assertions.assertTrue(Files.exists(Paths.get(directory, "selenium-server.log")));
            Assertions.assertTrue(Files.exists(Paths.get(directory, "video.mp4")));
        } finally {
            FileUtils.cleanDirectory(new File("src/test/assets/"));
        }
    }

    @Test
    public void downloadAndPrependAllAssets() throws IOException {
        driver.get("https://www.saucedemo.com");
        job.stop();

        String currentDefault = driver.getSessionId() + "_" +
            (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date()) + "_";
        driver = null;

        String directory = "src/test/assets";
        FileUtils.forceMkdir(new File(directory));

        try {
            job.downloadAllAssets(Paths.get(directory), currentDefault);

            Assertions.assertTrue(Files.exists(Paths.get(directory, currentDefault + "screenshots.zip")));
            Assertions.assertTrue(Files.exists(Paths.get(directory, currentDefault + "log.json")));
            Assertions.assertTrue(Files.exists(Paths.get(directory, currentDefault + "selenium-server.log")));
            Assertions.assertTrue(Files.exists(Paths.get(directory, currentDefault + "video.mp4")));
        } finally {
            FileUtils.cleanDirectory(new File("src/test/assets/"));
        }
    }

    @Test
    public void deleteAllAssets() throws IOException {
        driver.get("https://www.saucedemo.com");
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
