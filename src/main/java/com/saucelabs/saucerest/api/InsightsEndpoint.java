package com.saucelabs.saucerest.api;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.HttpMethod;
import com.saucelabs.saucerest.Unfinished;
import com.saucelabs.saucerest.model.insights.TestResult;
import com.saucelabs.saucerest.model.insights.TestResultParameter;
import java.io.IOException;
import java.util.Map;

@Unfinished("This endpoint is not yet completely implemented")
public class InsightsEndpoint extends AbstractEndpoint {
    public InsightsEndpoint(DataCenter dataCenter) {
        super(dataCenter);
    }

    public InsightsEndpoint(String apiServer) {
        super(apiServer);
    }

    public InsightsEndpoint(String username, String accessKey, DataCenter dataCenter) {
        super(username, accessKey, dataCenter);
    }

    public InsightsEndpoint(String username, String accessKey, String apiServer) {
        super(username, accessKey, apiServer);
    }

    public TestResult getTestResults(TestResultParameter parameter) throws IOException {
        String url = getBaseEndpoint() + "v1/analytics/tests";
        Map<String, Object> params;
        params = parameter.toMap();

        return deserializeJSONObject(requestWithQueryParameters(url, HttpMethod.GET, params), TestResult.class);
    }
}