package com.saucelabs.saucerest.model.insights;

import java.util.List;

public class ErrorBucket {
    public String name;
    public Integer count;
    public List<Item> items;
    public Boolean hasMore;
}
