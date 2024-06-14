package com.saucelabs.saucerest.model.jobs;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

public class Job {
    public String status;
    public BaseConfig baseConfig;
    public CommandCounts commandCounts;
    public Object deletionTime;
    public Object url;
    public String orgId;
    public Integer creationTime;
    public String id;
    public String teamId;
    public Object performanceEnabled;
    public Object assignedTunnelId;
    public Boolean container;
    public String groupId;
    @SerializedName("public")
    public String _public;
    public Object breakpointed;
    public String browserShortVersion;
    public String videoUrl;
    @SerializedName("custom-data")
    public Map<String, String> customData;
    public String browserVersion;
    public String owner;
    public String automationBackend;
    public Boolean collectsAutomatorLog;
    public Boolean recordScreenshots;
    public Boolean recordVideo;
    public Object build;
    public Boolean passed;
    public String logUrl;
    public Integer startTime;
    public Boolean proxied;
    public Integer modificationTime;
    public String name;
    public Integer commandsNotSuccessful;
    public String consolidatedStatus;
    public Object seleniumVersion;
    public Boolean manual;
    public Integer endTime;
    public Object error;
    public String os;
    public String browser;
    public List<String> tags;
    public String videoSecret;

    public Job() {
    }

    public Job(String status, BaseConfig baseConfig, CommandCounts commandCounts, Object deletionTime, Object url, String orgId, Integer creationTime, String id, String teamId, Object performanceEnabled, Object assignedTunnelId, Boolean container, String groupId, String _public, Object breakpointed, String browserShortVersion, String videoUrl, Map<String, String> customData, String browserVersion, String owner, String automationBackend, Boolean collectsAutomatorLog, Boolean recordScreenshots, Boolean recordVideo, Object build, Boolean passed, String logUrl, Integer startTime, Boolean proxied, Integer modificationTime, String name, Integer commandsNotSuccessful, String consolidatedStatus, Object seleniumVersion, Boolean manual, Integer endTime, Object error, String os, String browser, List<String> tags, String videoSecret) {
        this.status = status;
        this.baseConfig = baseConfig;
        this.commandCounts = commandCounts;
        this.deletionTime = deletionTime;
        this.url = url;
        this.orgId = orgId;
        this.creationTime = creationTime;
        this.id = id;
        this.teamId = teamId;
        this.performanceEnabled = performanceEnabled;
        this.assignedTunnelId = assignedTunnelId;
        this.container = container;
        this.groupId = groupId;
        this._public = _public;
        this.breakpointed = breakpointed;
        this.browserShortVersion = browserShortVersion;
        this.videoUrl = videoUrl;
        this.customData = customData;
        this.browserVersion = browserVersion;
        this.owner = owner;
        this.automationBackend = automationBackend;
        this.collectsAutomatorLog = collectsAutomatorLog;
        this.recordScreenshots = recordScreenshots;
        this.recordVideo = recordVideo;
        this.build = build;
        this.passed = passed;
        this.logUrl = logUrl;
        this.startTime = startTime;
        this.proxied = proxied;
        this.modificationTime = modificationTime;
        this.name = name;
        this.commandsNotSuccessful = commandsNotSuccessful;
        this.consolidatedStatus = consolidatedStatus;
        this.seleniumVersion = seleniumVersion;
        this.manual = manual;
        this.endTime = endTime;
        this.error = error;
        this.os = os;
        this.browser = browser;
        this.tags = tags;
        this.videoSecret = videoSecret;
    }
}