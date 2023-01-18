package com.saucelabs.saucerest.integration;

import com.google.common.collect.ImmutableMap;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.RealDevices;
import com.saucelabs.saucerest.model.realdevices.*;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.CapabilityType;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class RealDevicesTest {
    private final ThreadLocal<RealDevices> realDevices = new ThreadLocal<>();

    /**
     * Use this instead of {@link com.saucelabs.saucerest.integration.DataCenter} because not all regions support
     * app files yet.
     */
    enum Region {
        EU_CENTRAL, US_WEST
    }

    @BeforeAll
    public static void runRealDeviceTest() throws MalformedURLException {
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "12");
        capabilities.setCapability("appium:deviceName", ".*");
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "Chrome");

        MutableCapabilities sauceCapabilities = new MutableCapabilities();
        sauceCapabilities.setCapability("name", "SauceREST Android Real Device Integration Test");

        capabilities.setCapability("sauce:options", sauceCapabilities);
        URL euCentralSauceLabsUrl = new URL("https://" + System.getenv("SAUCE_USERNAME") + ":" + System.getenv("SAUCE_ACCESS_KEY") + "@ondemand.eu-central-1.saucelabs.com/wd/hub");
        URL usWestSauceLabsUrl = new URL("https://" + System.getenv("SAUCE_USERNAME") + ":" + System.getenv("SAUCE_ACCESS_KEY") + "@ondemand.us-west-1.saucelabs.com/wd/hub");

        AndroidDriver driverEU = new AndroidDriver(euCentralSauceLabsUrl, capabilities);
        //driverEU.get("https://saucedemo.com");
        driverEU.quit();
        AndroidDriver driverUS = new AndroidDriver(usWestSauceLabsUrl, capabilities);
        //driverUS.get("https://saucedemo.com");
        driverUS.quit();
    }

    public void setup(Region region) {
        realDevices.set(new SauceREST(com.saucelabs.saucerest.DataCenter.fromString(region.toString())).getRealDevices());
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getDevices(Region region) throws IOException {
        setup(region);

        Devices devices = realDevices.get().getDevices();

        Assertions.assertNotNull(devices);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getSpecificDevice(Region region) throws IOException {
        setup(region);

        Devices devices = realDevices.get().getDevices();
        String deviceId = devices.deviceList.get(0).id;
        Device device = realDevices.get().getSpecificDevice(deviceId);

        Assertions.assertNotNull(device);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getAvailableDevices(Region region) throws IOException {
        setup(region);

        AvailableDevices availableDevices = realDevices.get().getAvailableDevices();

        Assertions.assertNotNull(availableDevices);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getDeviceJobs(Region region) throws IOException {
        setup(region);

        DeviceJobs deviceJobs = realDevices.get().getDeviceJobs();

        Assertions.assertNotNull(deviceJobs);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getDeviceJobsWithLimit_5(Region region) throws IOException {
        setup(region);

        DeviceJobs deviceJobs = realDevices.get().getDeviceJobs(ImmutableMap.of("limit", 5));

        Assertions.assertNotNull(deviceJobs);
        Assertions.assertEquals(5, deviceJobs.metaData.limit);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getDeviceJobsWithOffset_5(Region region) throws IOException {
        setup(region);

        DeviceJobs deviceJobs = realDevices.get().getDeviceJobs(ImmutableMap.of("offset", 5));

        Assertions.assertNotNull(deviceJobs);
        Assertions.assertEquals(5, deviceJobs.metaData.offset);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getDeviceJobsWithLimitAndOffset(Region region) throws IOException {
        setup(region);

        DeviceJobs deviceJobs = realDevices.get().getDeviceJobs(ImmutableMap.of("offset", 5, "limit", 6));

        Assertions.assertNotNull(deviceJobs);
        Assertions.assertEquals(5, deviceJobs.metaData.offset);
        Assertions.assertEquals(6, deviceJobs.metaData.limit);
    }

    @ParameterizedTest
    @EnumSource(Region.class)
    public void getSpecificDeviceJob(Region region) throws IOException {
        setup(region);

        DeviceJobs deviceJobs = realDevices.get().getDeviceJobs();
        DeviceJob deviceJob = realDevices.get().getSpecificDeviceJob(deviceJobs.entities.get(0).id);

        Assertions.assertNotNull(deviceJob);
    }
}