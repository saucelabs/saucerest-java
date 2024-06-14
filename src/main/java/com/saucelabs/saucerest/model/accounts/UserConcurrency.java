package com.saucelabs.saucerest.model.accounts;

public class UserConcurrency {

    public Float timestamp;
    public Concurrency concurrency;

    /**
     * No args constructor for use in serialization
     */
    public UserConcurrency() {
    }

    /**
     * @param timestamp
     * @param concurrency
     */
    public UserConcurrency(Float timestamp, Concurrency concurrency) {
        super();
        this.timestamp = timestamp;
        this.concurrency = concurrency;
    }
}