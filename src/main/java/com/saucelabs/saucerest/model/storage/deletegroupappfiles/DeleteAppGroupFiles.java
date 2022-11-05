
package com.saucelabs.saucerest.model.storage.deletegroupappfiles;

import com.squareup.moshi.Json;

public class DeleteAppGroupFiles {

    @Json(name = "item")
    public Item item;

    /**
     * No args constructor for use in serialization
     */
    public DeleteAppGroupFiles() {
    }

    /**
     * @param item
     */
    public DeleteAppGroupFiles(Item item) {
        super();
        this.item = item;
    }

}
