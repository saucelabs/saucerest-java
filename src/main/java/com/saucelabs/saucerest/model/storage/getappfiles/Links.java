package com.saucelabs.saucerest.model.storage.getappfiles;

import com.squareup.moshi.Json;

public class Links {

    @Json(name = "prev")
    public Object prev;
    @Json(name = "next")
    public Object next;
    @Json(name = "self")
    public String self;

    /**
     * No args constructor for use in serialization
     */
    public Links() {
    }

    /**
     * @param next
     * @param prev
     * @param self
     */
    public Links(Object prev, Object next, String self) {
        super();
        this.prev = prev;
        this.next = next;
        this.self = self;
    }

}