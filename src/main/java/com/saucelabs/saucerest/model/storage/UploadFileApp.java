
package com.saucelabs.saucerest.model.storage;

import com.squareup.moshi.Json;

public class UploadFileApp {

    @Json(name = "item")
    public Item item;

    /**
     * No args constructor for use in serialization
     */
    public UploadFileApp() {
    }

    /**
     * @param item
     */
    public UploadFileApp(Item item) {
        super();
        this.item = item;
    }

}
