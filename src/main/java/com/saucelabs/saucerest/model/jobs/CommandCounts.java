package com.saucelabs.saucerest.model.jobs;

import com.squareup.moshi.Json;

public class CommandCounts {

    @Json(name = "All")
    public Integer all;
    @Json(name = "Error")
    public Integer error;

    public CommandCounts() {
    }

    public CommandCounts(Integer all, Integer error) {
        super();
        this.all = all;
        this.error = error;
    }
}