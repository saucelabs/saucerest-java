package com.saucelabs.saucerest.model.builds;

import com.squareup.moshi.Json;

import java.util.List;

public class Builds {
    @Json(name = "builds")
    public List<Build> builds;

    /**
     * No args constructor for use in serialization
     */
    public Builds() {
    }

    /**
     * @param builds
     */
    public Builds(List<Build> builds) {
        super();
        this.builds = builds;
    }
}