package com.saucelabs.saucerest.model.storage;

import java.util.List;

public class Metadata {

    public String identifier;
    public String name;
    public String version;
    public Boolean isTestRunner;
    public String icon;
    public String shortVersion;
    public Boolean isSimulator;
    public String minOs;
    public String targetOs;
    public Object testRunnerPluginPath;
    public List<String> deviceFamily = null;
    public Integer versionCode;
    public Integer minSdk;
    public Integer targetSdk;
    public Object testRunnerClass;
    public String iconHash;

    public Metadata() {
    }

    public Metadata(String identifier, String name, String version, Boolean isTestRunner, String icon, String shortVersion, Boolean isSimulator, String minOs, String targetOs, Object testRunnerPluginPath, List<String> deviceFamily, Integer versionCode, Integer minSdk, Integer targetSdk, Object testRunnerClass, String iconHash) {
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
        this.versionCode = versionCode;
        this.minSdk = minSdk;
        this.targetSdk = targetSdk;
        this.testRunnerClass = testRunnerClass;
        this.iconHash = iconHash;
    }
}