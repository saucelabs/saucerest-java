
package com.saucelabs.saucerest.model.sauceconnect;

import com.squareup.moshi.Json;

public class Linux {

    @Json(name = "download_url")
    public String downloadUrl;
    @Json(name = "sha1")
    public String sha1;

    /**
     * No args constructor for use in serialization
     */
    public Linux() {
    }

    /**
     * @param sha1
     * @param downloadUrl
     */
    public Linux(String downloadUrl, String sha1) {
        super();
        this.downloadUrl = downloadUrl;
        this.sha1 = sha1;
    }
}
