package com.saucelabs.saucerest.model.realdevices;

public class Entity {

    public Object assignedTunnelId;
    public String automationBackend;
    public String consolidatedStatus;
    public Long creationTime;
    public String deviceName;
    public String deviceType;
    public Long endTime;
    public String id;
    public Boolean manual;
    public String name;
    public String os;
    public String osVersion;
    public String ownerSauce;
    public Long startTime;
    public String status;
    public String testReportType;
    public Boolean hasCrashed;

    public Entity() {
    }

    public Entity(Object assignedTunnelId, String automationBackend, String consolidatedStatus, Long creationTime, String deviceName, String deviceType, Long endTime, String id, Boolean manual, String name, String os, String osVersion, String ownerSauce, Long startTime, String status, String testReportType, Boolean hasCrashed) {
        super();
        this.assignedTunnelId = assignedTunnelId;
        this.automationBackend = automationBackend;
        this.consolidatedStatus = consolidatedStatus;
        this.creationTime = creationTime;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.endTime = endTime;
        this.id = id;
        this.manual = manual;
        this.name = name;
        this.os = os;
        this.osVersion = osVersion;
        this.ownerSauce = ownerSauce;
        this.startTime = startTime;
        this.status = status;
        this.testReportType = testReportType;
        this.hasCrashed = hasCrashed;
    }
}