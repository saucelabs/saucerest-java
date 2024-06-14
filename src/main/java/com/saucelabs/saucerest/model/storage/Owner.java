package com.saucelabs.saucerest.model.storage;

public class Owner {

    public String id;
    public String orgId;

    public Owner() {
    }

    public Owner(String id, String orgId) {
        super();
        this.id = id;
        this.orgId = orgId;
    }
}