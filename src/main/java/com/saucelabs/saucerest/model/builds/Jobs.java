
package com.saucelabs.saucerest.model.builds;

public class Jobs {

    public Integer completed;
    public Integer errored;
    public Integer failed;
    public Integer finished;
    public Integer passed;
    public Integer _public;
    public Integer queued;
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