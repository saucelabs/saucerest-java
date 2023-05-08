package com.saucelabs.saucerest.api;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.HttpMethod;
import com.saucelabs.saucerest.JobSource;
import com.saucelabs.saucerest.model.builds.Build;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

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

    /**
     * Queries the requesting account and returns a summary of each build matching the query, including the ID value, which may be a required parameter of other API calls related to a specific build.
     * <p>
     * You can narrow the results of your query using any of the optional filtering parameters.
     *
     * @param jobSource The type of device for which you are getting builds. Valid values are: {@link JobSource}
     * @return A list of {@link Build} objects
     * @throws IOException when the request fails
     */
    public List<Build> lookupBuilds(JobSource jobSource) throws IOException {
        String url = getBaseEndpoint(jobSource);

        return deserializeJSONObject(request(url, HttpMethod.GET), Collections.singletonList(Build.class));
    }

    /**
     * The base endpoint of the Builds endpoint APIs.
     */
    protected String getBaseEndpoint(JobSource jobSource) {
        return super.getBaseEndpoint() + "v2/builds/" + jobSource.value + "/";
    }
}