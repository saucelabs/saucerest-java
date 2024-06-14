package com.saucelabs.saucerest.model.accounts;

public class ResetAccessKeyForTeam {

    public String id;
    public String username;
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