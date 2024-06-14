package com.saucelabs.saucerest.model.realdevices;

public class Organization {

    public Integer current;
    public Integer maximum;

    /**
     * No args constructor for use in serialization
     */
    public Organization() {
    }

    /**
     * @param current
     * @param maximum
     */
    public Organization(Integer current, Integer maximum) {
        super();
        this.current = current;
        this.maximum = maximum;
    }
}