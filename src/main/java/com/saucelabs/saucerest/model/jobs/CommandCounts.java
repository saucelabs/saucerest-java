package com.saucelabs.saucerest.model.jobs;

public class CommandCounts {

    public Integer all;
    public Integer error;

    public CommandCounts() {
    }

    public CommandCounts(Integer all, Integer error) {
        super();
        this.all = all;
        this.error = error;
    }
}