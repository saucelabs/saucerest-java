package com.saucelabs.saucerest.model.storage;

public class Links {

    public String prev;
    public String next;
    public String self;

    public Links() {
    }

    public Links(String prev, String next, String self) {
        super();
        this.prev = prev;
        this.next = next;
        this.self = self;
    }
}