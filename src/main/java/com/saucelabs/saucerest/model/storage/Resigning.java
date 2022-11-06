
package com.saucelabs.saucerest.model.storage;

import com.squareup.moshi.Json;

public class Resigning {

    @Json(name = "image_injection")
    public Boolean imageInjection;
    @Json(name = "group_directory")
    public Boolean groupDirectory;
    @Json(name = "biometrics")
    public Boolean biometrics;
    @Json(name = "sys_alerts_delay")
    public Boolean sysAlertsDelay;
    @Json(name = "network_capture")
    public Boolean networkCapture;

    /**
     * No args constructor for use in serialization
     */
    public Resigning() {
    }

    /**
     * @param biometrics
     * @param sysAlertsDelay
     * @param imageInjection
     * @param groupDirectory
     * @param networkCapture
     */
    public Resigning(Boolean imageInjection, Boolean groupDirectory, Boolean biometrics, Boolean sysAlertsDelay, Boolean networkCapture) {
        super();
        this.imageInjection = imageInjection;
        this.groupDirectory = groupDirectory;
        this.biometrics = biometrics;
        this.sysAlertsDelay = sysAlertsDelay;
        this.networkCapture = networkCapture;
    }
}