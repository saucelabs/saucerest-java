package com.saucelabs.saucerest.model.realdevices;

public class Concurrency {

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