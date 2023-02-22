package com.saucelabs.saucerest.model.realdevices;

import com.squareup.moshi.Json;

public class Concurrency {

    @Json(name = "organization")
    public Organization organization;

    /**
     * No args constructor for use in serialization
     */
    public Concurrency() {
    }

    /**
     * @param organization
     */
    public Concurrency(Organization organization) {
        super();
        this.organization = organization;
    }
}