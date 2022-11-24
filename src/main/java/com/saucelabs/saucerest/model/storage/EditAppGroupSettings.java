package com.saucelabs.saucerest.model.storage;

import com.saucelabs.saucerest.ErrorExplainers;
import com.saucelabs.saucerest.SauceException;
import com.saucelabs.saucerest.model.AbstractModel;
import com.squareup.moshi.Json;

public class EditAppGroupSettings extends AbstractModel {

    @Json(name = "settings")
    public Settings settings;
    @Json(name = "kind")
    public String kind;
    @Json(name = "identifier")
    public String identifier;

    public EditAppGroupSettings() {
    }

    public EditAppGroupSettings(Settings settings, String kind, String identifier) {
        super();
        this.settings = settings;
        this.kind = kind;
        this.identifier = identifier;
    }

    public static class Builder {

        public Proxy proxy;
        public Boolean audioCapture;
        public Boolean proxyEnabled;
        public String lang;
        public Orientation orientation;
        public Boolean resigningEnabled;
        public Resigning resigning;
        public Instrumentation instrumentation;
        public Boolean setupDeviceLock;
        public Boolean instrumentationEnabled;

        private Platform platform;

        public enum Platform {
            IOS, ANDROID
        }

        public enum Orientation {
            PORTRAIT, LANDSCAPE
        }

        public Builder() {
            // nope
        }

        public Builder(Platform platform) {
            this.platform = platform;
        }

        public Builder setProxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        public Builder setAudioCapture(Boolean audioCapture) {
            this.audioCapture = audioCapture;
            return this;
        }

        public Builder setProxyEnabled(Boolean proxyEnabled) {
            this.proxyEnabled = proxyEnabled;
            return this;
        }

        public Builder setLang(String lang) {
            this.lang = lang;
            return this;
        }

        public Builder setOrientation(Orientation orientation) {
            this.orientation = orientation;
            return this;
        }

        public Builder setResigningEnabled(Boolean resigningEnabled) {
            this.resigningEnabled = resigningEnabled;
            return this;
        }

        public Builder setResigning(Resigning resigning) {
            if (platform.equals(Platform.IOS)) {
                this.resigning = resigning;
                // to use Resigning features resigning needs to be enabled
                setResigningEnabled(true);
                return this;
            } else {
                String errorReasons = ErrorExplainers.errorMessageBuilder("Resigning is for iOS apps only", ErrorExplainers.ResigningNotAllowed());
                throw new SauceException.ResigningNotAllowed(errorReasons);
            }
        }

        public Builder setInstrumentation(Instrumentation instrumentation) {
            if (platform.equals(Platform.ANDROID)) {
                this.instrumentation = instrumentation;
                // to use Instrumentation features instrumentation needs to be enabled
                setInstrumentationEnabled(true);
                return this;
            } else {
                String errorReasons = ErrorExplainers.errorMessageBuilder("Instrumentation is for Android apps only", ErrorExplainers.InstrumentationNotAllowed());
                throw new SauceException.InstrumentationNotAllowed(errorReasons);
            }
        }

        public Builder setSetupDeviceLock(Boolean setupDeviceLock) {
            if (platform.equals(Platform.ANDROID)) {
                this.setupDeviceLock = setupDeviceLock;
                // to use Instrumentation features instrumentation needs to be enabled
                setInstrumentationEnabled(true);
                return this;
            } else {
                String errorReasons = ErrorExplainers.errorMessageBuilder("PIN device lock is for Android only", ErrorExplainers.DeviceLockOnlyOnAndroid());
                throw new SauceException.DeviceLockOnlyOnAndroid(errorReasons);
            }
        }

        public Builder setInstrumentationEnabled(Boolean instrumentationEnabled) {
            this.instrumentationEnabled = instrumentationEnabled;
            return this;
        }

        public EditAppGroupSettings build() {
            // overwrite conflicting settings. if resigning or instrumentation features are used then always set the respective feature to true as well
            if (this.resigning != null) {
                this.resigningEnabled = true;
            }

            if (this.instrumentation != null) {
                this.instrumentationEnabled = true;
            }

            //Settings settings1 = new Settings(proxy, audioCapture, proxyEnabled, lang, orientation.toString(), resigningEnabled, resigning, instrumentation, setupDeviceLock, instrumentationEnabled);

            Settings settings1 = new Settings.Builder()
                .build();

            // TODO: create settings, kind and identifier objects
            //return new EditAppGroupSettings(settings1, build().kind, build().identifier);
            return new EditAppGroupSettings();
        }
    }
}