package com.saucelabs.saucerest.model.storage;

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

    public Instrumentation() {
    }

    public Instrumentation(Boolean imageInjection, Boolean bypassScreenshotRestriction, Boolean biometrics, Boolean networkCapture) {
        super();
        this.imageInjection = imageInjection;
        this.bypassScreenshotRestriction = bypassScreenshotRestriction;
        this.biometrics = biometrics;
        this.networkCapture = networkCapture;
    }
}