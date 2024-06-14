package com.saucelabs.saucerest.model.sauceconnect;

public class StopTunnel {

    public Boolean result;
    public String id;
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