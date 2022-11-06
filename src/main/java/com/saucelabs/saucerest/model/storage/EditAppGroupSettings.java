package com.saucelabs.saucerest.model.storage;

import com.squareup.moshi.Json;

public class EditAppGroupSettings {

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
}