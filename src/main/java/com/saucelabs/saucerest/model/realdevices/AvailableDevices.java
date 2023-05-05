package com.saucelabs.saucerest.model.realdevices;

import java.util.List;

public class AvailableDevices {
    private final List<String> availableDevicesList;

    public AvailableDevices(List<String> availableDevicesList) {
        super();
        this.availableDevicesList = availableDevicesList;
    }
}