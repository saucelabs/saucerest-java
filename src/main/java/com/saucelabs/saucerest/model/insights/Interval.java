package com.saucelabs.saucerest.model.insights;

public enum Interval {
    ONE_MINUTE("1m"),
    FIFTEEN_MINUTES("15m"),
    ONE_HOUR("1h"),
    SIX_HOURS("6h"),
    TWELVE_HOURS("12h"),
    ONE_DAY("1d"),
    SEVEN_DAYS("7d"),
    THIRTY_DAYS("30d");

    public final String value;

    Interval(String value) {
        this.value = value;
    }
}
