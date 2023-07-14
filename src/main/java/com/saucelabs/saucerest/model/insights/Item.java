package com.saucelabs.saucerest.model.insights;

import com.squareup.moshi.Json;

public class Item {

    @Json(name = "ancestor")
    public String ancestor;
    @Json(name = "browser")
    public String browser;
    @Json(name = "browser_normalized")
    public String browserNormalized;
    @Json(name = "build")
    public String build;
    @Json(name = "creation_time")
    public String creationTime;
    @Json(name = "details_url")
    public String detailsUrl;
    @Json(name = "duration")
    public Integer duration;
    @Json(name = "end_time")
    public String endTime;
    @Json(name = "error")
    public String error;
    @Json(name = "id")
    public String id;
    @Json(name = "name")
    public String name;
    @Json(name = "os")
    public String os;
    @Json(name = "os_normalized")
    public String osNormalized;
    @Json(name = "owner")
    public String owner;
    @Json(name = "start_time")
    public String startTime;
    @Json(name = "status")
    public String status;
}