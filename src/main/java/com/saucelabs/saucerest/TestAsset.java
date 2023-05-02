package com.saucelabs.saucerest;

import java.util.Arrays;
import java.util.Optional;

/**
 * The Sauce Labs test assets do not have consistent names, labels and JSON keys/values. There is a difference
 * between the displayed filename in the UI, the JSON key in a HTTP response and its value.
 * This enum aims to combine all of these into a single place.
 * The enums themselves are named after what makes the most sense and what describes the test asset the best.
 * The label is the displayed filename in the UI.
 * The jsonKey is the JSON key that is used in the HTTP response and is mapped against
 * {@link com.saucelabs.saucerest.model.realdevices.DeviceJob} and {@link com.saucelabs.saucerest.model.jobs.JobAssets}.
 */
public enum TestAsset {
    SAUCE_LOG("log.json", "sauce-log"),
    VIDEO("video.mp4", "video_url"),
    SELENIUM_LOG("selenium-server.log", "log_url"),
    AUTOMATOR_LOG("automator.log", "automator.log"),
    LOGCAT_LOG("logcat.log", "logcat.log"),
    SYSLOG_LOG("ios-syslog.log", "ios-syslog.log"),
    HAR("network.har", "network_log_url"),
    PERFORMANCE("performance.json", "performance.json"),
    CONSOLE_LOG("console.json", "console.json"),
    // no JSON key because it's from the /asset/screenshots.zip endpoint
    SCREENSHOTS("screenshots.zip", null),
    APPIUM_LOG("appium-server.log", "framework_log_url"),
    INSIGHTS_LOG("insights.json", "testfairy_log_url"),
    CRASH_LOG("crash.json", "crash_log_url"),
    DEVICE_LOG("device.log", "device_log_url"),
    COMMANDS_LOG("commands.json", "requests_url");

    public final String label;
    public final String jsonKey;

    TestAsset(String label, String jsonKey) {
        this.label = label;
        this.jsonKey = jsonKey;
    }

    public static Optional<TestAsset> get(String label) {
        return Arrays.stream(TestAsset.values())
                .filter(asset -> asset.label.equals(label))
                .findFirst();
    }
}