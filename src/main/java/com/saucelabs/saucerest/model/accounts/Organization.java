package com.saucelabs.saucerest.model.accounts;

public class Organization {

    public String id;
    public Settings settings;
    public Integer totalVmConcurrency;
    public String name;
    public String createdAt;
    public String updatedAt;
    public Current current;
    public Allowed allowed;

    /**
     * No args constructor for use in serialization
     */
    public Organization() {
    }

    public Organization(String id, Settings settings, Integer totalVmConcurrency, String name, String createdAt, String updatedAt, Current current, Allowed allowed) {
        super();
        this.id = id;
        this.settings = settings;
        this.totalVmConcurrency = totalVmConcurrency;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.current = current;
        this.allowed = allowed;
    }
}