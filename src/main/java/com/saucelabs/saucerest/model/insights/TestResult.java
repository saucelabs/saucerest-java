package com.saucelabs.saucerest.model.insights;

import com.squareup.moshi.Json;

import java.util.List;

public class TestResult {
    @Json(name = "has_more")
    public Boolean hasMore;
    @Json(name = "items")
    public List<Item> items;
    @Json(name = "meta")
    public Meta meta;
}