package com.saucelabs.saucerest.model.storage;

public class Resigning {

    public Boolean imageInjection;
    public Boolean groupDirectory;
    public Boolean biometrics;
    public Boolean sysAlertsDelay;
    public Boolean networkCapture;

    private Resigning(Builder builder) {
        imageInjection = builder.imageInjection;
        groupDirectory = builder.groupDirectory;
        biometrics = builder.biometrics;
        sysAlertsDelay = builder.sysAlertsDelay;
        networkCapture = builder.networkCapture;
    }

    public static final class Builder {
        private Boolean imageInjection;
        private Boolean groupDirectory;
        private Boolean biometrics;
        private Boolean sysAlertsDelay;
        private Boolean networkCapture;

        public Builder setImageInjection(Boolean val) {
            imageInjection = val;
            return this;
        }

        public Builder setGroupDirectory(Boolean val) {
            groupDirectory = val;
            return this;
        }

        public Builder setBiometrics(Boolean val) {
            biometrics = val;
            return this;
        }

        public Builder setSysAlertsDelay(Boolean val) {
            sysAlertsDelay = val;
            return this;
        }

        public Builder setNetworkCapture(Boolean val) {
            networkCapture = val;
            return this;
        }

        public Resigning build() {
            return new Resigning(this);
        }
    }
}