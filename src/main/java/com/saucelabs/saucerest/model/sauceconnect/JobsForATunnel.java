package com.saucelabs.saucerest.model.sauceconnect;

public class JobsForATunnel {

    public String id;
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