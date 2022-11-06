package com.saucelabs.saucerest.model.storage;

import com.squareup.moshi.Json;

import java.util.List;

public class Access {

    @Json(name = "team_ids")
    public List<String> teamIds = null;
    @Json(name = "org_ids")
    public List<String> orgIds = null;

    public Access() {
    }

    public Access(List<String> teamIds, List<String> orgIds) {
        super();
        this.teamIds = teamIds;
        this.orgIds = orgIds;
    }
}