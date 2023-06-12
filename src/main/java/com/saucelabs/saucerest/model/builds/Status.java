package com.saucelabs.saucerest.model.builds;

public enum Status {
    running("running"),
    error("error"),
    failed("failed"),
    complete("complete"),
    success("success");

    public final String value;

    Status(String value) {
        this.value = value;
    }
}