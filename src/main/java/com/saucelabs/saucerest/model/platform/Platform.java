package com.saucelabs.saucerest.model.platform;

import com.squareup.moshi.Json;

import java.util.List;

public class Platform {

    @Json(name = "short_version")
    public String shortVersion;
    @Json(name = "long_name")
    public String longName;
    @Json(name = "api_name")
    public String apiName;
    @Json(name = "long_version")
    public String longVersion;
    @Json(name = "latest_stable_version")
    public String latestStableVersion;
    @Json(name = "automation_backend")
    public String automationBackend;
    @Json(name = "os")
    public String os;
    @Json(name = "deprecated_backend_versions")
    public List<Object> deprecatedBackendVersions = null;
    @Json(name = "recommended_backend_version")
    public String recommendedBackendVersion;
    @Json(name = "supported_backend_versions")
    public List<String> supportedBackendVersions = null;
    @Json(name = "device")
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