package com.saucelabs.saucerest.model.accounts;

import com.squareup.moshi.Json;

public class SetTeam {

    @Json(name = "id")
    public String id;
    @Json(name = "user")
    public User user;
    @Json(name = "team")
    public Team team;
    @Json(name = "created_at")
    public String createdAt;
    @Json(name = "updated_at")
    public String updatedAt;

    public SetTeam(String id, User user, Team team, String createdAt, String updatedAt) {
        this.id = id;
        this.user = user;
        this.team = team;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * No args constructor for use in serialization
     */
    public SetTeam() {
    }
}