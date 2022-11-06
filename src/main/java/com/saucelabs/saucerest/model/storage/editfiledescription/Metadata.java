
package com.saucelabs.saucerest.model.storage.editfiledescription;

import com.squareup.moshi.Json;

import java.util.List;

public class Metadata {

    @Json(name = "identifier")
    public String identifier;
    @Json(name = "name")
    public String name;
    @Json(name = "version")
    public String version;
    @Json(name = "is_test_runner")
    public Boolean isTestRunner;
    @Json(name = "icon")
    public String icon;
    @Json(name = "version_code")
    public Integer versionCode;
    @Json(name = "min_sdk")
    public Integer minSdk;
    @Json(name = "target_sdk")
    public Integer targetSdk;
    @Json(name = "test_runner_class")
    public Object testRunnerClass;
    @Json(name = "short_version")
    public String shortVersion;
    @Json(name = "is_simulator")
    public Boolean isSimulator;
    @Json(name = "min_os")
    public String minOs;
    @Json(name = "target_os")
    public String targetOs;
    @Json(name = "test_runner_plugin_path")
    public Object testRunnerPluginPath;
    @Json(name = "device_family")
    public List<String> deviceFamily = null;

    /**
     * No args constructor for use in serialization
     */
    public Metadata() {
    }

    /**
     * @param identifier
     * @param shortVersion
     * @param minOs
     * @param icon
     * @param targetSdk
     * @param deviceFamily
     * @param version
     * @param versionCode
     * @param testRunnerPluginPath
     * @param isTestRunner
     * @param minSdk
     * @param testRunnerClass
     * @param name
     * @param targetOs
     * @param isSimulator
     */
    public Metadata(String identifier, String name, String version, Boolean isTestRunner, String icon, Integer versionCode, Integer minSdk, Integer targetSdk, Object testRunnerClass, String shortVersion, Boolean isSimulator, String minOs, String targetOs, Object testRunnerPluginPath, List<String> deviceFamily) {
        super();
        this.identifier = identifier;
        this.name = name;
        this.version = version;
        this.isTestRunner = isTestRunner;
        this.icon = icon;
        this.versionCode = versionCode;
        this.minSdk = minSdk;
        this.targetSdk = targetSdk;
        this.testRunnerClass = testRunnerClass;
        this.shortVersion = shortVersion;
        this.isSimulator = isSimulator;
        this.minOs = minOs;
        this.targetOs = targetOs;
        this.testRunnerPluginPath = testRunnerPluginPath;
        this.deviceFamily = deviceFamily;
    }

}