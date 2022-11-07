package com.saucelabs.saucerest.integration;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.Platform;
import com.saucelabs.saucerest.model.platform.EndOfLifeAppiumVersions;
import com.saucelabs.saucerest.model.platform.SupportedPlatforms;
import com.saucelabs.saucerest.model.platform.TestStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;

public class PlatformTest {
    private final ThreadLocal<Platform> platform = new ThreadLocal<>();

    public void setup(com.saucelabs.saucerest.integration.DataCenter dataCenter) {
        platform.set(new SauceREST(DataCenter.fromString(dataCenter.toString())).getPlatform());
    }

    @ParameterizedTest
    @EnumSource(com.saucelabs.saucerest.integration.DataCenter.class)
    public void getTestStatus(com.saucelabs.saucerest.integration.DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        TestStatus testStatus = platform.get().getTestStatus();

        Assertions.assertNotNull(testStatus);
        Assertions.assertNotNull(testStatus.statusMessage);
    }

    @ParameterizedTest
    @EnumSource(com.saucelabs.saucerest.integration.DataCenter.class)
    public void getAllSupportedPlatforms(com.saucelabs.saucerest.integration.DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        SupportedPlatforms supportedPlatforms = platform.get().getSupportedPlatforms("all");

        Assertions.assertNotNull(supportedPlatforms);
        supportedPlatforms.platforms.forEach(platform -> Assertions.assertTrue((platform.automationBackend.equals("appium")) || (platform.automationBackend.equals("webdriver"))));
    }

    @ParameterizedTest
    @EnumSource(com.saucelabs.saucerest.integration.DataCenter.class)
    public void getAppiumSupportedPlatforms(com.saucelabs.saucerest.integration.DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        SupportedPlatforms supportedPlatforms = platform.get().getSupportedPlatforms("appium");

        Assertions.assertNotNull(supportedPlatforms);
        supportedPlatforms.platforms.forEach(platform -> Assertions.assertEquals("appium", platform.automationBackend));
    }

    @ParameterizedTest
    @EnumSource(com.saucelabs.saucerest.integration.DataCenter.class)
    public void getWebdriverSupportedPlatforms(com.saucelabs.saucerest.integration.DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        SupportedPlatforms supportedPlatforms = platform.get().getSupportedPlatforms("webdriver");

        Assertions.assertNotNull(supportedPlatforms);
        supportedPlatforms.platforms.forEach(platform -> Assertions.assertEquals("webdriver", platform.automationBackend));
    }

    @ParameterizedTest
    @EnumSource(com.saucelabs.saucerest.integration.DataCenter.class)
    public void getEndOfLifeAppiumVersions(com.saucelabs.saucerest.integration.DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        EndOfLifeAppiumVersions endOfLifeAppiumVersions = platform.get().getEndOfLifeAppiumVersions();

        Assertions.assertTrue(endOfLifeAppiumVersions.appiumVersionList.size() != 0);
    }
}
