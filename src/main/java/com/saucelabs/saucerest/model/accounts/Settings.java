
package com.saucelabs.saucerest.model.accounts;

import com.squareup.moshi.Json;

public class Settings {

  @Json(name = "live_only")
  public Boolean liveOnly;
  @Json(name = "real_devices")
  public Integer realDevices;
  @Json(name = "virtual_machines")
  public Integer virtualMachines;

  /**
   * No args constructor for use in serialization
   */
  public Settings() {
  }

  /**
   * @param realDevices
   * @param liveOnly
   * @param virtualMachines
   */
  public Settings(Boolean liveOnly, Integer realDevices, Integer virtualMachines) {
    super();
    this.liveOnly = liveOnly;
    this.realDevices = realDevices;
    this.virtualMachines = virtualMachines;
  }
}
