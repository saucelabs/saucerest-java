package com.saucelabs.saucerest.model.storage;

import com.saucelabs.saucerest.model.AbstractModel;
import com.squareup.moshi.Json;

public class Instrumentation extends AbstractModel {

    @Json(name = "image_injection")
    public Boolean imageInjection;
    @Json(name = "bypass_screenshot_restriction")
    public Boolean bypassScreenshotRestriction;
    @Json(name = "biometrics")
    public Boolean biometrics;
    @Json(name = "network_capture")
    public Boolean networkCapture;

    private Instrumentation(Builder builder) {
        imageInjection = builder.imageInjection;
        bypassScreenshotRestriction = builder.bypassScreenshotRestriction;
        biometrics = builder.biometrics;
        networkCapture = builder.networkCapture;
    }

    public static final class Builder {
        private Boolean imageInjection;
        private Boolean bypassScreenshotRestriction;
        private Boolean biometrics;
        private Boolean networkCapture;

        public Builder() {
        }

        public Builder setImageInjection(Boolean val) {
            imageInjection = val;
            return this;
        }

        public Builder setBypassScreenshotRestriction(Boolean val) {
            bypassScreenshotRestriction = val;
            return this;
        }

        public Builder setBiometrics(Boolean val) {
            biometrics = val;
            return this;
        }

        public Builder setNetworkCapture(Boolean val) {
            networkCapture = val;
            return this;
        }

        public Instrumentation build() {
            return new Instrumentation(this);
        }
    }
}