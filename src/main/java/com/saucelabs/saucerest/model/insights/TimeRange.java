package com.saucelabs.saucerest.model.insights;

/** A relative time window, measured backward from now, e.g. {@code new TimeRange(7, Unit.D)} for the last 7 days. */
public class TimeRange {
    private final int value;
    private final Unit unit;

    public TimeRange(int value, Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    @Override
    public String toString() {
        return String.format("%d%s", value, unit.value);
    }
}
