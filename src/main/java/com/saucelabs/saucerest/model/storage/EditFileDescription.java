package com.saucelabs.saucerest.model.storage;

import com.squareup.moshi.Json;

public class EditFileDescription {

    @Json(name = "item")
    public Item item;
    @Json(name = "changed")
    public Boolean changed;

    public EditFileDescription() {
    }

    public EditFileDescription(Item item, Boolean changed) {
        super();
        this.item = item;
        this.changed = changed;
    }
}