package com.saucelabs.saucerest.model.realdevices;

public class ApplicationSummary {

    public String appStorageId;
    public Integer groupId;
    public String filename;
    public String name;
    public String packageName;
    public String version;
    public String versionCode;
    public Object shortVersion;
    public Integer minSdkLevel;
    public Integer targetSdkLevel;
    public Object minOsVersion;
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