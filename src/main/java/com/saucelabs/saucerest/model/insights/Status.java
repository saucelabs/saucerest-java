package com.saucelabs.saucerest.model.insights;

/**
 * Test status values accepted by the v2 Insights endpoints (tests, test-cases, errors, trends).
 */
public enum Status {
    COMPLETE("complete"),
    ERROR("error"),
    PASSED("passed"),
    FAILED("failed");

    public final String value;

    Status(String value) {
        this.value = value;
    }
}
