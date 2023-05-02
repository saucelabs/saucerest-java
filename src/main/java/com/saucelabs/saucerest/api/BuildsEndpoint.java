package com.saucelabs.saucerest.api;

import com.saucelabs.saucerest.DataCenter;

public class BuildsEndpoint extends AbstractEndpoint {

    public BuildsEndpoint(DataCenter dataCenter) {
        super(dataCenter);
    }

    public BuildsEndpoint(String apiServer) {
        super(apiServer);
    }

    public BuildsEndpoint(String username, String accessKey, DataCenter dataCenter) {
        super(username, accessKey, dataCenter);
    }

    public BuildsEndpoint(String username, String accessKey, String apiServer) {
        super(username, accessKey, apiServer);
    }
}