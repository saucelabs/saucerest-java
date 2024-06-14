package com.saucelabs.saucerest.model.sauceconnect;

public class Osx {

  public String downloadUrl;

  public String sha256;

  /** No args constructor for use in serialization */
  public Osx() {}

  /**
   * @param sha256
   * @param downloadUrl
   */
  public Osx(String downloadUrl, String sha256) {
    super();
    this.downloadUrl = downloadUrl;
    this.sha256 = sha256;
  }
}
