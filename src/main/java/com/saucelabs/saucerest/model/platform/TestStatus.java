package com.saucelabs.saucerest.model.platform;

public class TestStatus {

    public double waitTime;
    public boolean serviceOperational;
    public String statusMessage;

    public TestStatus() {

    }

    public TestStatus(double waitTime, boolean serviceOperational, String statusMessage) {
        this.waitTime = waitTime;
        this.serviceOperational = serviceOperational;
        this.statusMessage = statusMessage;
    }
}
