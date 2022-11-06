
package com.saucelabs.saucerest.model.storage;

import com.squareup.moshi.Json;

import java.util.List;

public class GetAppFiles {

    @Json(name = "items")
    public List<Item> items = null;
    @Json(name = "links")
    public Links links;
    @Json(name = "page")
    public Integer page;
    @Json(name = "per_page")
    public Integer perPage;
    @Json(name = "total_items")
    public Integer totalItems;

    /**
     * No args constructor for use in serialization
     */
    public GetAppFiles() {
    }

    /**
     *
     * @param totalItems
     * @param perPage
     * @param links
     * @param page
     * @param items
     */
    public GetAppFiles(List<Item> items, Links links, Integer page, Integer perPage, Integer totalItems) {
        super();
        this.items = items;
        this.links = links;
        this.page = page;
        this.perPage = perPage;
        this.totalItems = totalItems;
    }

}
