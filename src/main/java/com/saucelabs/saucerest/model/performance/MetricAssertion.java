package com.saucelabs.saucerest.model.performance;

public class MetricAssertion {
    public Double baseline;
    public Double lowerBoundary;
    public Double upperBoundary;
    public Double realValue;
    public String jobId;
    public String datetime;
    public Integer orderIndex;
    public Outlier outlier;
}
