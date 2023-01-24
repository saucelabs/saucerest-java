package com.saucelabs.saucerest.integration;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.SauceConnect;
import com.saucelabs.saucerest.model.sauceconnect.JobsForATunnel;
import com.saucelabs.saucerest.model.sauceconnect.StopTunnel;
import com.saucelabs.saucerest.model.sauceconnect.TunnelInformation;
import com.saucelabs.saucerest.model.sauceconnect.Versions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.util.List;

import static com.saucelabs.saucerest.DataCenter.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        assertAll(
            () -> assertFalse(versions.latestVersion.isEmpty()),
            () -> assertFalse(versions.infoUrl.isEmpty()),
            () -> assertFalse(versions.warning.isEmpty()),
            () -> assertFalse(versions.downloads.linux.downloadUrl.isEmpty()),
            () -> assertFalse(versions.downloads.linux.sha1.isEmpty()),
            () -> assertFalse(versions.downloads.linuxArm64.downloadUrl.isEmpty()),
            () -> assertFalse(versions.downloads.linuxArm64.sha1.isEmpty()),
            () -> assertFalse(versions.downloads.osx.downloadUrl.isEmpty()),
            () -> assertFalse(versions.downloads.osx.sha1.isEmpty()),
            () -> assertFalse(versions.downloads.win32.downloadUrl.isEmpty()),
            () -> assertFalse(versions.downloads.win32.sha1.isEmpty())
        );
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getLatestVersionWithoutCredentialsTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST("", "", dataCenter);
        SauceConnect sauceConnect = sauceREST.getSauceConnect();
        Versions versions = sauceConnect.getLatestVersions();

        assertAll(
            () -> assertFalse(versions.latestVersion.isEmpty()),
            () -> assertFalse(versions.infoUrl.isEmpty()),
            () -> assertFalse(versions.warning.isEmpty()),
            () -> assertFalse(versions.downloads.linux.downloadUrl.isEmpty()),
            () -> assertFalse(versions.downloads.linux.sha1.isEmpty()),
            () -> assertFalse(versions.downloads.linuxArm64.downloadUrl.isEmpty()),
            () -> assertFalse(versions.downloads.linuxArm64.sha1.isEmpty()),
            () -> assertFalse(versions.downloads.osx.downloadUrl.isEmpty()),
            () -> assertFalse(versions.downloads.osx.sha1.isEmpty()),
            () -> assertFalse(versions.downloads.win32.downloadUrl.isEmpty()),
            () -> assertFalse(versions.downloads.win32.sha1.isEmpty())
        );
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

                assertTrue(stopTunnel.result);
                assertFalse(stopTunnel.id.isEmpty());
                assertNotNull(stopTunnel.jobsRunning);
            }
        }
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getTunnelInformationTestAndGetJobsForATunnelTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        SauceConnect sauceConnect = sauceREST.getSauceConnect();

        List<String> tunnelIDs = sauceConnect.getTunnelsForAUser();

        if (dataCenter.equals(US_EAST)) {
            assertEquals(0, tunnelIDs.size());
        } else {
            assertEquals(1, tunnelIDs.size());
            assertAll(
                () -> {
                    TunnelInformation tunnelInformation = sauceConnect.getTunnelInformation(tunnelIDs.get(0));
                    assertNotNull(tunnelInformation);
                },
                () -> {
                    JobsForATunnel jobsForATunnel = sauceConnect.getCurrentJobsForATunnel(tunnelIDs.get(0));

                    assertFalse(jobsForATunnel.id.isEmpty());
                    assertNotNull(jobsForATunnel.jobsRunning);
                }
            );
        }
    }
}
