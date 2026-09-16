package com.saucelabs.saucerest.model.performance;

import java.util.Map;

public class MetricItem {
    public String jobId;
    public String jobOwner;
    public String jobNameHash;
    public Map<String, Double> metricData;
    public String pageUrl;
    public Integer orderIndex;
    public String jobCreationTime;
    public String loadId;
    public String loaderId;
    public String error;
}
