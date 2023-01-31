package com.saucelabs.saucerest.api;

import com.google.common.collect.ImmutableMap;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.model.accounts.*;

import java.io.IOException;
import java.util.List;
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
     * @param name        A name for the new team.
     * @param settings    The settings object specifies the concurrency allocations for the team within the organization. The available attributes are:
     *                    virtual_machines - INTEGER
     *                    The settings parameter is required, but you only need to include the applicable concurrency attribute(s) for the team.
     * @param description A description to distinguish the team within the organization.
     * @return {@link CreateTeam}
     * @throws IOException API request failed
     */
    public CreateTeam createTeam(String name, Settings settings, String description) throws IOException {
        String url = getBaseEndpoint() + "teams/";
        Map map = ImmutableMap.of("name", name, "settings", settings, "description", description);

        return getResponseClass(postResponse(url, map), CreateTeam.class);
    }

    public Organizations getOrganization() throws IOException {
        String url = getBaseEndpoint() + "organizations";

        return getResponseClass(getResponseObject(url), Organizations.class);
    }

    /**
     * Deletes the specified team from the organization of the requesting account.
     *
     * @param teamID The unique identifier of the team. You can look up the IDs of teams in your organization using the {@link LookupTeams} endpoint.
     * @throws IOException API request failed
     */
    public void deleteTeam(String teamID) throws IOException {
        String url = getBaseEndpoint() + "teams/" + teamID;

        deleteResponse(url);
    }

    /**
     * Replaces all values of the specified team with the new set of parameters passed in the request. To update only certain parameters, see Partially Update Team.
     *
     * @param teamID      The unique identifier of the team. You can look up the IDs of teams in your organization using the {@link LookupTeams} endpoint.
     * @param name        The name of the team as it will be after the update. Pass the current value to keep the name unchanged.
     * @param settings    The updated concurrency allocations for the team. The available attributes are:
     *                    virtual_machines - INTEGER
     *                    The settings parameter is required, but you only need to include the applicable concurrency attribute(s) for the team.
     * @param description A description to distinguish the team within the organization. If the previous team definition included a description, omitting the parameter in the update will delete it from the team record.
     * @return {@link UpdateTeam}
     * @throws IOException API request failed
     */
    public UpdateTeam updateTeam(String teamID, String name, Settings settings, String description) throws IOException {
        String url = getBaseEndpoint() + "teams/" + teamID;
        Map map = ImmutableMap.of("name", name, "settings", settings, "description", description);

        return getResponseClass(putResponse(url, map), UpdateTeam.class);
    }

    /**
     * Updates one or more individual editable parameters (such as the concurrency allocation) of the specified team without requiring a full profile update.
     *
     * @param teamID     The unique identifier of the team. You can look up the ID of teams in your organization using the {@link LookupTeams} endpoint.
     * @param updateTeam The {@link UpdateTeam} object containing the parameters to update.
     * @return {@link UpdateTeam}
     * @throws IOException API request failed
     */
    public UpdateTeam partiallyUpdateTeam(String teamID, UpdateTeam updateTeam) throws IOException {
        String url = getBaseEndpoint() + "teams/" + teamID;

        return getResponseClass(patchResponse(url, updateTeam.toJson()), UpdateTeam.class);
    }

    /**
     * Returns the number of members in the specified team and lists each member.
     *
     * @param teamID Identifies the team for which you are requesting the list of members.
     * @return {@link TeamMembers}
     * @throws IOException API request failed
     */
    public TeamMembers getTeamMembers(String teamID) throws IOException {
        String url = getBaseEndpoint() + "teams/" + teamID + "/members";

        return getResponseClass(getResponseObject(url), TeamMembers.class);
    }

    /**
     * Globally regenerates new access key values for every member of the specified team.
     *
     * @param teamID Identifies the team for which you are resetting member access keys.
     * @return {@link ResetAccessKeyForTeam}
     * @throws IOException API request failed
     */
    public List<ResetAccessKeyForTeam> resetAccessKeyForTeam(String teamID) throws IOException {
        String url = getBaseEndpoint() + "teams/" + teamID + "/reset-access-key";

        return getResponseListClass(postResponse(url), ResetAccessKeyForTeam.class);
    }
}