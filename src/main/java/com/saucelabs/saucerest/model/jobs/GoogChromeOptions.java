package com.saucelabs.saucerest.model.jobs;

import java.util.List;

public class GoogChromeOptions {

    public List<String> args;
    public List<Object> extensions;

    public GoogChromeOptions() {
    }

    public GoogChromeOptions(List<String> args, List<Object> extensions) {
        super();
        this.args = args;
        this.extensions = extensions;
    }
}