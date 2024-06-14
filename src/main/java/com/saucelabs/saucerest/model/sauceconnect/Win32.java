package com.saucelabs.saucerest.model.sauceconnect;

public class Win32 {

  public String downloadUrl;

  public String sha256;

  /** No args constructor for use in serialization */
  public Win32() {}

  /**
   * @param sha256
   * @param downloadUrl
   */
  public Win32(String downloadUrl, String sha256) {
    super();
    this.downloadUrl = downloadUrl;
    this.sha256 = sha256;
  }
}
