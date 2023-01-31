package com.saucelabs.saucerest.model.accounts;

import com.squareup.moshi.Json;

public class ResetAccessKeyForTeam {

    @Json(name = "id")
    public String id;
    @Json(name = "username")
    public String username;
    @Json(name = "access_key")
    public String accessKey;

    /**
     * No args constructor for use in serialization
     */
    public ResetAccessKeyForTeam() {
    }

    /**
     * @param accessKey
     * @param id
     * @param username
     */
    public ResetAccessKeyForTeam(String id, String username, String accessKey) {
        super();
        this.id = id;
        this.username = username;
        this.accessKey = accessKey;
    }
}