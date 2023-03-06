package com.saucelabs.saucerest.model.accounts;

import com.squareup.moshi.Json;

public class Current {

    @Json(name = "vms")
    public Integer vms;
    @Json(name = "rds")
    public Integer rds;
    @Json(name = "mac_vms")
    public Integer macVms;

    /**
     * No args constructor for use in serialization
     */
    public Current() {
    }

    /**
     * @param rds
     * @param macVms
     * @param vms
     */
    public Current(Integer vms, Integer rds, Integer macVms) {
        super();
        this.vms = vms;
        this.rds = rds;
        this.macVms = macVms;
    }
}