package com.saucelabs.saucerest.model.builds;

import com.squareup.moshi.Json;

public class State {

  @Json(name = "completed")
  public Boolean completed;

  @Json(name = "errored")
  public Boolean errored;

  @Json(name = "failed")
  public Boolean failed;

  @Json(name = "finished")
  public Boolean finished;

  @Json(name = "new")
  public Boolean _new;

  @Json(name = "passed")
  public Boolean passed;

  @Json(name = "public")
  public Boolean _public;

  @Json(name = "queued")
  public Boolean queued;

  @Json(name = "running")
  public Boolean running;

  /** No args constructor for use in serialization */
  public State() {}

  public State(
      Boolean completed,
      Boolean errored,
      Boolean failed,
      Boolean finished,
      Boolean _new,
      Boolean passed,
      Boolean _public,
      Boolean queued,
      Boolean running) {
    this.completed = completed;
    this.errored = errored;
    this.failed = failed;
    this.finished = finished;
    this._new = _new;
    this.passed = passed;
    this._public = _public;
    this.queued = queued;
    this.running = running;
  }
}
