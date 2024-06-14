package com.saucelabs.saucerest.model.builds;

import java.util.List;

public class JobsInBuild {

  public List<JobInBuild> jobs;

  /** No args constructor for use in serialization */
  public JobsInBuild() {}

  public JobsInBuild(List<JobInBuild> jobs) {
    this.jobs = jobs;
  }
}
