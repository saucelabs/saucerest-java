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

import static org.junit.jupiter.api.Assertions.*;

public class PlatformEndpointTest {
    private final ThreadLocal<PlatformEndpoint> platform = new ThreadLocal<>();

    public void setup(DataCenter dataCenter) {
        platform.set(new SauceREST(dataCenter).getPlatformEndpoint());
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_WEST", "EU_CENTRAL"}, mode = EnumSource.Mode.INCLUDE)
    public void getTestStatus(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        TestStatus testStatus = platform.get().getTestStatus();

        assertNotNull(testStatus);
        assertNotNull(testStatus.statusMessage);
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_WEST", "EU_CENTRAL"}, mode = EnumSource.Mode.INCLUDE)
    public void getAllSupportedPlatforms(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        SupportedPlatforms supportedPlatforms = platform.get().getSupportedPlatforms("all");

        assertNotNull(supportedPlatforms);
        supportedPlatforms.getPlatforms().forEach(platform -> assertTrue((platform.automationBackend.equals("appium")) || (platform.automationBackend.equals("webdriver"))));
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_WEST", "EU_CENTRAL"}, mode = EnumSource.Mode.INCLUDE)
    public void getAppiumSupportedPlatforms(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        SupportedPlatforms supportedPlatforms = platform.get().getSupportedPlatforms("appium");

        assertNotNull(supportedPlatforms);
        supportedPlatforms.getPlatforms().forEach(platform -> Assertions.assertEquals("appium", platform.automationBackend));
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_WEST", "EU_CENTRAL"}, mode = EnumSource.Mode.INCLUDE)
    public void getWebdriverSupportedPlatforms(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        SupportedPlatforms supportedPlatforms = platform.get().getSupportedPlatforms("webdriver");

        assertNotNull(supportedPlatforms);
        supportedPlatforms.getPlatforms().forEach(platform -> Assertions.assertEquals("webdriver", platform.automationBackend));
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_WEST", "EU_CENTRAL"}, mode = EnumSource.Mode.INCLUDE)
    public void getEndOfLifeAppiumVersions(DataCenter dataCenter) throws IOException {
        setup(dataCenter);

        EndOfLifeAppiumVersions endOfLifeAppiumVersions = platform.get().getEndOfLifeAppiumVersions();

        assertNotEquals(0, endOfLifeAppiumVersions.getAppiumVersionList().size());
    }
}