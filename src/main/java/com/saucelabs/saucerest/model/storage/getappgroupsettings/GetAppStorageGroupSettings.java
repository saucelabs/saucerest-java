
package com.saucelabs.saucerest.model.storage.getappgroupsettings;

import com.squareup.moshi.Json;

public class GetAppStorageGroupSettings {

    @Json(name = "settings")
    public Settings settings;
    @Json(name = "kind")
    public String kind;
    @Json(name = "identifier")
    public String identifier;

    /**
     * No args constructor for use in serialization
     */
    public GetAppStorageGroupSettings() {
    }

    /**
     * @param settings
     * @param identifier
     * @param kind
     */
    public GetAppStorageGroupSettings(Settings settings, String kind, String identifier) {
        super();
        this.settings = settings;
        this.kind = kind;
        this.identifier = identifier;
    }

}
