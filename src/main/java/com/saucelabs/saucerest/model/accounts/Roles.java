package com.saucelabs.saucerest.model.accounts;

public enum Roles {
    ORGADMIN(1),
    TEAMADMIN(4),
    MEMBER(3);

    private final int value;

    Roles(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}