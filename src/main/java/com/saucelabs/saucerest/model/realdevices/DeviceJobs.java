package com.saucelabs.saucerest.model.realdevices;

import com.squareup.moshi.Json;
import java.util.List;

public class DeviceJobs {

    @Json(name = "entities")
    public List<Entity> entities = null;
    @Json(name = "metaData")
    public MetaData metaData;

    public DeviceJobs() {
    }

    public DeviceJobs(List<Entity> entities, MetaData metaData) {
        super();
        this.entities = entities;
        this.metaData = metaData;
    }
}