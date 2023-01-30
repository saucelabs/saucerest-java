package com.saucelabs.saucerest.model.accounts;

import com.saucelabs.saucerest.model.AbstractModel;
import com.squareup.moshi.Json;

public class Settings extends AbstractModel {

    @Json(name = "live_only")
    public Boolean liveOnly;
    @Json(name = "real_devices")
    public Integer realDevices;
    @Json(name = "virtual_machines")
    public Integer virtualMachines;

    /**
     * No args constructor for use in serialization
     */
    public Settings() {
    }

    /**
     * @param realDevices
     * @param liveOnly
     * @param virtualMachines
     */
    public Settings(Boolean liveOnly, Integer realDevices, Integer virtualMachines) {
        super();
        this.liveOnly = liveOnly;
        this.realDevices = realDevices;
        this.virtualMachines = virtualMachines;
    }

    private Settings(Builder builder) {
        liveOnly = builder.liveOnly;
        realDevices = builder.realDevices;
        virtualMachines = builder.virtualMachines;
    }

    public static final class Builder {
        private Boolean liveOnly;
        private Integer realDevices;
        private Integer virtualMachines;

        public Builder() {
        }

        // Currently not supported by the API
//        public Builder setLiveOnly(Boolean val) {
//            liveOnly = val;
//            return this;
//        }
//
//        public Builder setRealDevices(Integer val) {
//            realDevices = val;
//            return this;
//        }

        public Builder setVirtualMachines(Integer val) {
            virtualMachines = val;
            return this;
        }

        public Settings build() {
            return new Settings(this);
        }
    }
}