package com.saucelabs.saucerest.model.accounts;

public class Allowed {

    public Integer vms;
    public Integer rds;
    public Integer macVms;

    /**
     * No args constructor for use in serialization
     */
    public Allowed() {
    }

    /**
     * @param rds
     * @param macVms
     * @param vms
     */
    public Allowed(Integer vms, Integer rds, Integer macVms) {
        super();
        this.vms = vms;
        this.rds = rds;
        this.macVms = macVms;
    }

}