
package com.saucelabs.saucerest.model.accounts;


import com.squareup.moshi.Json;

public class Organization {

    @Json(name = "id")
    public String id;
    @Json(name = "name")
    public String name;

    /**
     * No args constructor for use in serialization
     */
    public Organization() {
    }

    /**
     * @param name
     * @param id
     */
    public Organization(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }
}