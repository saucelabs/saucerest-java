package com.saucelabs.saucerest.model.storage;

public class Settings {

    public Proxy proxy;
    public Boolean audioCapture;
    public Boolean proxyEnabled;
    public String lang;
    public String orientation;
    public Boolean resigningEnabled;
    public Resigning resigning;
    public Instrumentation instrumentation;
    public Boolean setupDeviceLock;
    public Boolean instrumentationEnabled;

    private Settings(Builder builder) {
        proxy = builder.proxy;
        audioCapture = builder.audioCapture;
        proxyEnabled = builder.proxyEnabled;
        lang = builder.lang;
        orientation = builder.orientation;
        resigningEnabled = builder.resigningEnabled;
        resigning = builder.resigning;
        instrumentation = builder.instrumentation;
        setupDeviceLock = builder.setupDeviceLock;
        instrumentationEnabled = builder.instrumentationEnabled;
    }

    public static final class Builder {
        private Proxy proxy;
        private Boolean audioCapture;
        private Boolean proxyEnabled;
        private String lang;
        private String orientation;
        private Boolean resigningEnabled;
        private Resigning resigning;
        private Instrumentation instrumentation;
        private Boolean setupDeviceLock;
        private Boolean instrumentationEnabled;

        public Builder setProxy(Proxy val) {
            proxy = val;
            return this;
        }

        public Builder setAudioCapture(Boolean val) {
            audioCapture = val;
            return this;
        }

        public Builder setProxyEnabled(Boolean val) {
            proxyEnabled = val;
            return this;
        }

        public Builder setLang(String val) {
            lang = val;
            return this;
        }

        public Builder setOrientation(String val) {
            orientation = val;
            return this;
        }

        public Builder setResigningEnabled(Boolean val) {
            resigningEnabled = val;
            return this;
        }

        public Builder setResigning(Resigning val) {
            resigning = val;
            return this;
        }

        public Builder setInstrumentation(Instrumentation val) {
            instrumentation = val;
            return this;
        }

        public Builder setSetupDeviceLock(Boolean val) {
            setupDeviceLock = val;
            return this;
        }

        public Builder setInstrumentationEnabled(Boolean val) {
            instrumentationEnabled = val;
            return this;
        }

        public Settings build() {
            if (proxy != null) {
                proxyEnabled = true;
            }

            if (instrumentation != null) {
                instrumentationEnabled = true;
            }

            if (resigning != null) {
                resigningEnabled = true;
            }

            return new Settings(this);
        }
    }
}