package com.saucelabs.saucerest.model.insights;

import java.util.List;
import java.util.Map;

public class Tests {
    public List<TestExecution> items;
    public Integer total;
    public Map<String, Integer> statuses;
    public Integer maxDuration;
}
