package com.saucelabs.saucerest;

public enum TestAsset {
    SAUCE_LOG("log.json"),
    VIDEO("video.mp4"),
    SELENIUM_LOG("selenium-server.log"),
    AUTOMATOR_LOG("automator.log"),
    LOGCAT_LOG("logcat.log"),
    SYSLOG_LOG("ios-syslog.log"),
    HAR("network.har"),
    PERFORMANCE("performance.json"),
    CONSOLE_LOG("console.json"),
    SCREENSHOTS("screenshots.zip"),
    APPIUM_LOG("appium-server.log");

    public final String label;

    TestAsset(String label) {
        this.label = label;
    }
}
