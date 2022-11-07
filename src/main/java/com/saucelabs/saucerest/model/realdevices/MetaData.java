package com.saucelabs.saucerest.model.realdevices;

import com.squareup.moshi.Json;

public class MetaData {

    @Json(name = "limit")
    public Integer limit;
    @Json(name = "moreAvailable")
    public Boolean moreAvailable;
    @Json(name = "offset")
    public Integer offset;
    @Json(name = "sortDirection")
    public String sortDirection;

    public MetaData() {
    }

    public MetaData(Integer limit, Boolean moreAvailable, Integer offset, String sortDirection) {
        super();
        this.limit = limit;
        this.moreAvailable = moreAvailable;
        this.offset = offset;
        this.sortDirection = sortDirection;
    }
}