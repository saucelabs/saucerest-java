package com.saucelabs.saucerest.model.sauceconnect;

import com.squareup.moshi.Json;
import java.util.List;

public class Versions {

    @Json(name = "downloads")
    public Downloads downloads;
    @Json(name = "info_url")
    public String infoUrl;
    @Json(name = "latest_version")
    public String latestVersion;
    @Json(name = "warning")
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
