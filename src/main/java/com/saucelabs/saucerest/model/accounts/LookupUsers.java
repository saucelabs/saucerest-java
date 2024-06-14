package com.saucelabs.saucerest.model.accounts;

import java.util.List;

public class LookupUsers {

    public Links links;
    public Integer count;
    public List<Result> results;

    /**
     * No args constructor for use in serialization
     */
    public LookupUsers() {
    }

    public LookupUsers(Links links, Integer count, List<Result> results) {
        super();
        this.links = links;
        this.count = count;
        this.results = results;
    }
}