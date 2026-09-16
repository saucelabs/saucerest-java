package com.saucelabs.saucerest.model.insights;

import java.util.List;
import java.util.Map;

public class TestTrends {
    public TrendsMeta meta;
    public List<TrendBucket> buckets;
    public Map<String, Map<String, Integer>> metrics;
}
