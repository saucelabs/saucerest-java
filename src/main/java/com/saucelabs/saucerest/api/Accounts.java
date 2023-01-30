package com.saucelabs.saucerest.api;

import com.google.common.collect.ImmutableMap;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.model.accounts.CreateTeam;
import com.saucelabs.saucerest.model.accounts.LookupTeams;
import com.saucelabs.saucerest.model.accounts.Settings;
import com.saucelabs.saucerest.model.accounts.Team;

import java.io.IOException;
import java.util.Map;

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

    /**
     * Creates a new team under the organization of the requesting account.
     *
     * @param name           A name for the new team.
     * @param organizationID The unique ID of the organization under which the team is created. You can look up your organization ID by calling the GET https://api.{region}.saucelabs.com/team-management/v1/organizations/ endpoint.
     * @param settings       The settings object specifies the concurrency allocations for the team within the organization. The available attributes are:
     *                       virtual_machines - INTEGER
     *                       The settings parameter is required, but you only need to include the applicable concurrency attribute(s) for the team.
     * @return {@link CreateTeam}
     * @throws IOException API request failed
     */
    public CreateTeam createTeam(String name, String organizationID, Settings settings) throws IOException {
        return createTeam(name, organizationID, settings, null);
    }

    /**
     * Creates a new team under the organization of the requesting account.
     *
     * @param name           A name for the new team.
     * @param organizationID The unique ID of the organization under which the team is created. You can look up your organization ID by calling the GET https://api.{region}.saucelabs.com/team-management/v1/organizations/ endpoint.
     * @param settings       The settings object specifies the concurrency allocations for the team within the organization. The available attributes are:
     *                       virtual_machines - INTEGER
     *                       The settings parameter is required, but you only need to include the applicable concurrency attribute(s) for the team.
     * @param description    A description to distinguish the team within the organization.
     * @return {@link CreateTeam}
     * @throws IOException API request failed
     */
    public CreateTeam createTeam(String name, String organizationID, Settings settings, String description) throws IOException {
        String url = getBaseEndpoint() + "teams/";
        Map map;

        if (description == null) {
            map = ImmutableMap.of("name", name, "organization", organizationID, "settings", settings);
        } else {
            map = ImmutableMap.of("name", name, "organization", organizationID, "settings", settings, "description", description);
        }

        return getResponseClass(postResponse(url, map), CreateTeam.class);
    }
}
