package com.saucelabs.saucerest.model.insights;

public enum Scope {
    ME("me"),
    ORGANIZATION("organization"),
    SINGLE("single");

    public final String value;

    Scope(String value) {
        this.value = value;
    }
}
