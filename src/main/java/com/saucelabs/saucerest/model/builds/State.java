package com.saucelabs.saucerest.model.builds;

public class State {

  public Boolean completed;

  public Boolean errored;

  public Boolean failed;

  public Boolean finished;

  public Boolean _new;

  public Boolean passed;

  public Boolean _public;

  public Boolean queued;

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
