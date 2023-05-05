package com.saucelabs.saucerest.model.realdevices;

import java.util.List;

public class Devices {
    public List<Device> getDeviceList() {
        return deviceList;
    }

    private final List<Device> deviceList;

    public Devices(List<Device> devices) {
        super();
        this.deviceList = devices;
    }
}