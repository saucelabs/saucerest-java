
package com.saucelabs.saucerest.model.accounts;

import com.squareup.moshi.Json;

public class Group {

  @Json(name = "id")
  public String id;
  @Json(name = "name")
  public String name;
  @Json(name = "virtual_machines")
  public Integer virtualMachines;
  @Json(name = "real_devices")
  public Integer realDevices;

  /**
   * No args constructor for use in serialization
   */
  public Group() {
  }

  /**
   * @param realDevices
   * @param name
   * @param id
   * @param virtualMachines
   */
  public Group(String id, String name, Integer virtualMachines, Integer realDevices) {
    super();
    this.id = id;
    this.name = name;
    this.virtualMachines = virtualMachines;
    this.realDevices = realDevices;
  }
}
