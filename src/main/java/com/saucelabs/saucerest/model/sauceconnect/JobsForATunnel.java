package com.saucelabs.saucerest.model.sauceconnect;

import com.squareup.moshi.Json;

public class JobsForATunnel {

    @Json(name = "id")
    public String id;
    @Json(name = "jobs_running")
    public Integer jobsRunning;

    /**
     * No args constructor for use in serialization
     */
    public JobsForATunnel() {
    }

    /**
     * @param jobsRunning
     * @param id
     */
    public JobsForATunnel(String id, Integer jobsRunning) {
        super();
        this.id = id;
        this.jobsRunning = jobsRunning;
    }
}