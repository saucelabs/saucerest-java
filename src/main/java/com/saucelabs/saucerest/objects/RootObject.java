package com.saucelabs.saucerest.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by halkeye on 2015-11-29.
 */
public class RootObject {
    @JsonProperty("files")
    private ArrayList<File> files;

    public ArrayList<File> getFiles() { return this.files; }

    public void setFiles(ArrayList<File> files) { this.files = files; }

    @JsonProperty("jobs")
    private ArrayList<Job> jobs;

    final public ArrayList<Job> getJobs() {
        return jobs;
    }

    @JsonProperty("truncated")
    private boolean truncated;

    final public boolean getTruncated() {
        return truncated;
    }
}
