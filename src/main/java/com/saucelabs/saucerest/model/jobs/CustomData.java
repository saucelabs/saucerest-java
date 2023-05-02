package com.saucelabs.saucerest.model.jobs;

import com.squareup.moshi.Json;

public class CustomData {

    @Json(name = "tcd")
    public String tcd;
    @Json(name = "editor")
    public String editor;

    /**
     * No args constructor for use in serialization
     */
    public CustomData() {
    }

    /**
     * @param editor
     * @param tcd
     */
    public CustomData(String tcd, String editor) {
        super();
        this.tcd = tcd;
        this.editor = editor;
    }

}