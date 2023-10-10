package com.saucelabs.saucerest.model.accounts;

import com.squareup.moshi.Json;
import java.util.List;

public class UsersTeam {

    @Json(name = "links")
    public Links links;
    @Json(name = "count")
    public Integer count;
    @Json(name = "results")
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