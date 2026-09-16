package com.saucelabs.saucerest.model.insights;

import java.util.List;

public class TestExecution {
    public String id;
    public String name;
    public String status;
    public String creationTime;
    public String modificationTime;
    public String error;
    public Boolean passed;
    public String browserNormalized;
    public String osNormalized;
    public String deviceName;
    public String deviceGroup;
    public String build;
    public String automationBackend;
    public Integer duration;
    public List<String> tags;
    public String owner;
    public String ancestor;
    public String userId;
    public String teamId;
    public String groupId;
    public String orgId;
    public String startTime;
    public String endTime;
    public String deletionTime;
    public Boolean isExpired;
}
