package com.saucelabs.saucerest.model.jobs;

import com.squareup.moshi.Json;

import java.util.List;

public class GoogChromeOptions {

    @Json(name = "args")
    public List<String> args;
    @Json(name = "extensions")
    public List<Object> extensions;

    public GoogChromeOptions() {
    }

    public GoogChromeOptions(List<String> args, List<Object> extensions) {
        super();
        this.args = args;
        this.extensions = extensions;
    }
}