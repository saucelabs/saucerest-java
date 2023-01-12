package com.saucelabs.saucerest.api;

import com.saucelabs.saucerest.DataCenter;
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

        //return getResponseListClass(getResponseObject(url), List.class);
        return null;
    }

    public void getTunnelInformation() {

    }

    public void getCurrentJobsForATunnel() {

    }

    public void stopTunnel() {

    }

    /**
     * No authentication API to retrieve the latest version of Sauce Connect for all supported platforms.
     *
     * @return {@link Versions}
     * @throws IOException API request failed
     */
    public Versions getLatestVersions() throws IOException {
        String url = getBaseEndpoint() + "public/tunnels/info/versions";

        return getResponseClass(getResponseObject(url), Versions.class);
    }

    /**
     * The base endpoint of the Sauce Connect endpoint APIs.
     */
    private String getBaseEndpoint() {
        return baseURL + "rest/v1/";
    }
}
