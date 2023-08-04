package com.saucelabs.saucerest.api;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.Unfinished;

@Unfinished("This endpoint is not yet completely implemented")
public class PerformanceEndpoint extends AbstractEndpoint {
    public PerformanceEndpoint(DataCenter dataCenter) {
        super(dataCenter);
    }

    public PerformanceEndpoint(String apiServer) {
        super(apiServer);
    }

    public PerformanceEndpoint(String username, String accessKey, DataCenter dataCenter) {
        super(username, accessKey, dataCenter);
    }

    public PerformanceEndpoint(String username, String accessKey, String apiServer) {
        super(username, accessKey, apiServer);
    }
}