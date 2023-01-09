package com.saucelabs.saucerest.model.realdevices;

import com.squareup.moshi.Json;

public class Entity {

    @Json(name = "assigned_tunnel_id")
    public Object assignedTunnelId;
    @Json(name = "automation_backend")
    public String automationBackend;
    @Json(name = "consolidated_status")
    public String consolidatedStatus;
    @Json(name = "creation_time")
    public Long creationTime;
    @Json(name = "device_name")
    public String deviceName;
    @Json(name = "device_type")
    public String deviceType;
    @Json(name = "end_time")
    public Long endTime;
    @Json(name = "id")
    public String id;
    @Json(name = "manual")
    public Boolean manual;
    @Json(name = "name")
    public String name;
    @Json(name = "os")
    public String os;
    @Json(name = "os_version")
    public String osVersion;
    @Json(name = "owner_sauce")
    public String ownerSauce;
    @Json(name = "start_time")
    public Long startTime;
    @Json(name = "status")
    public String status;
    @Json(name = "test_report_type")
    public String testReportType;

    public Entity() {
    }

    public Entity(Object assignedTunnelId, String automationBackend, String consolidatedStatus, Long creationTime, String deviceName, String deviceType, Long endTime, String id, Boolean manual, String name, String os, String osVersion, String ownerSauce, Long startTime, String status, String testReportType) {
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
    }
}