package com.saucelabs.saucerest;

public enum JobSource {
    RDC("rdc"),
    VDC("vdc");

    public final String value;

    JobSource(String value) {
        this.value = value;
    }
}