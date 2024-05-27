package com.saucelabs.saucerest.model.sauceconnect;

import com.squareup.moshi.Json;

public class Linux {

  @Json(name = "download_url")
  public String downloadUrl;

  @Json(name = "sha256")
  public String sha256;

  /** No args constructor for use in serialization */
  public Linux() {}

  /**
   * @param sha256
   * @param downloadUrl
   */
  public Linux(String downloadUrl, String sha256) {
    super();
    this.downloadUrl = downloadUrl;
    this.sha256 = sha256;
  }
}
