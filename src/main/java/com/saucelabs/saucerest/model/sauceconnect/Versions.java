package com.saucelabs.saucerest.model.sauceconnect;

import java.util.List;

public class Versions {

    public Downloads downloads;
    public String infoUrl;
    public String latestVersion;
    public List<String> warning = null;

    /**
     * No args constructor for use in serialization
     */
    public Versions() {
    }

    /**
     * @param downloads
     * @param infoUrl
     * @param latestVersion
     * @param warning
     */
    public Versions(Downloads downloads, String infoUrl, String latestVersion, List<String> warning) {
        super();
        this.downloads = downloads;
        this.infoUrl = infoUrl;
        this.latestVersion = latestVersion;
        this.warning = warning;
    }
}
