package com.saucelabs.saucerest.model.accounts;

import com.squareup.moshi.Json;

public class UserConcurrency {

    @Json(name = "timestamp")
    public Float timestamp;
    @Json(name = "concurrency")
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