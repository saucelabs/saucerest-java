
package com.saucelabs.saucerest.model.accounts;

import com.squareup.moshi.Json;

public class Group {

  @Json(name = "id")
  public String id;
  @Json(name = "name")
  public String name;

  /**
   * No args constructor for use in serialization
   */
  public Group() {
  }

  /**
   * @param name
   * @param id
   */
  public Group(String id, String name) {
    super();
    this.id = id;
    this.name = name;
  }

}
