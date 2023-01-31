
package com.saucelabs.saucerest.model.accounts;

import com.squareup.moshi.Json;

public class Role {

    @Json(name = "name")
    public String name;
    @Json(name = "role")
    public Integer role;

    /**
     * No args constructor for use in serialization
     */
    public Role() {
    }

    /**
     * @param role
     * @param name
     */
    public Role(String name, Integer role) {
        super();
        this.name = name;
        this.role = role;
    }
}