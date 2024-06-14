package com.saucelabs.saucerest.model.jobs;

public class CustomData {

    public String tcd;
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