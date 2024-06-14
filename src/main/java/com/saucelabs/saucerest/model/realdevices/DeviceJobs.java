package com.saucelabs.saucerest.model.realdevices;

import java.util.List;

public class DeviceJobs {

    public List<Entity> entities = null;
    public MetaData metaData;

    public DeviceJobs() {
    }

    public DeviceJobs(List<Entity> entities, MetaData metaData) {
        super();
        this.entities = entities;
        this.metaData = metaData;
    }
}