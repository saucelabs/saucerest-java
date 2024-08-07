package com.saucelabs.saucerest.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.RealDevicesEndpoint;
import com.saucelabs.saucerest.model.realdevices.*;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.OutputType;

public class RealDevicesEndpointTest {
    private final ThreadLocal<RealDevicesEndpoint> realDevices = new ThreadLocal<>();
    @TempDir
    private Path tempDir;

    @BeforeAll
    public static void runRealDeviceTest() throws MalformedURLException {
        UiAutomator2Options uiAutomator2Options = new UiAutomator2Options();
        uiAutomator2Options.setPlatformVersion("^1[2-3].*");
        uiAutomator2Options.setDeviceName(".*");
        uiAutomator2Options.withBrowserName("Chrome");

        MutableCapabilities sauceCapabilities = new MutableCapabilities();
        sauceCapabilities.setCapability("name", "SauceREST Android Real Device Integration Test");

        uiAutomator2Options.setCapability("sauce:options", sauceCapabilities);
        URL euCentralSauceLabsUrl = new URL("https://" + System.getenv("SAUCE_USERNAME") + ":" + System.getenv("SAUCE_ACCESS_KEY") + "@ondemand.eu-central-1.saucelabs.com/wd/hub");
        URL usWestSauceLabsUrl = new URL("https://" + System.getenv("SAUCE_USERNAME") + ":" + System.getenv("SAUCE_ACCESS_KEY") + "@ondemand.us-west-1.saucelabs.com/wd/hub");

        AndroidDriver driverEU = new AndroidDriver(euCentralSauceLabsUrl, uiAutomator2Options);
        driverEU.get("https://saucedemo.com");
        driverEU.getScreenshotAs(OutputType.FILE);
        driverEU.quit();
        AndroidDriver driverUS = new AndroidDriver(usWestSauceLabsUrl, uiAutomator2Options);
        driverUS.get("https://saucedemo.com");
        driverUS.getScreenshotAs(OutputType.FILE);
        driverUS.quit();
    }

    public void setup(DataCenter dataCenter) {
        realDevices.set(new SauceREST(dataCenter).getRealDevicesEndpoint());
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getDevices(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        //Devices devices = realDevices.get().getDevices();
        List<Device> devices = realDevices.get().getDevices();

        assertFalse(devices.isEmpty());
        assertFalse(devices.get(0).id.isEmpty());
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getSpecificDevice(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        List<Device> devices = realDevices.get().getDevices();
        String deviceId = devices.get(0).id;
        Device device = realDevices.get().getSpecificDevice(deviceId);

        assertNotNull(device);
        assertEquals(device.id, deviceId);
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getAvailableDevices(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        AvailableDevices availableDevices = realDevices.get().getAvailableDevices();

        assertNotNull(availableDevices);
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getDeviceJobs(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        DeviceJobs deviceJobs = realDevices.get().getDeviceJobs();

        assertNotNull(deviceJobs);
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getDeviceJobsWithLimit_5(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        DeviceJobs deviceJobs = realDevices.get().getDeviceJobs(Map.of("limit", 5));

        assertNotNull(deviceJobs);
        assertEquals(5, deviceJobs.metaData.limit);
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getDeviceJobsWithOffset_5(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        DeviceJobs deviceJobs = realDevices.get().getDeviceJobs(Map.of("offset", 5));

        assertNotNull(deviceJobs);
        assertEquals(5, deviceJobs.metaData.offset);
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class)
    public void getDeviceJobsWithLimitAndOffset(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        DeviceJobs deviceJobs = realDevices.get().getDeviceJobs(Map.of("offset", 5, "limit", 6));

        assertNotNull(deviceJobs);
        assertEquals(5, deviceJobs.metaData.offset);
        assertEquals(6, deviceJobs.metaData.limit);
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getSpecificDeviceJob(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        DeviceJobs deviceJobs = realDevices.get().getDeviceJobs();
        DeviceJob deviceJob = realDevices.get().getSpecificDeviceJob(deviceJobs.entities.get(0).id);

        assertNotNull(deviceJob);
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getConcurrency(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        Concurrency concurrency = realDevices.get().getConcurrency();

        assertNotNull(concurrency);
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getTestAssetTest(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        DeviceJobs deviceJobs = realDevices.get().getDeviceJobs();
        DeviceJob deviceJob = realDevices.get().getSpecificDeviceJob(deviceJobs.entities.get(0).id);

        realDevices.get().downloadVideo(deviceJob.id, tempDir.toString());
        realDevices.get().downloadDeviceLog(deviceJob.id, tempDir.toString());
        realDevices.get().downloadCommandsLog(deviceJob.id, tempDir.toString());
        realDevices.get().downloadAppiumLog(deviceJob.id, tempDir.toString());
        realDevices.get().downloadScreenshots(deviceJob.id, tempDir.toString());
        //realDevices.get().downloadHARFile(deviceJob.id, tempDir.toString());
        //realDevices.get().downloadDeviceVitals(deviceJob.id, tempDir.toString());

        assertAll(
            () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), "video.mp4"))),
            () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), "device.log"))),
            () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), "commands.json"))),
            () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), "appium-server.log"))),
            () -> assertTrue(Files.exists(Paths.get(tempDir.toString(), "0.png")))
            //() -> assertTrue(Files.exists(Paths.get(tempDir.toString(), "network.har"))),
            //() -> assertTrue(Files.exists(Paths.get(tempDir.toString(), "insights.json")))
        );
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getAppiumServerVersionTest(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        DeviceJobs deviceJobs = realDevices.get().getDeviceJobs();
        DeviceJob deviceJob = realDevices.get().getSpecificDeviceJob(deviceJobs.entities.get(0).id);

        String appiumServerVersion = realDevices.get().getAppiumServerVersion(deviceJob.id);

        String regex = "^\\d+\\.\\d+\\.\\d+$";

        assertTrue(Pattern.matches(regex, appiumServerVersion));
    }
}
