
package com.saucelabs.saucerest.model.storage.getappgroups;

import com.squareup.moshi.Json;

import java.util.List;

public class GetAppStorageGroups {

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
    @Json(name = "is_simulator")
    public Boolean isSimulator;

    /**
     * No args constructor for use in serialization
     */
    public GetAppStorageGroups() {
    }

    /**
     * @param totalItems
     * @param perPage
     * @param links
     * @param page
     * @param items
     * @param isSimulator
     */
    public GetAppStorageGroups(List<Item> items, Links links, Integer page, Integer perPage, Integer totalItems, boolean isSimulator) {
        super();
        this.items = items;
        this.links = links;
        this.page = page;
        this.perPage = perPage;
        this.totalItems = totalItems;
        this.isSimulator = isSimulator;
    }

}
