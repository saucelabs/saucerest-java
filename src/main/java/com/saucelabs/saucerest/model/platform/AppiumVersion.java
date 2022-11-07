package com.saucelabs.saucerest.model.platform;

public class AppiumVersion {

    public String version;
    public Integer timestamp;

    public AppiumVersion(String version, Integer timestamp) {
        this.version = version;
        this.timestamp = timestamp;
    }
}
