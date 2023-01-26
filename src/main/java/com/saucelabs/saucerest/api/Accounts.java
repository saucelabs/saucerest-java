package com.saucelabs.saucerest.api;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.model.accounts.LookupTeams;
import com.saucelabs.saucerest.model.accounts.Team;

import java.io.IOException;

/**
 * Account Management API Methods <br>
 * <p>
 * The Accounts API exposes the following methods related to individual and team account configuration and monitoring.
 */
public class Accounts extends AbstractEndpoint {
    public Accounts(DataCenter dataCenter) {
        super(dataCenter);
    }

    public Accounts(String apiServer) {
        super(apiServer);
    }

    public Accounts(String username, String accessKey, DataCenter dataCenter) {
        super(username, accessKey, dataCenter);
    }

    public Accounts(String username, String accessKey, String apiServer) {
        super(username, accessKey, apiServer);
    }

    @Override
    protected String getBaseEndpoint() {
        return super.getBaseEndpoint() + "team-management/v1/";
    }

    /**
     * Queries the organization of the requesting account and returns the number of teams matching the query and a summary of each team, including the ID value, which may be a required parameter of other API calls related to a specific team.
     *
     * @return {@link LookupTeams}
     * @throws IOException API request failed
     */
    public LookupTeams lookupTeams() throws IOException {
        String url = getBaseEndpoint() + "teams/";

        return getResponseClass(getResponseObject(url), LookupTeams.class);
    }

    /**
     * @param name Returns the set of teams that begin with the specified name value. For example, name=sauce would return all teams in the organization with names beginning with "sauce".
     * @return {@link LookupTeams}
     * @throws IOException API request failed
     */
    public LookupTeams lookupTeams(String name) throws IOException {
        String url = getBaseEndpoint() + "teams?name=" + name;

        return getResponseClass(getResponseObject(url), LookupTeams.class);
    }

    /**
     * Returns the full profile of the specified team. The ID of the team is the only valid unique identifier.
     *
     * @param teamID The unique identifier of the team. You can look up the IDs of teams in your organization using the Lookup Teams ({@link LookupTeams}) endpoint.
     * @return {@link Team}
     * @throws IOException API request failed
     */
    public Team getSpecificTeam(String teamID) throws IOException {
        String url = getBaseEndpoint() + "teams/" + teamID;

        return getResponseClass(getResponseObject(url), Team.class);
    }
}
