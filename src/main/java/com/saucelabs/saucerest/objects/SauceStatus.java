package com.saucelabs.saucerest.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by gavinmogan on 2015-12-01.
 */
public class SauceStatus implements Serializable {
    @JsonProperty("wait_time")
    private double wait_time;

    public double getWaitTime() { return this.wait_time; }

    @JsonProperty("service_operational")
    private boolean service_operational;

    public boolean getServiceOperational() { return this.service_operational; }

    @JsonProperty("status_message")
    private String status_message;

    public String getStatusMessage() { return this.status_message; }
}
