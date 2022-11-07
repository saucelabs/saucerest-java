package com.saucelabs.saucerest.model.realdevices;

import com.squareup.moshi.Json;

public class ApplicationSummary {

    @Json(name = "appStorageId")
    public String appStorageId;
    @Json(name = "groupId")
    public Integer groupId;
    @Json(name = "filename")
    public String filename;
    @Json(name = "name")
    public String name;
    @Json(name = "packageName")
    public String packageName;
    @Json(name = "version")
    public String version;
    @Json(name = "versionCode")
    public String versionCode;
    @Json(name = "shortVersion")
    public Object shortVersion;
    @Json(name = "minSdkLevel")
    public Integer minSdkLevel;
    @Json(name = "targetSdkLevel")
    public Integer targetSdkLevel;
    @Json(name = "minOsVersion")
    public Object minOsVersion;
    @Json(name = "targetOsVersion")
    public Object targetOsVersion;

    public ApplicationSummary() {
    }

    public ApplicationSummary(String appStorageId, Integer groupId, String filename, String name, String packageName, String version, String versionCode, Object shortVersion, Integer minSdkLevel, Integer targetSdkLevel, Object minOsVersion, Object targetOsVersion) {
        super();
        this.appStorageId = appStorageId;
        this.groupId = groupId;
        this.filename = filename;
        this.name = name;
        this.packageName = packageName;
        this.version = version;
        this.versionCode = versionCode;
        this.shortVersion = shortVersion;
        this.minSdkLevel = minSdkLevel;
        this.targetSdkLevel = targetSdkLevel;
        this.minOsVersion = minOsVersion;
        this.targetOsVersion = targetOsVersion;
    }
}