package com.saucelabs.saucerest.api;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.model.sauceconnect.JobsForATunnel;
import com.saucelabs.saucerest.model.sauceconnect.StopTunnel;
import com.saucelabs.saucerest.model.sauceconnect.TunnelInformation;
import com.saucelabs.saucerest.model.sauceconnect.Versions;

import java.io.IOException;
import java.util.List;

public class SauceConnect extends AbstractEndpoint {
    public SauceConnect(DataCenter dataCenter) {
        super(dataCenter);
    }

    public SauceConnect(String apiServer) {
        super(apiServer);
    }

    public SauceConnect(String username, String accessKey, String apiServer) {
        super(username, accessKey, apiServer);
    }

    public SauceConnect(String username, String accessKey, DataCenter dataCenter) {
        super(username, accessKey, dataCenter);
    }

    /**
     * Returns Tunnel IDs or Tunnels Info for any currently running tunnels launched by or shared with the specified user.
     * Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/connect/#get-tunnels-for-a-user">here</a>
     */
    public List<String> getTunnelsForAUser() throws IOException {
        return getTunnelsForAUser(this.username);
    }

    /**
     * Returns Tunnel IDs or Tunnels Info for any currently running tunnels launched by or shared with the specified user.
     * Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/connect/#get-tunnels-for-a-user">here</a>
     *
     * @param username Sauce Labs username
     */
    public List<String> getTunnelsForAUser(String username) throws IOException {
        String url = getBaseEndpoint() + username + "/tunnels";

        return deserializeJSONArray(getResponseObject(url), String.class);
    }

    /**
     * Returns information about the specified tunnel.
     *
     * @param username The authentication username of the owner of the requested tunnel.
     * @param tunnelID The unique identifier of the requested tunnel.
     * @return {@link TunnelInformation}
     * @throws IOException
     */
    public TunnelInformation getTunnelInformation(String username, String tunnelID) throws IOException {
        String url = getBaseEndpoint() + username + "/tunnels/" + tunnelID;

        return deserializeJSONObject(getResponseObject(url), TunnelInformation.class);
    }

    /**
     * Returns information about the specified tunnel.
     *
     * @param tunnelID The unique identifier of the requested tunnel.
     * @return {@link TunnelInformation}
     * @throws IOException
     */
    public TunnelInformation getTunnelInformation(String tunnelID) throws IOException {
        return getTunnelInformation(this.username, tunnelID);
    }

    /**
     * Returns the number of currently running jobs for the specified tunnel.
     *
     * @param username The authentication username of the user whose tunnels you are requesting.
     * @param tunnelID The unique identifier of the requested tunnel.
     * @return {@link JobsForATunnel}
     * @throws IOException API request failed
     */
    public JobsForATunnel getCurrentJobsForATunnel(String username, String tunnelID) throws IOException {
        String url = getBaseEndpoint() + username + "/tunnels/" + tunnelID + "/num_jobs";

        return deserializeJSONObject(getResponseObject(url), JobsForATunnel.class);
    }

    /**
     * Returns the number of currently running jobs for the specified tunnel.
     *
     * @param tunnelID The unique identifier of the requested tunnel.
     * @return {@link JobsForATunnel}
     * @throws IOException API request failed
     */
    public JobsForATunnel getCurrentJobsForATunnel(String tunnelID) throws IOException {
        return getCurrentJobsForATunnel(this.username, tunnelID);
    }

    /**
     * Shuts down the specified tunnel.
     *
     * @param username The authentication username of the user whose tunnels you are requesting.
     * @param tunnelID The unique identifier of the tunnel to stop.
     * @return {@link StopTunnel}
     * @throws IOException API request failed
     */
    public StopTunnel stopTunnel(String username, String tunnelID) throws IOException {
        String url = getBaseEndpoint() + username + "/tunnels/" + tunnelID;

        return deserializeJSONObject(deleteResponse(url), StopTunnel.class);
    }

    /**
     * Shuts down the specified tunnel.
     *
     * @param tunnelID The unique identifier of the tunnel to stop.
     * @return {@link StopTunnel}
     * @throws IOException API request failed
     */
    public StopTunnel stopTunnel(String tunnelID) throws IOException {
        return stopTunnel(this.username, tunnelID);
    }

    /**
     * No authentication API to retrieve the latest version of Sauce Connect for all supported platforms.
     *
     * @return {@link Versions}
     * @throws IOException API request failed
     */
    public Versions getLatestVersions() throws IOException {
        String url = getBaseEndpoint() + "public/tunnels/info/versions";

        return deserializeJSONObject(getResponseObject(url), Versions.class);
    }

    /**
     * The base endpoint of the Sauce Connect endpoint APIs.
     */
    @Override
    public String getBaseEndpoint() {
        return super.getBaseEndpoint() + "rest/v1/";
    }
}