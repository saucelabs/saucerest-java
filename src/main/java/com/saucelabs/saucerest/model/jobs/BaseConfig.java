package com.saucelabs.saucerest.model.jobs;

public class BaseConfig {

    public GoogChromeOptions googChromeOptions;
    public SauceOptions sauceOptions;
    public String browserName;
    public String platformName;
    public String browserVersion;

    public BaseConfig() {
    }

    public BaseConfig(GoogChromeOptions googChromeOptions, SauceOptions sauceOptions, String browserName, String platformName, String browserVersion) {
        super();
        this.googChromeOptions = googChromeOptions;
        this.sauceOptions = sauceOptions;
        this.browserName = browserName;
        this.platformName = platformName;
        this.browserVersion = browserVersion;
    }
}