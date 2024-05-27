package com.saucelabs.saucerest.model.sauceconnect;

import com.squareup.moshi.Json;

public class LinuxArm64 {

  @Json(name = "download_url")
  public String downloadUrl;

  @Json(name = "sha256")
  public String sha256;

  /** No args constructor for use in serialization */
  public LinuxArm64() {}

  /**
   * @param sha256
   * @param downloadUrl
   */
  public LinuxArm64(String downloadUrl, String sha256) {
    super();
    this.downloadUrl = downloadUrl;
    this.sha256 = sha256;
  }
}
