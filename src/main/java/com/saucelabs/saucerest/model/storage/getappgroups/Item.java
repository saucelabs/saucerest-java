
package com.saucelabs.saucerest.model.storage.getappgroups;

import com.saucelabs.saucerest.model.storage.Access;
import com.saucelabs.saucerest.model.storage.Recent;
import com.saucelabs.saucerest.model.storage.Settings;
import com.squareup.moshi.Json;

public class Item {

    @Json(name = "id")
    public Integer id;
    @Json(name = "name")
    public String name;
    @Json(name = "recent")
    public Recent recent;
    @Json(name = "count")
    public Integer count;
    @Json(name = "access")
    public Access access;
    @Json(name = "settings")
    public Settings settings;

    /**
     * No args constructor for use in serialization
     */
    public Item() {
    }

    /**
     * @param settings
     * @param access
     * @param name
     * @param count
     * @param id
     * @param recent
     */
    public Item(Integer id, String name, Recent recent, Integer count, Access access, Settings settings) {
        super();
        this.id = id;
        this.name = name;
        this.recent = recent;
        this.count = count;
        this.access = access;
        this.settings = settings;
    }

}
