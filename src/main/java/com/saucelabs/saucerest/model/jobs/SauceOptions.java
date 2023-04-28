package com.saucelabs.saucerest.model.jobs;

import com.squareup.moshi.Json;

import java.util.List;

public class SauceOptions {

    @Json(name = "tags")
    public List<String> tags;
    @Json(name = "build")
    public String build;
    @Json(name = "name")
    public String name;

    public SauceOptions() {
    }

    public SauceOptions(List<String> tags, String build, String name) {
        super();
        this.tags = tags;
        this.build = build;
        this.name = name;
    }
}