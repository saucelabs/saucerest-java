package com.saucelabs.saucerest.model.realdevices;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class DeviceJobs {

    public List<Entity> entities;
    @SerializedName("metaData")
    public MetaData metaData;

    public DeviceJobs() {
    }

    public DeviceJobs(List<Entity> entities, MetaData metaData) {
        this.entities = entities;
        this.metaData = metaData;
    }
}