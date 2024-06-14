package com.saucelabs.saucerest.model.realdevices;

import java.util.List;

public class DeviceJob {

    public ApplicationSummary applicationSummary;
    public Object assignedTunnelId;
    public String deviceType;
    public String ownerSauce;
    public String automationBackend;
    public BaseConfig baseConfig;
    public String build;
    public Boolean collectsAutomatorLog;
    public String consolidatedStatus;
    public Long creationTime;
    public DeviceDescriptor deviceDescriptor;
    public Long endTime;
    public Object error;
    public String id;
    public String frameworkLogUrl;
    public String deviceLogUrl;
    public String requestsUrl;
    public Object testCasesUrl;
    public Object junitLogUrl;
    public Boolean manual;
    public Long modificationTime;
    public String name;
    public String os;
    public String osVersion;
    public String deviceName;
    public Boolean passed;
    public Boolean proxied;
    public Boolean recordScreenshots;
    public List<Object> screenshots = null;
    public Boolean recordVideo;
    public Long startTime;
    public String status;
    public List<Object> tags = null;
    public String videoUrl;
    public String remoteAppFileUrl;
    public String appiumSessionId;
    public Object deviceSessionId;
    public String client;
    public String networkLogUrl;
    public String testfairyLogUrl;
    public String testReportType;
    public String crashLogUrl;
    public Boolean usedCachedDevice;
    public String backtraceUrl;
    public String appiumVersion;

    public DeviceJob() {
    }

    public DeviceJob(ApplicationSummary applicationSummary, Object assignedTunnelId, String deviceType, String ownerSauce, String automationBackend, BaseConfig baseConfig, String build, Boolean collectsAutomatorLog, String consolidatedStatus, Long creationTime, DeviceDescriptor deviceDescriptor, Long endTime, Object error, String id, String frameworkLogUrl, String deviceLogUrl, String requestsUrl, Object testCasesUrl, Object junitLogUrl, Boolean manual, Long modificationTime, String name, String os, String osVersion, String deviceName, Boolean passed, Boolean proxied, Boolean recordScreenshots, List<Object> screenshots, Boolean recordVideo, Long startTime, String status, List<Object> tags, String videoUrl, String remoteAppFileUrl, String appiumSessionId, Object deviceSessionId, String client, String networkLogUrl, String testfairyLogUrl, String testReportType, String crashLogUrl, Boolean usedCachedDevice, String backtraceUrl, String appiumVersion) {
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
        this.backtraceUrl = backtraceUrl;
        this.appiumVersion = appiumVersion;
    }
}