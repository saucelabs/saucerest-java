package com.saucelabs.saucerest.model.accounts;

import com.squareup.moshi.Json;

public class Organization {

    @Json(name = "id")
    public String id;
    @Json(name = "settings")
    public Settings settings;
    @Json(name = "total_vm_concurrency")
    public Integer totalVmConcurrency;
    @Json(name = "name")
    public String name;
    @Json(name = "created_at")
    public String createdAt;
    @Json(name = "updated_at")
    public String updatedAt;
    @Json(name = "current")
    public Current current;
    @Json(name = "allowed")
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