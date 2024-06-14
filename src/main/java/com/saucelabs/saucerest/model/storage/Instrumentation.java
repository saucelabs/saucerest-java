package com.saucelabs.saucerest.model.storage;

public class Instrumentation {

    public Boolean imageInjection;
    public Boolean bypassScreenshotRestriction;
    public Boolean biometrics;
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