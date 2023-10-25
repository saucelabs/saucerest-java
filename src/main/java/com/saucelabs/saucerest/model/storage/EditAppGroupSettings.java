package com.saucelabs.saucerest.model.storage;

import com.saucelabs.saucerest.ErrorExplainers;
import com.saucelabs.saucerest.SauceException;
import com.saucelabs.saucerest.model.AbstractModel;
import com.squareup.moshi.Json;
import java.util.stream.Stream;

public class EditAppGroupSettings extends AbstractModel {

    @Json(name = "settings")
    public Settings settings;
    @Json(name = "kind")
    public String kind;
    @Json(name = "identifier")
    public String identifier;

    private EditAppGroupSettings(Builder builder) {
        settings = builder.settings;
    }

    public static final class Builder {
        private final Platform platform;
        private Settings settings;

        public Builder(Platform platform) {
            this.platform = platform;
        }

        public Builder setSettings(Settings val) {
            settings = val;
            return this;
        }

        public EditAppGroupSettings build() {
            if (platform.equals(Platform.IOS)) {
                if (settings.instrumentationEnabled != null || settings.instrumentation != null) {
                    throw new SauceException.InstrumentationNotAllowed(ErrorExplainers.InstrumentationNotAllowed());
                }

                if (settings.setupDeviceLock != null) {
                    throw new SauceException.DeviceLockOnlyOnAndroid(ErrorExplainers.DeviceLockOnlyOnAndroid());
                }
            }

            if (platform.equals(Platform.ANDROID)) {
                if (settings.resigningEnabled != null || settings.resigning != null) {
                    throw new SauceException.ResigningNotAllowed(ErrorExplainers.ResigningNotAllowed());
                }
            }

            return new EditAppGroupSettings(this);
        }

        public enum Platform {
            IOS, ANDROID, OTHER;

            public static Platform fromString(String platform) {
                return Stream.of(values()).filter(p -> p.name().equalsIgnoreCase(platform)).findFirst().orElse(OTHER);
            }
        }
    }
}