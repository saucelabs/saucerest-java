package com.saucelabs.saucerest.model.insights;

public enum Sort {
    ASC("asc"),
    DESC("desc");

    public final String value;

    Sort(String value) {
        this.value = value;
    }
}
