package com.saucelabs.saucerest.model.platform;

import java.util.List;

public class EndOfLifeAppiumVersions {

    private final List<AppiumVersion> appiumVersions;

    public List<AppiumVersion> getAppiumVersionList() {
        return appiumVersions;
    }

    public EndOfLifeAppiumVersions(List<AppiumVersion> appiumVersions) {
        this.appiumVersions = appiumVersions;
    }
}