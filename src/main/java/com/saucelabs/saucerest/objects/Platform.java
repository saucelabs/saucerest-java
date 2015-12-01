package com.saucelabs.saucerest.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gavinmogan on 2015-11-28.
 */
public class Platform implements Serializable, Comparable<Platform> {
    private static HashMap<String, String> osNamesMap = new HashMap<String, String>();

    static {
        osNamesMap.put("Windows 2015", "Windows 10");
        osNamesMap.put("Windows 2012 R2", "Windows 8.1");
        osNamesMap.put("Windows 2012", "Windows 8");
        osNamesMap.put("Windows 2008", "Windows 7");
        osNamesMap.put("Windows 2003", "Windows XP");
        osNamesMap.put("Mac 10.11", "OS X El Capitan");
        osNamesMap.put("Mac 10.10", "OS X Yosemite");
        osNamesMap.put("Mac 10.9", "OS X Mavericks");
        osNamesMap.put("Mac 10.8", "OS X Mountain Lion");
    }

    @JsonProperty("deprecated_backend_versions")
    private List<String> deprecatedBackendVersions;

    @JsonProperty("short_version")
    private String shortVersion;

    @JsonProperty("long_version")
    private String longVersion;

    @JsonProperty("latest_stable_version")
    private String latestStableVersion;

    @JsonProperty("recommended_backend_version")
    private String recommendedBackendVersion;

    @JsonProperty("supported_backend_versions")
    private List<String> supportedBackendVersions;

    @JsonProperty("long_name")
    private String longName;

    @JsonProperty("api_name")
    private String apiName;

    @JsonProperty("device")
    private String device;

    @JsonProperty("automation_backend")
    private String automationBackend; // FIXME - should be an enum

    @JsonProperty("os")
    private String os;

    public int compareTo(Platform platform) {
        return String.CASE_INSENSITIVE_ORDER.compare(getLabel(), platform.getLabel());
    }

    public String getOs() {
        if (this.device != null) { return this.device; }
        return this.os;
    }

    public String getOsName() {
        String name = osNamesMap.get(this.os);
        return name != null ? name : this.os;
    }

    public String getApiName() {
        return this.apiName;
    }

    public String getLongName() {
        return this.longName;
    }

    public String getBrowserKey() {

        String browserKey = os + apiName + shortVersion;
        //replace any spaces with _s
        browserKey=browserKey.replaceAll(" ","_");
        //replace any . with _
        browserKey=browserKey.replaceAll("\\.","_");

        return browserKey;
    }

    public String getLabel() {
        return getOsName() + " " + longName + " " + shortVersion;
    }


    public List<String> getDeprecatedBackendVersions() {
        return deprecatedBackendVersions;
    }

    public String getLongVersion() {
        return longVersion;
    }

    public String getLatestStableVersion() {
        return latestStableVersion;
    }

    public String getRecommendedBackendVersion() {
        return recommendedBackendVersion;
    }

    public List<String> getSupportedBackendVersions() {
        return supportedBackendVersions;
    }

    public String getAutomationBackend() {
        return automationBackend;
    }
}