package com.saucelabs.saucerest.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.JobSource;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.BuildsEndpoint;
import com.saucelabs.saucerest.model.builds.Build;
import com.saucelabs.saucerest.model.builds.LookupBuildsParameters;
import com.saucelabs.saucerest.model.builds.Status;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class BuildsEndpointTest {
    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void lookupBuildsTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        BuildsEndpoint buildsEndpoint = sauceREST.getBuildsEndpoint();

        List<Build> builds = buildsEndpoint.lookupBuilds(JobSource.VDC);

        assertTrue(builds.size() > 0);
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void lookupBuildsTestWithParameter(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        BuildsEndpoint buildsEndpoint = sauceREST.getBuildsEndpoint();

        LookupBuildsParameters parameters = new LookupBuildsParameters.Builder()
                .setLimit(1)
                .setStatus(new Status[]{Status.complete, Status.success})
                .build();

        List<Build> builds = buildsEndpoint.lookupBuilds(JobSource.VDC, parameters);

        assertEquals(1, builds.size());
        assertEquals(JobSource.VDC.value, builds.get(0).source);
        assertTrue(builds.get(0).status.equalsIgnoreCase(Status.complete.value) ||
                builds.get(0).status.equalsIgnoreCase(Status.success.value));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getSpecificBuildTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        BuildsEndpoint buildsEndpoint = sauceREST.getBuildsEndpoint();

        LookupBuildsParameters parameters = new LookupBuildsParameters.Builder()
                .setLimit(1)
                .build();

        List<Build> builds = buildsEndpoint.lookupBuilds(JobSource.VDC, parameters);

        Build build = buildsEndpoint.getSpecificBuild(JobSource.VDC, builds.get(0).id);

        assertEquals(builds.get(0).id, build.id);
    }
}