package com.saucelabs.saucerest.model.platform;

import com.squareup.moshi.Json;

public class TestStatus {

    @Json(name = "wait_time")
    public double waitTime;
    @Json(name = "service_operational")
    public boolean serviceOperational;
    @Json(name = "status_message")
    public String statusMessage;

    public TestStatus() {

    }

    public TestStatus(double waitTime, boolean serviceOperational, String statusMessage) {
        this.waitTime = waitTime;
        this.serviceOperational = serviceOperational;
        this.statusMessage = statusMessage;
    }
}
