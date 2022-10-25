package com.saucelabs.saucerest;

public enum JobVisibility {
    PUBLIC("public"),
    PUBLIC_RESTRICTED("public restricted"),
    SHARE("share"),
    TEAM("team"),
    PRIVATE("private");

    public final String value;

    JobVisibility(String value) {
        this.value = value;
    }
}
