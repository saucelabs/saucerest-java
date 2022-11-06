package com.saucelabs.saucerest.model.storage;

import com.squareup.moshi.Json;

/**
 * 2 endpoints return ID as an Int instead of String.
 */
public class ItemInteger {

    @Json(name = "id")
    public Integer id;
    @Json(name = "name")
    public String name;
    @Json(name = "recent")
    public Recent recent;
    @Json(name = "count")
    public Integer count;
    @Json(name = "access")
    public Access access;
    @Json(name = "settings")
    public Settings settings;

    /**
     * No args constructor for use in serialization
     */
    public ItemInteger() {
    }

    /**
     * @param settings
     * @param access
     * @param name
     * @param count
     * @param id
     * @param recent
     */
    public ItemInteger(Integer id, String name, Recent recent, Integer count, Access access, Settings settings) {
        super();
        this.id = id;
        this.name = name;
        this.recent = recent;
        this.count = count;
        this.access = access;
        this.settings = settings;
    }
}