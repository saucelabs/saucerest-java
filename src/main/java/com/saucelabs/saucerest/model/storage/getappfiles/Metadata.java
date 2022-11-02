
package com.saucelabs.saucerest.model.storage.getappfiles;

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
     * @param isTestRunner
     * @param shortVersion
     * @param minOs
     * @param name
     * @param icon
     * @param deviceFamily
     * @param version
     * @param targetOs
     * @param isSimulator
     * @param testRunnerPluginPath
     */
    public Metadata(String identifier, String name, String version, Boolean isTestRunner, String icon, String shortVersion, Boolean isSimulator, String minOs, String targetOs, Object testRunnerPluginPath, List<String> deviceFamily) {
        super();
        this.identifier = identifier;
        this.name = name;
        this.version = version;
        this.isTestRunner = isTestRunner;
        this.icon = icon;
        this.shortVersion = shortVersion;
        this.isSimulator = isSimulator;
        this.minOs = minOs;
        this.targetOs = targetOs;
        this.testRunnerPluginPath = testRunnerPluginPath;
        this.deviceFamily = deviceFamily;
    }

}
