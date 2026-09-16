package com.saucelabs.saucerest.model.insights;

public enum TagFilterMode {
    AND("and"),
    OR("or");

    public final String value;

    TagFilterMode(String value) {
        this.value = value;
    }
}
