package com.saucelabs.saucerest.model.insights;

/** Only applicable when the {@link com.saucelabs.saucerest.JobSource} is RDC. */
public enum DeviceGroup {
    PRIVATE("private"),
    PUBLIC("public");

    public final String value;

    DeviceGroup(String value) {
        this.value = value;
    }
}
