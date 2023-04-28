package com.saucelabs.saucerest.integration;

import com.google.common.collect.ImmutableMap;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.JobsEndpoint;
import com.saucelabs.saucerest.api.RealDevicesEndpoint;
import com.saucelabs.saucerest.model.jobs.Job;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import okhttp3.*;
import org.awaitility.Awaitility;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.io.TempDir;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SauceRESTTest {
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(300, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .build();

    private static Logger logger = Logger.getLogger(SauceRESTTest.class.getName());

    @TempDir
    private Path tempDir;

    @Test
    public void cacheIdNoResetTest(TestInfo testInfo) throws MalformedURLException {
        MutableCapabilities capabilities = new MutableCapabilities();
        MutableCapabilities sauceCapabilities = new MutableCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "13");
        capabilities.setCapability("appium:deviceName", "Google Pixel.*");
        capabilities.setCapability("appium:app", "storage:filename=Android-MyDemoAppRN.apk");
        capabilities.setCapability("appium:automationName", "UiAutomator2");
        // noReset is set to true, so the app is not reinstalled on every test
        capabilities.setCapability("appium:noReset", true);

        sauceCapabilities.setCapability("name", testInfo.getDisplayName());
        sauceCapabilities.setCapability("username", System.getenv("SAUCE_USERNAME"));
        sauceCapabilities.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
        sauceCapabilities.setCapability("cacheId", "MyOwnUnique1234ID");

        capabilities.setCapability("sauce:options", sauceCapabilities);

        URL url = new URL("https://ondemand.eu-central-1.saucelabs.com:443/wd/hub");
        AndroidDriver driver = new AndroidDriver(url, capabilities);

        annotate(driver, "Click on backpack");
        driver.findElement(AppiumBy.xpath("(//android.view.ViewGroup[@content-desc=\"store item\"])[1]/android.view.ViewGroup[1]/android.widget.ImageView")).click();
        annotate(driver, "Add backpack to cart");
        driver.findElement(AppiumBy.accessibilityId("Add To Cart button")).click();
        annotate(driver, "Click on cart");
        driver.findElement(AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"cart badge\"]/android.widget.ImageView")).click();
        driver.getScreenshotAs(OutputType.FILE);

        // Now, lets close the session

        driver.quit();

        // Now, lets start a new session with the same cacheId

        AndroidDriver driver2 = new AndroidDriver(url, capabilities);
        annotate(driver2, "Click on cart");
        driver2.findElement(AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"cart badge\"]/android.widget.ImageView")).click();
        driver2.getScreenshotAs(OutputType.FILE);

        // Check the cart still has one item

        WebElement element = driver2.findElement(AppiumBy.xpath("//android.view.ViewGroup[@content-desc=\"cart badge\"]/android.widget.TextView"));
        assertEquals("1", element.getText());
        driver2.getScreenshotAs(OutputType.FILE);
    }

    @Test
    public void testRealDeviceTestAssetDownload() throws IOException {
        SauceREST sauceREST = new SauceREST(DataCenter.EU_CENTRAL);
        RealDevicesEndpoint realDevicesEndpoint = sauceREST.getRealDevices();
        JobsEndpoint jobsEndpoint = sauceREST.getJobs();

        Job jobSelenium = jobsEndpoint.getJobDetails("94d430c109a6498d958feac5334143ee");
        Job jobAppium = jobsEndpoint.getJobDetails("1fb557eceba743879a322b41bf9a1ff4");

        jobsEndpoint.downloadServerLog(jobSelenium.id, tempDir);
        jobsEndpoint.downloadServerLog(jobAppium.id, tempDir);

        assertAll(
                () -> Files.exists(tempDir.resolve("selenium-server.log")),
                () -> Files.exists(tempDir.resolve("appium-server.log"))
        );
    }

    @Test
    public void uninstallInstallInstrumentedApp(TestInfo testInfo) throws IOException, InterruptedException {
        MutableCapabilities capabilities = new MutableCapabilities();
        MutableCapabilities sauceCapabilities = new MutableCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "13");
        capabilities.setCapability("appium:deviceName", "Google Pixel.*");
        capabilities.setCapability("appium:app", "storage:filename=mda-1.0.17-20.apk");
        capabilities.setCapability("appium:automationName", "UiAutomator2");

        sauceCapabilities.setCapability("name", testInfo.getDisplayName());
        sauceCapabilities.setCapability("username", System.getenv("SAUCE_USERNAME"));
        sauceCapabilities.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));

        capabilities.setCapability("sauce:options", sauceCapabilities);

        URL url = new URL("https://ondemand.eu-central-1.saucelabs.com:443/wd/hub");
        AndroidDriver driver = new AndroidDriver(url, capabilities);
        String deviceContextId = driver.getCapabilities().getCapability("appium:deviceContextId").toString();
        String installAppURL = String.format("https://api.eu-central-1.saucelabs.com/v1/rdc/manual/sessions/%s/app-storage/installations", deviceContextId);
        String fileID = "96d14ad8-927a-4bda-92f5-97983522b1c3"; // file ID of the app to be installed (can be found in the app storage)
        int groupID = 799859; // group ID of the app to be installed (can be found in the app storage)
        String bundleID = "com.saucelabs.mydemoapp.android"; // bundle ID of the app to be installed (can be found in the app storage)

        Map<String, Object> body = ImmutableMap.of(
                "appStorageId", fileID,
                "groupId", groupID,
                "launch", true
        );

        // Uninstall initially installed app
        annotate(driver, "Uninstalling app");
        driver.removeApp(bundleID);

        // Wait until app is uninstalled
        annotate(driver, "Waiting until app is uninstalled");
        Awaitility.await()
                .atMost(30, TimeUnit.SECONDS)
                .pollInterval(5, TimeUnit.SECONDS)
                .until(() -> !driver.isAppInstalled(bundleID));
        annotate(driver, "App is uninstalled");

        // Ok, app is uninstalled, now install the app via API
        annotate(driver, "Installing app via API");
        Request request = createRequest(installAppURL, body, "POST");
        String installationID = "";

        try (Response response = makeRequest(request)) {
            logger.info(response.code() + " " + response.body().string());
            installationID = new JSONObject(response.body().string()).getString("id");
        } catch (Exception e) {
            // ignore
        }

        // Wait until app is installed
        annotate(driver, "Waiting until app is installed from API request");
        Awaitility.await()
                .atMost(30, TimeUnit.SECONDS)
                .pollInterval(5, TimeUnit.SECONDS)
                .until(() -> driver.isAppInstalled(bundleID));
        annotate(driver, "App is installed from API request");

        // App should be installed now. Let's start the app
        annotate(driver, "Starting app");
        driver.activateApp(bundleID);
        Thread.sleep(2000);
        driver.getScreenshotAs(OutputType.FILE);
        driver.getScreenshotAs(OutputType.FILE);
        driver.quit();
    }

    private String createBasicAuth(String username, String accessKey) {
        return Credentials.basic(username, accessKey);
    }

    private Request createRequest(String url, Map<String, Object> body, String httpMethod) {
        RequestBody requestBody = RequestBody.create(new JSONObject(body).toString(), MediaType.get("application/json"));

        Request.Builder chain = new Request.Builder()
                .url(url)
                .header("Authorization", createBasicAuth(System.getenv("SAUCE_USERNAME"), System.getenv("SAUCE_ACCESS_KEY")))
                .method(httpMethod, requestBody);

        logger.info("Request: " + chain.build());
        //logger.info("Request body: " + new JSONObject(body));
        return chain.build();
    }

    private Response makeRequest(Request request) throws IOException {
        Response response;
        try {
            response = CLIENT.newCall(request).execute();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error executing request", e);
            throw e;
        }

        if (!response.isSuccessful()) {
            logger.log(Level.WARNING, "Request {0} {1} failed with response code {2} and message {3}",
                    new Object[]{request.method(), request.url(), response.code(), response.message()});
        }

        return response;
    }

    protected void annotate(AppiumDriver driver, String text) {
        ((JavascriptExecutor) driver).executeScript("sauce:context=" + text);
    }
}