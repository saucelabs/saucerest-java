
package com.saucelabs.saucerest.model.storage.getappgroups;

import com.saucelabs.saucerest.model.storage.Instrumentation;
import com.saucelabs.saucerest.model.storage.Proxy;
import com.saucelabs.saucerest.model.storage.Resigning;
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
    @Json(name = "setup_device_lock")
    public Boolean setupDeviceLock;
    @Json(name = "resigning_enabled")
    public Boolean resigningEnabled;
    @Json(name = "resigning")
    public Resigning resigning;

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
     * @param instrumentation
     * @param lang
     * @param resigningEnabled
     * @param resigning
     */
    public Settings(Proxy proxy, Boolean audioCapture, Boolean proxyEnabled, String lang, Object orientation, Boolean instrumentationEnabled, Instrumentation instrumentation, Boolean setupDeviceLock, Boolean resigningEnabled, Resigning resigning) {
        super();
        this.proxy = proxy;
        this.audioCapture = audioCapture;
        this.proxyEnabled = proxyEnabled;
        this.lang = lang;
        this.orientation = orientation;
        this.instrumentationEnabled = instrumentationEnabled;
        this.instrumentation = instrumentation;
        this.setupDeviceLock = setupDeviceLock;
        this.resigningEnabled = resigningEnabled;
        this.resigning = resigning;
    }

}
