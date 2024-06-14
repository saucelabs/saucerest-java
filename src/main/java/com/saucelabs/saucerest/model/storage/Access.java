package com.saucelabs.saucerest.model.storage;

import java.util.List;

public class Access {

    public List<String> teamIds = null;
    public List<String> orgIds = null;

    public Access() {
    }

    public Access(List<String> teamIds, List<String> orgIds) {
        super();
        this.teamIds = teamIds;
        this.orgIds = orgIds;
    }
}