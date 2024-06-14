package com.saucelabs.saucerest.model.realdevices;

public class MetaData {

    public Integer limit;
    public Boolean moreAvailable;
    public Integer offset;
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