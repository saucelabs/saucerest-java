package com.saucelabs.saucerest.model.storage;

public class GetAppStorageGroupSettings {

    public Settings settings;
    public String kind;
    public String identifier;

    private GetAppStorageGroupSettings(Builder builder) {
        settings = builder.settings;
        kind = builder.kind;
        identifier = builder.identifier;
    }

    public static final class Builder {
        private Settings settings;
        private String kind;
        private String identifier;

        public Builder setSettings(Settings val) {
            settings = val;
            return this;
        }

        public Builder setKind(String val) {
            kind = val;
            return this;
        }

        public Builder setIdentifier(String val) {
            identifier = val;
            return this;
        }

        public GetAppStorageGroupSettings build() {
            return new GetAppStorageGroupSettings(this);
        }
    }
}