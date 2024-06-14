package com.saucelabs.saucerest.model.storage;

import java.util.List;

public class GetAppStorageGroups {

    public List<ItemInteger> items;
    public Links links;
    public Integer page;
    public Integer perPage;
    public Integer totalItems;
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