package com.saucelabs.saucerest.model.accounts;

import java.util.List;

public class LookupTeams {

    public Links links;
    public Integer count;
    public List<Result> results = null;

    /**
     * No args constructor for use in serialization
     */
    public LookupTeams() {
    }

    /**
     * @param count
     * @param links
     * @param results
     */
    public LookupTeams(Links links, Integer count, List<Result> results) {
        super();
        this.links = links;
        this.count = count;
        this.results = results;
    }

}
