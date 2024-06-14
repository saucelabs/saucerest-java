package com.saucelabs.saucerest.model.accounts;

public class Group {

    public String id;
    public String name;
    public Integer virtualMachines;
    public Integer realDevices;

    /**
     * No args constructor for use in serialization
     */
    public Group() {
    }

    /**
     * @param realDevices
     * @param name
     * @param id
     * @param virtualMachines
     */
    public Group(String id, String name, Integer virtualMachines, Integer realDevices) {
        super();
        this.id = id;
        this.name = name;
        this.virtualMachines = virtualMachines;
        this.realDevices = realDevices;
    }
}
