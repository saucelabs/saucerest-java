package com.saucelabs.saucerest.model.insights;

import java.util.Map;

public class TestCase {
    public String name;
    public Map<String, Integer> statuses;
    public Integer totalRuns;
    public Double completeRate;
    public Double errorRate;
    public Double failRate;
    public Double passRate;
    public Double avgDuration;
    public Double medianDuration;
    public Double totalDuration;
}
