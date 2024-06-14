package com.saucelabs.saucerest.model.sauceconnect;

public class LinuxArm64 {

  public String downloadUrl;

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
