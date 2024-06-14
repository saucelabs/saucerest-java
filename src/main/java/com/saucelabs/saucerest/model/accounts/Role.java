package com.saucelabs.saucerest.model.accounts;

public class Role {

    public String name;
    public Integer role;

    /**
     * No args constructor for use in serialization
     */
    public Role() {
    }

    public Role(String name, Integer role) {
        super();
        this.name = name;
        this.role = role;
    }
}