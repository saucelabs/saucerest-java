package com.saucelabs.saucerest.model.sauceconnect;

public class Instance {

    public String instanceId;
    public String allocationPrefix;
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