package com.saucelabs.saucerest.model.realdevices;

import com.squareup.moshi.Json;

import java.util.List;

public class DeviceJob {

    @Json(name = "application_summary")
    public ApplicationSummary applicationSummary;
    @Json(name = "assigned_tunnel_id")
    public Object assignedTunnelId;
    @Json(name = "device_type")
    public String deviceType;
    @Json(name = "owner_sauce")
    public String ownerSauce;
    @Json(name = "automation_backend")
    public String automationBackend;
    @Json(name = "base_config")
    public BaseConfig baseConfig;
    @Json(name = "build")
    public String build;
    @Json(name = "collects_automator_log")
    public Boolean collectsAutomatorLog;
    @Json(name = "consolidated_status")
    public String consolidatedStatus;
    @Json(name = "creation_time")
    public Long creationTime;
    @Json(name = "device_descriptor")
    public DeviceDescriptor deviceDescriptor;
    @Json(name = "end_time")
    public Long endTime;
    @Json(name = "error")
    public Object error;
    @Json(name = "id")
    public String id;
    @Json(name = "framework_log_url")
    public String frameworkLogUrl;
    @Json(name = "device_log_url")
    public String deviceLogUrl;
    @Json(name = "requests_url")
    public String requestsUrl;
    @Json(name = "test_cases_url")
    public Object testCasesUrl;
    @Json(name = "junit_log_url")
    public Object junitLogUrl;
    @Json(name = "manual")
    public Boolean manual;
    @Json(name = "modification_time")
    public Long modificationTime;
    @Json(name = "name")
    public String name;
    @Json(name = "os")
    public String os;
    @Json(name = "os_version")
    public String osVersion;
    @Json(name = "device_name")
    public String deviceName;
    @Json(name = "passed")
    public Boolean passed;
    @Json(name = "proxied")
    public Boolean proxied;
    @Json(name = "record_screenshots")
    public Boolean recordScreenshots;
    @Json(name = "screenshots")
    public List<Object> screenshots = null;
    @Json(name = "record_video")
    public Boolean recordVideo;
    @Json(name = "start_time")
    public Long startTime;
    @Json(name = "status")
    public String status;
    @Json(name = "tags")
    public List<Object> tags = null;
    @Json(name = "video_url")
    public String videoUrl;
    @Json(name = "remote_app_file_url")
    public String remoteAppFileUrl;
    @Json(name = "appium_session_id")
    public String appiumSessionId;
    @Json(name = "device_session_id")
    public Object deviceSessionId;
    @Json(name = "client")
    public String client;
    @Json(name = "network_log_url")
    public String networkLogUrl;
    @Json(name = "testfairy_log_url")
    public String testfairyLogUrl;
    @Json(name = "test_report_type")
    public String testReportType;
    @Json(name = "crash_log_url")
    public String crashLogUrl;
    @Json(name = "used_cached_device")
    public Boolean usedCachedDevice;

    public DeviceJob() {
    }

    public DeviceJob(ApplicationSummary applicationSummary, Object assignedTunnelId, String deviceType, String ownerSauce, String automationBackend, BaseConfig baseConfig, String build, Boolean collectsAutomatorLog, String consolidatedStatus, Long creationTime, DeviceDescriptor deviceDescriptor, Long endTime, Object error, String id, String frameworkLogUrl, String deviceLogUrl, String requestsUrl, Object testCasesUrl, Object junitLogUrl, Boolean manual, Long modificationTime, String name, String os, String osVersion, String deviceName, Boolean passed, Boolean proxied, Boolean recordScreenshots, List<Object> screenshots, Boolean recordVideo, Long startTime, String status, List<Object> tags, String videoUrl, String remoteAppFileUrl, String appiumSessionId, Object deviceSessionId, String client, String networkLogUrl, String testfairyLogUrl, String testReportType, String crashLogUrl, Boolean usedCachedDevice) {
        super();
        this.applicationSummary = applicationSummary;
        this.assignedTunnelId = assignedTunnelId;
        this.deviceType = deviceType;
        this.ownerSauce = ownerSauce;
        this.automationBackend = automationBackend;
        this.baseConfig = baseConfig;
        this.build = build;
        this.collectsAutomatorLog = collectsAutomatorLog;
        this.consolidatedStatus = consolidatedStatus;
        this.creationTime = creationTime;
        this.deviceDescriptor = deviceDescriptor;
        this.endTime = endTime;
        this.error = error;
        this.id = id;
        this.frameworkLogUrl = frameworkLogUrl;
        this.deviceLogUrl = deviceLogUrl;
        this.requestsUrl = requestsUrl;
        this.testCasesUrl = testCasesUrl;
        this.junitLogUrl = junitLogUrl;
        this.manual = manual;
        this.modificationTime = modificationTime;
        this.name = name;
        this.os = os;
        this.osVersion = osVersion;
        this.deviceName = deviceName;
        this.passed = passed;
        this.proxied = proxied;
        this.recordScreenshots = recordScreenshots;
        this.screenshots = screenshots;
        this.recordVideo = recordVideo;
        this.startTime = startTime;
        this.status = status;
        this.tags = tags;
        this.videoUrl = videoUrl;
        this.remoteAppFileUrl = remoteAppFileUrl;
        this.appiumSessionId = appiumSessionId;
        this.deviceSessionId = deviceSessionId;
        this.client = client;
        this.networkLogUrl = networkLogUrl;
        this.testfairyLogUrl = testfairyLogUrl;
        this.testReportType = testReportType;
        this.crashLogUrl = crashLogUrl;
        this.usedCachedDevice = usedCachedDevice;
    }
}