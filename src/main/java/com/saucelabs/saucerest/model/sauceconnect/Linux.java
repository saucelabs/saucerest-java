package com.saucelabs.saucerest.model.sauceconnect;

public class Linux {

  public String downloadUrl;

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
