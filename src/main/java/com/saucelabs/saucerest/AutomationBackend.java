package com.saucelabs.saucerest;

public enum AutomationBackend {
    APPIUM("appium"),
    WEBDRIVER("webdriver");

    public final String label;

    AutomationBackend(String label) {
        this.label = label;
    }
}
