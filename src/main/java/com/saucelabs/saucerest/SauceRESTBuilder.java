package com.saucelabs.saucerest;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

public class SauceRESTBuilder {
    private String username;
    private String accessKey;
    private DataCenter dataCenter;
    private int maxDuration = 15;
    private int maxRetries = -1;
    private int delay = 1;
    private int maxDelay = 5;
    private int delayFactor = 2;
    private ChronoUnit chronoUnit = ChronoUnit.SECONDS;
    private List<Class<? extends Throwable>> throwableList = Collections.singletonList(SauceException.NotYetDone.class);

    public SauceRESTBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public SauceRESTBuilder setAccessKey(String accessKey) {
        this.accessKey = accessKey;
        return this;
    }

    public SauceRESTBuilder setDatacenter(DataCenter datacenter) {
        this.dataCenter = datacenter;
        return this;
    }

    public SauceRESTBuilder setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
        return this;
    }

    public SauceRESTBuilder setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
        return this;
    }

    public SauceRESTBuilder setDelay(int delay) {
        this.delay = delay;
        return this;
    }

    public SauceRESTBuilder setMaxDelay(int maxDelay) {
        this.maxDelay = maxDelay;
        return this;
    }

    public SauceRESTBuilder setDelayFactor(int delayFactor) {
        this.delayFactor = delayFactor;
        return this;
    }

    public SauceRESTBuilder setChronoUnit(ChronoUnit chronoUnit) {
        this.chronoUnit = chronoUnit;
        return this;
    }

    public SauceRESTBuilder handleException(List<Class<? extends Throwable>> exceptions) {
        this.throwableList = exceptions;
        return this;
    }

    public SauceREST build() {
        if (username == null || accessKey == null) {
            throw new IllegalStateException("Required values (username/accessKey) missing");
        }

        return new SauceREST(username, accessKey, dataCenter, maxDuration, maxRetries, delay, maxDelay, chronoUnit,
            delayFactor, throwableList);
    }
}
