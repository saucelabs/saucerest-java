package com.saucelabs.saucerest.model.storage;

import com.squareup.moshi.Json;

public class DeleteAppFile {

    @Json(name = "item")
    public Item item;

    public DeleteAppFile() {
    }

    public DeleteAppFile(Item item) {
        super();
        this.item = item;
    }
}