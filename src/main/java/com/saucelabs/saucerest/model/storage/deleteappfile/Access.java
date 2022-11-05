
package com.saucelabs.saucerest.model.storage.deleteappfile;

import com.squareup.moshi.Json;

import java.util.List;

public class Access {

    @Json(name = "team_ids")
    public List<String> teamIds = null;
    @Json(name = "org_ids")
    public List<Object> orgIds = null;

    /**
     * No args constructor for use in serialization
     */
    public Access() {
    }

    /**
     * @param orgIds
     * @param teamIds
     */
    public Access(List<String> teamIds, List<Object> orgIds) {
        super();
        this.teamIds = teamIds;
        this.orgIds = orgIds;
    }

}
