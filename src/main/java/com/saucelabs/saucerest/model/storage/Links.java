
package com.saucelabs.saucerest.model.storage;

import com.squareup.moshi.Json;

public class Links {

    @Json(name = "prev")
    public String prev;
    @Json(name = "next")
    public String next;
    @Json(name = "self")
    public String self;

    /**
     * No args constructor for use in serialization
     *
     */
    public Links() {
    }

    /**
     * @param next
     * @param prev
     * @param self
     */
    public Links(String prev, String next, String self) {
        super();
        this.prev = prev;
        this.next = next;
        this.self = self;
    }

}
