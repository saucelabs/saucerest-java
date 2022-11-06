package com.saucelabs.saucerest.model.storage;

import com.squareup.moshi.Json;

public class DeleteAppGroupFiles {

    @Json(name = "item")
    public ItemInteger item;

    public DeleteAppGroupFiles() {
    }

    public DeleteAppGroupFiles(ItemInteger item) {
        super();
        this.item = item;
    }
}