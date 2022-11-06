package com.saucelabs.saucerest.model.storage;

import com.squareup.moshi.Json;

public class EditFileDescription {

    @Json(name = "item")
    public Item item;
    @Json(name = "changed")
    public Boolean changed;

    /**
     * No args constructor for use in serialization
     */
    public EditFileDescription() {
    }

    /**
     * @param item
     * @param changed
     */
    public EditFileDescription(Item item, Boolean changed) {
        super();
        this.item = item;
        this.changed = changed;
    }
}