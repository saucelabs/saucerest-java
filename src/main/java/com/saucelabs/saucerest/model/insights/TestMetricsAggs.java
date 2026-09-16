package com.saucelabs.saucerest.model.insights;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class TestMetricsAggs {
    public Integer count;

    @SerializedName("fastestRun")
    public Item fastestRun;

    @SerializedName("slowestRun")
    public Item slowestRun;

    public Map<String, Integer> statuses;

    @SerializedName("totalQueueTime")
    public Double totalQueueTime;

    @SerializedName("totalRunTime")
    public Double totalRunTime;
}
