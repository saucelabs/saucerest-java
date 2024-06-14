package com.saucelabs.saucerest.model.builds;

public class Build {

  public Integer creationTime;

  public Integer deletionTime;

  public Integer endTime;

  public String groupId;

  public String id;

  public Jobs jobs;

  public Integer modificationTime;

  public String name;

  public String orgId;

  public String ownerId;

  public Object passed;

  public Boolean _public;

  public Integer run;

  public String source;

  public Integer startTime;

  public String status;

  public String teamId;

  public Build() {}

  public Build(
      Integer creationTime,
      Integer deletionTime,
      Integer endTime,
      String groupId,
      String id,
      Jobs jobs,
      Integer modificationTime,
      String name,
      String orgId,
      String ownerId,
      Object passed,
      Boolean _public,
      Integer run,
      String source,
      Integer startTime,
      String status,
      String teamId) {
    super();
    this.creationTime = creationTime;
    this.deletionTime = deletionTime;
    this.endTime = endTime;
    this.groupId = groupId;
    this.id = id;
    this.jobs = jobs;
    this.modificationTime = modificationTime;
    this.name = name;
    this.orgId = orgId;
    this.ownerId = ownerId;
    this.passed = passed;
    this._public = _public;
    this.run = run;
    this.source = source;
    this.startTime = startTime;
    this.status = status;
    this.teamId = teamId;
  }
}
