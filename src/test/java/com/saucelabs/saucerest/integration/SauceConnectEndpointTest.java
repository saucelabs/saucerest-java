package com.saucelabs.saucerest.integration;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.SauceConnectEndpoint;
import com.saucelabs.saucerest.model.sauceconnect.JobsForATunnel;
import com.saucelabs.saucerest.model.sauceconnect.StopTunnel;
import com.saucelabs.saucerest.model.sauceconnect.TunnelInformation;
import com.saucelabs.saucerest.model.sauceconnect.Versions;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Sauce Connect integration tests by nature require a running Sauce Connect tunnel. On GitHub this is done via an
 * action that starts the required tunnels. Locally running these tests require the developer to start a tunnel beforehand.
 */
public class SauceConnectEndpointTest {

    @AfterAll
    @SuppressWarnings("all")
    public static void tearDown() throws IOException {
        for (DataCenter dataCenter : DataCenter.values()) {
            SauceREST sauceREST = new SauceREST(dataCenter);
            SauceConnectEndpoint sauceConnectEndpoint = sauceREST.getSauceConnectEndpoint();

            List<String> tunnelIDs = sauceConnectEndpoint.getTunnelsForAUser();

            for (String tunnelID : tunnelIDs) {
                StopTunnel stopTunnel = sauceConnectEndpoint.stopTunnel(tunnelID);

                Assertions.assertTrue(stopTunnel.result);
                Assertions.assertFalse(stopTunnel.id.isEmpty());
                Assertions.assertNotNull(stopTunnel.jobsRunning);
            }
        }
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getLatestVersionTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        SauceConnectEndpoint sauceConnectEndpoint = sauceREST.getSauceConnectEndpoint();
        Versions versions = sauceConnectEndpoint.getLatestVersions();

        Assertions.assertFalse(versions.latestVersion.isEmpty());
        Assertions.assertFalse(versions.infoUrl.isEmpty());
        Assertions.assertFalse(versions.warning.isEmpty());
        Assertions.assertFalse(versions.downloads.linux.downloadUrl.isEmpty());
        Assertions.assertFalse(versions.downloads.linux.sha1.isEmpty());
        Assertions.assertFalse(versions.downloads.linuxArm64.downloadUrl.isEmpty());
        Assertions.assertFalse(versions.downloads.linuxArm64.sha1.isEmpty());
        Assertions.assertFalse(versions.downloads.osx.downloadUrl.isEmpty());
        Assertions.assertFalse(versions.downloads.osx.sha1.isEmpty());
        Assertions.assertFalse(versions.downloads.win32.downloadUrl.isEmpty());
        Assertions.assertFalse(versions.downloads.win32.sha1.isEmpty());
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getLatestVersionWithoutCredentialsTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST("", "", dataCenter);
        SauceConnectEndpoint sauceConnectEndpoint = sauceREST.getSauceConnectEndpoint();
        Versions versions = sauceConnectEndpoint.getLatestVersions();

        Assertions.assertFalse(versions.latestVersion.isEmpty());
        Assertions.assertFalse(versions.infoUrl.isEmpty());
        Assertions.assertFalse(versions.warning.isEmpty());
        Assertions.assertFalse(versions.downloads.linux.downloadUrl.isEmpty());
        Assertions.assertFalse(versions.downloads.linux.sha1.isEmpty());
        Assertions.assertFalse(versions.downloads.linuxArm64.downloadUrl.isEmpty());
        Assertions.assertFalse(versions.downloads.linuxArm64.sha1.isEmpty());
        Assertions.assertFalse(versions.downloads.osx.downloadUrl.isEmpty());
        Assertions.assertFalse(versions.downloads.osx.sha1.isEmpty());
        Assertions.assertFalse(versions.downloads.win32.downloadUrl.isEmpty());
        Assertions.assertFalse(versions.downloads.win32.sha1.isEmpty());
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getTunnelsForAUserTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        SauceConnectEndpoint sauceConnectEndpoint = sauceREST.getSauceConnectEndpoint();

        List<String> tunnelIDs = sauceConnectEndpoint.getTunnelsForAUser();

        Assertions.assertFalse(tunnelIDs.isEmpty());
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getTunnelsInformationForAUserTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        SauceConnectEndpoint sauceConnectEndpoint = sauceREST.getSauceConnectEndpoint();

        List<TunnelInformation> tunnelsInfo = sauceConnectEndpoint.getTunnelsInformationForAUser();

        Assertions.assertFalse(tunnelsInfo.isEmpty());
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getTunnelInformationTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        SauceConnectEndpoint sauceConnectEndpoint = sauceREST.getSauceConnectEndpoint();

        List<String> tunnelIDs = sauceConnectEndpoint.getTunnelsForAUser();

        Assertions.assertFalse(tunnelIDs.isEmpty());

        for (String tunnelID : tunnelIDs) {
            TunnelInformation tunnelInformation = sauceConnectEndpoint.getTunnelInformation(tunnelID);

            Assertions.assertNotNull(tunnelInformation);
        }
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getJobsForATunnelTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        SauceConnectEndpoint sauceConnectEndpoint = sauceREST.getSauceConnectEndpoint();
        List<String> tunnelIDs = sauceConnectEndpoint.getTunnelsForAUser();

        for (String tunnelID : tunnelIDs) {
            JobsForATunnel jobsForATunnel = sauceConnectEndpoint.getCurrentJobsForATunnel(tunnelID);

            Assertions.assertFalse(jobsForATunnel.id.isEmpty());
            Assertions.assertNotNull(jobsForATunnel.jobsRunning);
        }
    }
}