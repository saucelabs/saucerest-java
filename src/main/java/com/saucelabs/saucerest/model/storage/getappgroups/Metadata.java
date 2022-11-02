
package com.saucelabs.saucerest.model.storage.getappgroups;

import com.squareup.moshi.Json;

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

    /**
     * No args constructor for use in serialization
     */
    public Metadata() {
    }

    /**
     * @param identifier
     * @param isTestRunner
     * @param minSdk
     * @param testRunnerClass
     * @param name
     * @param icon
     * @param targetSdk
     * @param version
     * @param versionCode
     */
    public Metadata(String identifier, String name, String version, Boolean isTestRunner, String icon, Integer versionCode, Integer minSdk, Integer targetSdk, Object testRunnerClass) {
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
    }

}
