package com.saucelabs.saucerest.model.accounts;

public class SetTeam {

    public String id;
    public User user;
    public Team team;
    public String createdAt;
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