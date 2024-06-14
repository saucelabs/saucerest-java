package com.saucelabs.saucerest.model.jobs;

import java.util.List;

public class SauceOptions {

    public List<String> tags;
    public String build;
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