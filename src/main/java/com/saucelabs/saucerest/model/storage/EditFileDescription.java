package com.saucelabs.saucerest.model.storage;

public class EditFileDescription {

    public Item item;
    public Boolean changed;

    public EditFileDescription() {
    }

    public EditFileDescription(Item item, Boolean changed) {
        super();
        this.item = item;
        this.changed = changed;
    }
}