package com.saucelabs.saucerest.model.platform;

import java.util.List;

public class Platform {

    public String shortVersion;
    public String longName;
    public String apiName;
    public String longVersion;
    public String latestStableVersion;
    public String automationBackend;
    public String os;
    public List<Object> deprecatedBackendVersions = null;
    public String recommendedBackendVersion;
    public List<String> supportedBackendVersions = null;
    public String device;

    public Platform() {
    }

    public Platform(String shortVersion, String longName, String apiName, String longVersion, String latestStableVersion, String automationBackend, String os, List<Object> deprecatedBackendVersions, String recommendedBackendVersion, List<String> supportedBackendVersions, String device) {
        super();
        this.shortVersion = shortVersion;
        this.longName = longName;
        this.apiName = apiName;
        this.longVersion = longVersion;
        this.latestStableVersion = latestStableVersion;
        this.automationBackend = automationBackend;
        this.os = os;
        this.deprecatedBackendVersions = deprecatedBackendVersions;
        this.recommendedBackendVersion = recommendedBackendVersion;
        this.supportedBackendVersions = supportedBackendVersions;
        this.device = device;
    }
}