package com.saucelabs.saucerest.model.insights;

public enum Unit {
    D("d"),
    H("h"),
    M("m"),
    S("s");

    public final String value;

    Unit(String value) {
        this.value = value;
    }
}
