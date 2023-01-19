package com.saucelabs.saucerest.model.sauceconnect;

import com.squareup.moshi.Json;

public class Instance {

    @Json(name = "instance_id")
    public String instanceId;
    @Json(name = "allocation_prefix")
    public String allocationPrefix;
    @Json(name = "status")
    public String status;

    /**
     * No args constructor for use in serialization
     */
    public Instance() {
    }

    /**
     * @param allocationPrefix
     * @param instanceId
     * @param status
     */
    public Instance(String instanceId, String allocationPrefix, String status) {
        super();
        this.instanceId = instanceId;
        this.allocationPrefix = allocationPrefix;
        this.status = status;
    }
}