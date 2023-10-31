package com.saucelabs.saucerest.model.builds;

import com.squareup.moshi.Json;

public class JobInBuild {

  @Json(name = "creation_time")
  public Integer creationTime;

  @Json(name = "deletion_time")
  public Integer deletionTime;

  @Json(name = "id")
  public String id;

  @Json(name = "modification_time")
  public Integer modificationTime;

  @Json(name = "state")
  public State state;

  /** No args constructor for use in serialization */
  public JobInBuild() {}

  public JobInBuild(
      Integer creationTime,
      Integer deletionTime,
      String id,
      Integer modificationTime,
      State state) {
    this.creationTime = creationTime;
    this.deletionTime = deletionTime;
    this.id = id;
    this.modificationTime = modificationTime;
    this.state = state;
  }
}
