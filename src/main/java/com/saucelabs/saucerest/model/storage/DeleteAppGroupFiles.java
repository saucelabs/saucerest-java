
package com.saucelabs.saucerest.model.storage;

import com.squareup.moshi.Json;

public class DeleteAppGroupFiles {

    @Json(name = "item")
    public ItemInteger item;

    /**
     * No args constructor for use in serialization
     */
    public DeleteAppGroupFiles() {
    }

    /**
     * @param item
     */
    public DeleteAppGroupFiles(ItemInteger item) {
        super();
        this.item = item;
    }
}