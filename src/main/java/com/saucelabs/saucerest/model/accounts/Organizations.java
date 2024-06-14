package com.saucelabs.saucerest.model.accounts;

import java.util.List;

public class Organizations {

    public Links links;
    public Integer count;
    public List<Result> results;

    /**
     * No args constructor for use in serialization
     */
    public Organizations() {
    }

    public Organizations(Links links, Integer count, List<Result> results) {
        super();
        this.links = links;
        this.count = count;
        this.results = results;
    }
}