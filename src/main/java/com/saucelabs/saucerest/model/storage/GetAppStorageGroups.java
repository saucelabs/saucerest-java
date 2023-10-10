package com.saucelabs.saucerest.model.storage;

import com.squareup.moshi.Json;
import java.util.List;

public class GetAppStorageGroups {

    @Json(name = "items")
    public List<ItemInteger> items;
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

    public GetAppStorageGroups() {
    }

    public GetAppStorageGroups(List<ItemInteger> items, Links links, Integer page, Integer perPage, Integer totalItems, boolean isSimulator) {
        super();
        this.items = items;
        this.links = links;
        this.page = page;
        this.perPage = perPage;
        this.totalItems = totalItems;
        this.isSimulator = isSimulator;
    }
}