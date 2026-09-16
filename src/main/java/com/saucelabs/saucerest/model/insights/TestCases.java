package com.saucelabs.saucerest.model.insights;

import java.util.List;
import java.util.Map;

public class TestCases {
    public List<TestCase> testCases;
    public Integer total;
    public Map<String, Integer> statuses;
    public Double avgRuntime;
}
