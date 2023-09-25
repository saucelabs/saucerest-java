package com.saucelabs.saucerest.model.jobs;

import com.saucelabs.saucerest.model.AbstractModel;
import com.squareup.moshi.Json;
import java.util.List;
import java.util.Map;

public class Job extends AbstractModel {
    @Json(name = "status")
    public String status;
    @Json(name = "base_config")
    public BaseConfig baseConfig;
    @Json(name = "command_counts")
    public CommandCounts commandCounts;
    @Json(name = "deletion_time")
    public Object deletionTime;
    @Json(name = "url")
    public Object url;
    @Json(name = "org_id")
    public String orgId;
    @Json(name = "creation_time")
    public Integer creationTime;
    @Json(name = "id")
    public String id;
    @Json(name = "team_id")
    public String teamId;
    @Json(name = "performance_enabled")
    public Object performanceEnabled;
    @Json(name = "assigned_tunnel_id")
    public Object assignedTunnelId;
    @Json(name = "container")
    public Boolean container;
    @Json(name = "group_id")
    public String groupId;
    @Json(name = "public")
    public String _public;
    @Json(name = "breakpointed")
    public Object breakpointed;
    @Json(name = "browser_short_version")
    public String browserShortVersion;
    @Json(name = "video_url")
    public String videoUrl;
    @Json(name = "custom-data")
    public Map<String, String> customData;
    @Json(name = "browser_version")
    public String browserVersion;
    @Json(name = "owner")
    public String owner;
    @Json(name = "automation_backend")
    public String automationBackend;
    @Json(name = "collects_automator_log")
    public Boolean collectsAutomatorLog;
    @Json(name = "record_screenshots")
    public Boolean recordScreenshots;
    @Json(name = "record_video")
    public Boolean recordVideo;
    @Json(name = "build")
    public Object build;
    @Json(name = "passed")
    public Boolean passed;
    @Json(name = "log_url")
    public String logUrl;
    @Json(name = "start_time")
    public Integer startTime;
    @Json(name = "proxied")
    public Boolean proxied;
    @Json(name = "modification_time")
    public Integer modificationTime;
    @Json(name = "name")
    public String name;
    @Json(name = "commands_not_successful")
    public Integer commandsNotSuccessful;
    @Json(name = "consolidated_status")
    public String consolidatedStatus;
    @Json(name = "selenium_version")
    public Object seleniumVersion;
    @Json(name = "manual")
    public Boolean manual;
    @Json(name = "end_time")
    public Integer endTime;
    @Json(name = "error")
    public Object error;
    @Json(name = "os")
    public String os;
    @Json(name = "browser")
    public String browser;
    @Json(name = "tags")
    public List<String> tags;
    @Json(name = "video_secret")
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