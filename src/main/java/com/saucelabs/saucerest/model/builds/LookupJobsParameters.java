package com.saucelabs.saucerest.model.builds;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class LookupJobsParameters {
  private final Boolean completed;
  private final Boolean errored;
  private final Boolean failed;
  private final Boolean faulty;
  private final Boolean finished;
  private final Integer modifiedSince;
  private final Boolean new_;
  private final Boolean passed;
  private final Boolean public_;
  private final Boolean queued;

  private LookupJobsParameters(Builder builder) {
    this.completed = builder.completed;
    this.errored = builder.errored;
    this.failed = builder.failed;
    this.faulty = builder.faulty;
    this.finished = builder.finished;
    this.modifiedSince = builder.modifiedSince;
    this.new_ = builder.new_;
    this.passed = builder.passed;
    this.public_ = builder.public_;
    this.queued = builder.queued;
  }

  public Map<String, Object> toMap() {
    Map<String, Object> parameters = new HashMap<>();

    Stream.of(
            new AbstractMap.SimpleEntry<>("completed", completed),
            new AbstractMap.SimpleEntry<>("errored", errored),
            new AbstractMap.SimpleEntry<>("failed", failed),
            new AbstractMap.SimpleEntry<>("faulty", faulty),
            new AbstractMap.SimpleEntry<>("finished", finished),
            new AbstractMap.SimpleEntry<>("modified_since", modifiedSince),
            new AbstractMap.SimpleEntry<>("new", new_),
            new AbstractMap.SimpleEntry<>("passed", passed),
            new AbstractMap.SimpleEntry<>("public", public_),
            new AbstractMap.SimpleEntry<>("queued", queued))
        .filter(e -> e.getValue() != null)
        .forEach(e -> parameters.put(e.getKey(), e.getValue()));

    return parameters;
  }

  /** {@code LookupJobsParameters} builder static inner class. */
  public static final class Builder {
    private Boolean completed;
    private Boolean errored;
    private Boolean failed;
    private Boolean faulty;
    private Boolean finished;
    private Integer modifiedSince;
    private Boolean new_;
    private Boolean passed;
    private Boolean public_;
    private Boolean queued;

    public Builder() {}

    /**
     * Sets the {@code completed} and returns a reference to this Builder enabling method chaining.
     *
     * @param val the {@code completed} to set
     * @return a reference to this Builder
     */
    public Builder setCompleted(Boolean val) {
      completed = val;
      return this;
    }

    /**
     * Sets the {@code errored} and returns a reference to this Builder enabling method chaining.
     *
     * @param val the {@code errored} to set
     * @return a reference to this Builder
     */
    public Builder setErrored(Boolean val) {
      errored = val;
      return this;
    }

    /**
     * Sets the {@code failed} and returns a reference to this Builder enabling method chaining.
     *
     * @param val the {@code failed} to set
     * @return a reference to this Builder
     */
    public Builder setFailed(Boolean val) {
      failed = val;
      return this;
    }

    /**
     * Sets the {@code faulty} and returns a reference to this Builder enabling method chaining.
     *
     * @param val the {@code faulty} to set
     * @return a reference to this Builder
     */
    public Builder setFaulty(Boolean val) {
      faulty = val;
      return this;
    }

    /**
     * Sets the {@code finished} and returns a reference to this Builder enabling method chaining.
     *
     * @param val the {@code finished} to set
     * @return a reference to this Builder
     */
    public Builder setFinished(Boolean val) {
      finished = val;
      return this;
    }

    /**
     * Sets the {@code modfied_since} and returns a reference to this Builder enabling method
     * chaining.
     *
     * @param val the {@code modfied_since} to set
     * @return a reference to this Builder
     */
    public Builder setModifiedSince(Integer val) {
      modifiedSince = val;
      return this;
    }

    /**
     * Sets the {@code new} and returns a reference to this Builder enabling method chaining.
     *
     * @param val the {@code new} to set
     * @return a reference to this Builder
     */
    public Builder setNew(Boolean val) {
      new_ = val;
      return this;
    }

    /**
     * Sets the {@code passed} and returns a reference to this Builder enabling method chaining.
     *
     * @param val the {@code passed} to set
     * @return a reference to this Builder
     */
    public Builder setPassed(Boolean val) {
      passed = val;
      return this;
    }

    /**
     * Sets the {@code public} and returns a reference to this Builder enabling method chaining.
     *
     * @param val the {@code public} to set
     * @return a reference to this Builder
     */
    public Builder setPublic(Boolean val) {
      public_ = val;
      return this;
    }

    /**
     * Sets the {@code queued} and returns a reference to this Builder enabling method chaining.
     *
     * @param val the {@code queued} to set
     * @return a reference to this Builder
     */
    public Builder setQueued(Boolean val) {
      queued = val;
      return this;
    }

    /**
     * Returns a {@code LookupJobsParameters} built from the parameters previously set.
     *
     * @return a {@code LookupJobsParameters} built with parameters of this {@code
     *     LookupJobsParameters.Builder}
     */
    public LookupJobsParameters build() {
      return new LookupJobsParameters(this);
    }
  }
}
