package com.saucelabs.saucerest.model.storage.editappgroupsettings;

import com.squareup.moshi.Json;

public class Settings {

    @Json(name = "proxy")
    public Proxy proxy;
    @Json(name = "audio_capture")
    public Boolean audioCapture;
    @Json(name = "proxy_enabled")
    public Boolean proxyEnabled;
    @Json(name = "lang")
    public String lang;
    @Json(name = "orientation")
    public Object orientation;
    @Json(name = "instrumentation_enabled")
    public Boolean instrumentationEnabled;
    @Json(name = "instrumentation")
    public Instrumentation instrumentation;
    @Json(name = "resigning_enabled")
    public Boolean resigningEnabled;
    @Json(name = "resigning")
    public Resigning resigning;
    @Json(name = "setup_device_lock")
    public Boolean setupDeviceLock;

    /**
     * No args constructor for use in serialization
     */
    public Settings() {
    }

    /**
     * @param proxy
     * @param audioCapture
     * @param proxyEnabled
     * @param orientation
     * @param instrumentationEnabled
     * @param setupDeviceLock
     * @param resigningEnabled
     * @param resigning
     * @param instrumentation
     * @param lang
     */
    public Settings(Proxy proxy, Boolean audioCapture, Boolean proxyEnabled, String lang, Object orientation, Boolean instrumentationEnabled, Instrumentation instrumentation, Boolean resigningEnabled, Resigning resigning, Boolean setupDeviceLock) {
        super();
        this.proxy = proxy;
        this.audioCapture = audioCapture;
        this.proxyEnabled = proxyEnabled;
        this.lang = lang;
        this.orientation = orientation;
        this.instrumentationEnabled = instrumentationEnabled;
        this.instrumentation = instrumentation;
        this.resigningEnabled = resigningEnabled;
        this.resigning = resigning;
        this.setupDeviceLock = setupDeviceLock;
    }

}
