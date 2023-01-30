package com.saucelabs.saucerest.integration;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.SauceConnect;
import com.saucelabs.saucerest.model.sauceconnect.JobsForATunnel;
import com.saucelabs.saucerest.model.sauceconnect.StopTunnel;
import com.saucelabs.saucerest.model.sauceconnect.TunnelInformation;
import com.saucelabs.saucerest.model.sauceconnect.Versions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.util.List;

import static com.saucelabs.saucerest.DataCenter.*;

/**
 * Sauce Connect integration tests by nature require a running Sauce Connect tunnel. On GitHub this is done via an
 * action that starts the required tunnels. Locally running these tests require the developer to start a tunnel beforehand.
 */
public class SauceConnectTest {

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getLatestVersionTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        SauceConnect sauceConnect = sauceREST.getSauceConnect();
        Versions versions = sauceConnect.getLatestVersions();

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
        SauceConnect sauceConnect = sauceREST.getSauceConnect();
        Versions versions = sauceConnect.getLatestVersions();

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

    @AfterAll
    @SuppressWarnings("all")
    public static void tearDown() throws IOException {
        for (DataCenter dataCenter : DataCenter.values()) {
            SauceREST sauceREST = new SauceREST(dataCenter);
            SauceConnect sauceConnect = sauceREST.getSauceConnect();

            List<String> tunnelIDs = sauceConnect.getTunnelsForAUser();

            for (String tunnelID : tunnelIDs) {
                StopTunnel stopTunnel = sauceConnect.stopTunnel(tunnelID);

                Assertions.assertTrue(stopTunnel.result);
                Assertions.assertFalse(stopTunnel.id.isEmpty());
                Assertions.assertNotNull(stopTunnel.jobsRunning);
            }
        }
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"EU_CENTRAL", "US_WEST", "APAC_SOUTHEAST"}, mode = EnumSource.Mode.INCLUDE)
    public void getTunnelsForAUserTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        SauceConnect sauceConnect = sauceREST.getSauceConnect();

        List<String> tunnelIDs = sauceConnect.getTunnelsForAUser();

        Assertions.assertEquals(
            dataCenter.equals(EU_CENTRAL) ||
                dataCenter.equals(US_WEST) ||
                dataCenter.equals(APAC_SOUTHEAST) ? 1 : 0, tunnelIDs.size());
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"EU_CENTRAL", "US_WEST", "APAC_SOUTHEAST"}, mode = EnumSource.Mode.INCLUDE)
    public void getTunnelInformationTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        SauceConnect sauceConnect = sauceREST.getSauceConnect();

        List<String> tunnelIDs = sauceConnect.getTunnelsForAUser();

        for (String tunnelID : tunnelIDs) {
            TunnelInformation tunnelInformation = sauceConnect.getTunnelInformation(tunnelID);

            Assertions.assertEquals(1, tunnelIDs.size());
            Assertions.assertNotNull(tunnelInformation);
        }
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"EU_CENTRAL", "US_WEST", "APAC_SOUTHEAST"}, mode = EnumSource.Mode.INCLUDE)
    public void getJobsForATunnelTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        SauceConnect sauceConnect = sauceREST.getSauceConnect();
        List<String> tunnelIDs = sauceConnect.getTunnelsForAUser();

        for (String tunnelID : tunnelIDs) {
            JobsForATunnel jobsForATunnel = sauceConnect.getCurrentJobsForATunnel(tunnelID);

            Assertions.assertFalse(jobsForATunnel.id.isEmpty());
            Assertions.assertNotNull(jobsForATunnel.jobsRunning);
        }
    }
}
