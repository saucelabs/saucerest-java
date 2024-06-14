package com.saucelabs.saucerest.model.accounts;

public class Concurrency {

    public Organization organization;
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