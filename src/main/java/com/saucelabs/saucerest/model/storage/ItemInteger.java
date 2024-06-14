package com.saucelabs.saucerest.model.storage;

/**
 * 2 endpoints return ID as an Int instead of String.
 */
public class ItemInteger {

    public Integer id;
    public String name;
    public Recent recent;
    public Integer count;
    public Access access;
    public Settings settings;
    public String projectPath;

    public ItemInteger() {
    }

    public ItemInteger(Integer id, String name, Recent recent, Integer count, Access access, Settings settings, String projectPath) {
        super();
        this.id = id;
        this.name = name;
        this.recent = recent;
        this.count = count;
        this.access = access;
        this.settings = settings;
        this.projectPath = projectPath;
    }
}