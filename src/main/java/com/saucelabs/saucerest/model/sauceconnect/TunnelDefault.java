package com.saucelabs.saucerest.model.sauceconnect;

import com.saucelabs.saucerest.model.storage.Item;
import com.squareup.moshi.Json;

public class TunnelDefault {
    @Json(name = "item")
    public Item item;

    public TunnelDefault() {
    }

    public TunnelDefault(Item item) {
        super();
        this.item = item;
    }
}
