
package com.saucelabs.saucerest.model.storage.getappgroups;

import com.squareup.moshi.Json;

public class Instrumentation {

    @Json(name = "image_injection")
    public Boolean imageInjection;
    @Json(name = "bypass_screenshot_restriction")
    public Boolean bypassScreenshotRestriction;
    @Json(name = "biometrics")
    public Boolean biometrics;
    @Json(name = "network_capture")
    public Boolean networkCapture;

    /**
     * No args constructor for use in serialization
     */
    public Instrumentation() {
    }

    /**
     * @param biometrics
     * @param bypassScreenshotRestriction
     * @param imageInjection
     * @param networkCapture
     */
    public Instrumentation(Boolean imageInjection, Boolean bypassScreenshotRestriction, Boolean biometrics, Boolean networkCapture) {
        super();
        this.imageInjection = imageInjection;
        this.bypassScreenshotRestriction = bypassScreenshotRestriction;
        this.biometrics = biometrics;
        this.networkCapture = networkCapture;
    }

}
