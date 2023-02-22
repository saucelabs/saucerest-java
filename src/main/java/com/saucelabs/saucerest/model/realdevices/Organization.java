package com.saucelabs.saucerest.model.realdevices;

import com.squareup.moshi.Json;

public class Organization {

    @Json(name = "current")
    public Integer current;
    @Json(name = "maximum")
    public Integer maximum;

    /**
     * No args constructor for use in serialization
     */
    public Organization() {
    }

    /**
     * @param current
     * @param maximum
     */
    public Organization(Integer current, Integer maximum) {
        super();
        this.current = current;
        this.maximum = maximum;
    }
}