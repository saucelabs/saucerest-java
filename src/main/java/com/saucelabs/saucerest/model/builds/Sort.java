package com.saucelabs.saucerest.model.builds;

public enum Sort {
    asc("asc"),
    desc("desc");

    public final String value;

    Sort(String value) {
        this.value = value;
    }
}