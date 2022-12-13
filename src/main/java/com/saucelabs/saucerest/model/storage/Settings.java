package com.saucelabs.saucerest.model.storage;

import com.saucelabs.saucerest.model.AbstractModel;
import com.squareup.moshi.Json;

public class Settings extends AbstractModel {

    @Json(name = "proxy")
    public Proxy proxy;
    @Json(name = "audio_capture")
    public Boolean audioCapture;
    @Json(name = "proxy_enabled")
    public Boolean proxyEnabled;
    @Json(name = "lang")
    public String lang;
    @Json(name = "orientation")
    public String orientation;
    @Json(name = "resigning_enabled")
    public Boolean resigningEnabled;
    @Json(name = "resigning")
    public Resigning resigning;
    @Json(name = "instrumentation")
    public Instrumentation instrumentation;
    @Json(name = "setup_device_lock")
    public Boolean setupDeviceLock;
    @Json(name = "instrumentation_enabled")
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

        public Builder() {
        }

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

//    public Settings() {
//    }
//
//    public Settings(Proxy proxy, Boolean audioCapture, Boolean proxyEnabled, String lang, String orientation, Boolean resigningEnabled, Resigning resigning, Instrumentation instrumentation, Boolean setupDeviceLock, Boolean instrumentationEnabled) {
//        super();
//        this.proxy = proxy;
//        this.audioCapture = audioCapture;
//        this.proxyEnabled = proxyEnabled;
//        this.lang = lang;
//        this.orientation = orientation;
//        this.resigningEnabled = resigningEnabled;
//        this.resigning = resigning;
//        this.instrumentation = instrumentation;
//        this.setupDeviceLock = setupDeviceLock;
//        this.instrumentationEnabled = instrumentationEnabled;
//    }
}