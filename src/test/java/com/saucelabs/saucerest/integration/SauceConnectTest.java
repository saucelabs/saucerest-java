package com.saucelabs.saucerest.integration;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.SauceConnect;
import com.saucelabs.saucerest.model.sauceconnect.StopTunnel;
import com.saucelabs.saucerest.model.sauceconnect.TunnelInformation;
import com.saucelabs.saucerest.model.sauceconnect.Versions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.util.List;

import static com.saucelabs.saucerest.DataCenter.EU_CENTRAL;
import static com.saucelabs.saucerest.DataCenter.US_WEST;

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

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getTunnelsForAUserTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        SauceConnect sauceConnect = sauceREST.getSauceConnect();

        List<String> tunnelIDs = sauceConnect.getTunnelsForAUser();

        Assertions.assertEquals(dataCenter.equals(EU_CENTRAL) || dataCenter.equals(US_WEST) ? 1 : 0, tunnelIDs.size());
    }

    @AfterAll
    @SuppressWarnings("all")
    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public static void stopTunnels() throws IOException {
        SauceREST sauceREST = new SauceREST(EU_CENTRAL);
        SauceConnect sauceConnect = sauceREST.getSauceConnect();

        List<String> tunnelIDs = sauceConnect.getTunnelsForAUser();

        for (String tunnelID : tunnelIDs) {
            StopTunnel stopTunnel = sauceConnect.stopTunnel(tunnelID);

            Assertions.assertTrue(stopTunnel.result);
            Assertions.assertFalse(stopTunnel.id.isEmpty());
            Assertions.assertNotNull(stopTunnel.jobsRunning);
        }
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getTunnelInformation(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        SauceConnect sauceConnect = sauceREST.getSauceConnect();

        List<String> tunnelIDs = sauceConnect.getTunnelsForAUser();
        TunnelInformation tunnelInformation = sauceConnect.getTunnelInformation(tunnelIDs.get(0));

        Assertions.assertEquals(1, tunnelIDs.size());
        Assertions.assertNotNull(tunnelInformation);
    }
}
