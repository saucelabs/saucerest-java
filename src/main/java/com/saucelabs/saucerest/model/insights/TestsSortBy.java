package com.saucelabs.saucerest.model.insights;

public enum TestsSortBy {
    DURATION("duration"),
    CREATION_TIME("creation_time");

    public final String value;

    TestsSortBy(String value) {
        this.value = value;
    }
}
