package com.saucelabs.saucerest.model.storage;

import com.squareup.moshi.Json;

public class UploadFileApp {

    @Json(name = "item")
    public Item item;

    public UploadFileApp() {
    }

    public UploadFileApp(Item item) {
        super();
        this.item = item;
    }
}