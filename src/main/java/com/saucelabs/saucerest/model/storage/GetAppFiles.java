package com.saucelabs.saucerest.model.storage;

import java.util.List;

public class GetAppFiles {

    public List<Item> items = null;
    public Links links;
    public Integer page;
    public Integer perPage;
    public Integer totalItems;

    public GetAppFiles() {
    }

    public GetAppFiles(List<Item> items, Links links, Integer page, Integer perPage, Integer totalItems) {
        super();
        this.items = items;
        this.links = links;
        this.page = page;
        this.perPage = perPage;
        this.totalItems = totalItems;
    }
}