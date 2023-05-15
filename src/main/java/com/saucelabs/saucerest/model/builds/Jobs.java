
package com.saucelabs.saucerest.model.builds;

import com.squareup.moshi.Json;

public class Jobs {

    @Json(name = "completed")
    public Integer completed;
    @Json(name = "errored")
    public Integer errored;
    @Json(name = "failed")
    public Integer failed;
    @Json(name = "finished")
    public Integer finished;
    @Json(name = "passed")
    public Integer passed;
    @Json(name = "public")
    public Integer _public;
    @Json(name = "queued")
    public Integer queued;
    @Json(name = "running")
    public Integer running;

    /**
     * No args constructor for use in serialization
     */
    public Jobs() {
    }

    public Jobs(Integer completed, Integer errored, Integer failed, Integer finished, Integer passed, Integer _public, Integer queued, Integer running) {
        super();
        this.completed = completed;
        this.errored = errored;
        this.failed = failed;
        this.finished = finished;
        this.passed = passed;
        this._public = _public;
        this.queued = queued;
        this.running = running;
    }
}