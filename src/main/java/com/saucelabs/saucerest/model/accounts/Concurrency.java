package com.saucelabs.saucerest.model.accounts;

import com.squareup.moshi.Json;

public class Concurrency {

    @Json(name = "organization")
    public Organization organization;
    @Json(name = "team")
    public Team team;

    /**
     * No args constructor for use in serialization
     */
    public Concurrency() {
    }

    /**
     * @param organization
     * @param team
     */
    public Concurrency(Organization organization, Team team) {
        super();
        this.organization = organization;
        this.team = team;
    }
}