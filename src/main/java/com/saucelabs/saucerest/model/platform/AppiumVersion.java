package com.saucelabs.saucerest.model.platform;

public class AppiumVersion {

    private final String version;
    private final Integer timestamp;

    public AppiumVersion(String version, Integer timestamp) {
        this.version = version;
        this.timestamp = timestamp;
    }
}