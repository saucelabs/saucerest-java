package com.saucelabs.saucerest.model.accounts;

import com.squareup.moshi.Json;

public class Links {

    @Json(name = "next")
    public Object next;
    @Json(name = "previous")
    public Object previous;
    @Json(name = "first")
    public String first;
    @Json(name = "last")
    public String last;

    /**
     * No args constructor for use in serialization
     */
    public Links() {
    }

    /**
     * @param next
     * @param previous
     * @param last
     * @param first
     */
    public Links(Object next, Object previous, String first, String last) {
        super();
        this.next = next;
        this.previous = previous;
        this.first = first;
        this.last = last;
    }

}
