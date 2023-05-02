package com.saucelabs.saucerest.model.jobs;

import com.squareup.moshi.Json;

public class BaseConfig {

    @Json(name = "goog:chromeOptions")
    public GoogChromeOptions googChromeOptions;
    @Json(name = "sauce:options")
    public SauceOptions sauceOptions;
    @Json(name = "browserName")
    public String browserName;
    @Json(name = "platformName")
    public String platformName;
    @Json(name = "browserVersion")
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