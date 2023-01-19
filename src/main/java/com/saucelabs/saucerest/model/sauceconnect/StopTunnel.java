package com.saucelabs.saucerest.model.sauceconnect;

import com.squareup.moshi.Json;

public class StopTunnel {

    @Json(name = "result")
    public Boolean result;
    @Json(name = "id")
    public String id;
    @Json(name = "jobs_running")
    public Integer jobsRunning;

    /**
     * No args constructor for use in serialization
     */
    public StopTunnel() {
    }

    /**
     * @param result
     * @param jobsRunning
     * @param id
     */
    public StopTunnel(Boolean result, String id, Integer jobsRunning) {
        super();
        this.result = result;
        this.id = id;
        this.jobsRunning = jobsRunning;
    }

}