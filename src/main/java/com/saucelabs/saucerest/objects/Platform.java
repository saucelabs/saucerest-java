package com.saucelabs.saucerest.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gavinmogan on 2015-11-28.
 */
public class Platform implements Serializable {
    @JsonProperty("deprecated_backend_versions")
    public List<String> deprecatedBackendVersions;

    @JsonProperty("short_version")
    public String shortVersion;

    @JsonProperty("long_version")
    public String longVersion;

    @JsonProperty("latest_stable_version")
    public String latestStableVersion;

    @JsonProperty("recommended_backend_version")
    public String recommendedBackendVersion;

    @JsonProperty("supported_backend_versions")
    public List<String> supportedBackendVersions;

    @JsonProperty("long_name")
    public String longName;

    @JsonProperty("api_name")
    public String apiName;

    @JsonProperty("device")
    public String device;

    @JsonProperty("automation_backend")
    public String automationBackend; // FIXME - should be an enum

    @JsonProperty("os")
    public String os;
}