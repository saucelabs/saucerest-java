package com.saucelabs.saucerest.integration;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.PlatformEndpoint;
import com.saucelabs.saucerest.model.platform.EndOfLifeAppiumVersions;
import com.saucelabs.saucerest.model.platform.SupportedPlatforms;
import com.saucelabs.saucerest.model.platform.TestStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;

public class PlatformEndpointTest {
    private final ThreadLocal<PlatformEndpoint> platform = new ThreadLocal<>();

    public void setup(DataCenter dataCenter) {
        platform.set(new SauceREST(dataCenter).getPlatform());
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_WEST", "EU_CENTRAL"}, mode = EnumSource.Mode.INCLUDE)
    public void getTestStatus(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        TestStatus testStatus = platform.get().getTestStatus();

        Assertions.assertNotNull(testStatus);
        Assertions.assertNotNull(testStatus.statusMessage);
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_WEST", "EU_CENTRAL"}, mode = EnumSource.Mode.INCLUDE)
    public void getAllSupportedPlatforms(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        SupportedPlatforms supportedPlatforms = platform.get().getSupportedPlatforms("all");

        Assertions.assertNotNull(supportedPlatforms);
        supportedPlatforms.platforms.forEach(platform -> Assertions.assertTrue((platform.automationBackend.equals("appium")) || (platform.automationBackend.equals("webdriver"))));
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_WEST", "EU_CENTRAL"}, mode = EnumSource.Mode.INCLUDE)
    public void getAppiumSupportedPlatforms(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        SupportedPlatforms supportedPlatforms = platform.get().getSupportedPlatforms("appium");

        Assertions.assertNotNull(supportedPlatforms);
        supportedPlatforms.platforms.forEach(platform -> Assertions.assertEquals("appium", platform.automationBackend));
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_WEST", "EU_CENTRAL"}, mode = EnumSource.Mode.INCLUDE)
    public void getWebdriverSupportedPlatforms(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        SupportedPlatforms supportedPlatforms = platform.get().getSupportedPlatforms("webdriver");

        Assertions.assertNotNull(supportedPlatforms);
        supportedPlatforms.platforms.forEach(platform -> Assertions.assertEquals("webdriver", platform.automationBackend));
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_WEST", "EU_CENTRAL"}, mode = EnumSource.Mode.INCLUDE)
    public void getEndOfLifeAppiumVersions(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        EndOfLifeAppiumVersions endOfLifeAppiumVersions = platform.get().getEndOfLifeAppiumVersions();

        Assertions.assertTrue(endOfLifeAppiumVersions.appiumVersionList.size() != 0);
    }
}