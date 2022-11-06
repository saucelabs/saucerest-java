
package com.saucelabs.saucerest.model.storage;

import com.squareup.moshi.Json;

public class Owner {

    @Json(name = "id")
    public String id;
    @Json(name = "org_id")
    public String orgId;

    /**
     * No args constructor for use in serialization
     */
    public Owner() {
    }

    /**
     * @param id
     * @param orgId
     */
    public Owner(String id, String orgId) {
        super();
        this.id = id;
        this.orgId = orgId;
    }

}
