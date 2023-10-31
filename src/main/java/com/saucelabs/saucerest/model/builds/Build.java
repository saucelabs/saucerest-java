package com.saucelabs.saucerest.model.builds;

import com.saucelabs.saucerest.model.AbstractModel;
import com.squareup.moshi.Json;

public class Build extends AbstractModel {

  @Json(name = "creation_time")
  public Integer creationTime;

  @Json(name = "deletion_time")
  public Integer deletionTime;

  @Json(name = "end_time")
  public Integer endTime;

  @Json(name = "group_id")
  public String groupId;

  @Json(name = "id")
  public String id;

  @Json(name = "jobs")
  public Jobs jobs;

  @Json(name = "modification_time")
  public Integer modificationTime;

  @Json(name = "name")
  public String name;

  @Json(name = "org_id")
  public String orgId;

  @Json(name = "owner_id")
  public String ownerId;

  @Json(name = "passed")
  public Object passed;

  @Json(name = "public")
  public Boolean _public;

  @Json(name = "run")
  public Integer run;

  @Json(name = "source")
  public String source;

  @Json(name = "start_time")
  public Integer startTime;

  @Json(name = "status")
  public String status;

  @Json(name = "team_id")
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
