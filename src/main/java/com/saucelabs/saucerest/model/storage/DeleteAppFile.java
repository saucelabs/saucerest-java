
package com.saucelabs.saucerest.model.storage;

import com.squareup.moshi.Json;

public class DeleteAppFile {

    @Json(name = "item")
    public Item item;

    /**
     * No args constructor for use in serialization
     */
    public DeleteAppFile() {
    }

    /**
     * @param item
     */
    public DeleteAppFile(Item item) {
        super();
        this.item = item;
    }

}
