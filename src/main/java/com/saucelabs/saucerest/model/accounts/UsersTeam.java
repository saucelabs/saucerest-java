package com.saucelabs.saucerest.model.accounts;

import java.util.List;

public class UsersTeam {

    public Links links;
    public Integer count;
    public List<Result> results;

    /**
     * No args constructor for use in serialization
     */
    public UsersTeam() {
    }

    /**
     * @param count
     * @param links
     * @param results
     */
    public UsersTeam(Links links, Integer count, List<Result> results) {
        super();
        this.links = links;
        this.count = count;
        this.results = results;
    }
}