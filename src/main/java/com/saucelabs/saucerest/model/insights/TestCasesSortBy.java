package com.saucelabs.saucerest.model.insights;

public enum TestCasesSortBy {
    TOTAL_RUNS("total_runs"),
    NAME("name"),
    COMPLETE_COUNT("complete_count"),
    ERROR_COUNT("error_count"),
    FAIL_COUNT("fail_count"),
    PASS_COUNT("pass_count"),
    COMPLETE_RATE("complete_rate"),
    ERROR_RATE("error_rate"),
    FAILURE_RATE("failure_rate"),
    PASS_RATE("pass_rate"),
    AVG_DURATION("avg_duration"),
    MEDIAN_DURATION("median_duration"),
    TOTAL_DURATION("total_duration");

    public final String value;

    TestCasesSortBy(String value) {
        this.value = value;
    }
}
