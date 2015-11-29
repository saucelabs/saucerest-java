package com.saucelabs.saucerest.objects;

import java.util.List;

public class JobList {
    private List<Job> jobs;
    private boolean truncated;

    final public List<Job> getJobs() {
        return jobs;
    }

    final public boolean getTruncated() {
        return truncated;
    }
}
