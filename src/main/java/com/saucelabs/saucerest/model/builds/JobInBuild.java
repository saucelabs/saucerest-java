package com.saucelabs.saucerest.model.builds;

public class JobInBuild {

  public Integer creationTime;

  public Integer deletionTime;

  public String id;

  public Integer modificationTime;

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
