package com.saucelabs.saucerest.api;

import com.saucelabs.saucerest.DataCenter;

public class Builds extends AbstractEndpoint {

    public Builds(DataCenter dataCenter) {
        super(dataCenter);
    }

    public Builds(String apiServer) {
        super(apiServer);
    }

    public Builds(String username, String accessKey, DataCenter dataCenter) {
        super(username, accessKey, dataCenter);
    }

    public Builds(String username, String accessKey, String apiServer) {
        super(username, accessKey, apiServer);
    }
}
