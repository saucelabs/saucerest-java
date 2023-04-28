package com.saucelabs.saucerest.api;

import com.google.common.collect.ImmutableMap;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.HttpMethod;
import com.saucelabs.saucerest.model.accounts.*;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Account Management API Methods <br>
 * <p>
 * The Accounts API exposes the following methods related to individual and team account configuration and monitoring.
 */
public class AccountsEndpoint extends AbstractEndpoint {
    public AccountsEndpoint(DataCenter dataCenter) {
        super(dataCenter);
    }

    public AccountsEndpoint(String apiServer) {
        super(apiServer);
    }

    public AccountsEndpoint(String username, String accessKey, DataCenter dataCenter) {
        super(username, accessKey, dataCenter);
    }

    public AccountsEndpoint(String username, String accessKey, String apiServer) {
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

        return deserializeJSONObject(request(url, HttpMethod.GET), LookupTeams.class);
    }

    /**
     * @param name Returns the set of teams that begin with the specified name value. For example, name=sauce would return all teams in the organization with names beginning with "sauce".
     * @return {@link LookupTeams}
     * @throws IOException API request failed
     */
    public LookupTeams lookupTeams(String name) throws IOException {
        String url = getBaseEndpoint() + "teams?name=" + name;

        return deserializeJSONObject(request(url, HttpMethod.GET), LookupTeams.class);
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

        return deserializeJSONObject(request(url, HttpMethod.GET), Team.class);
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

        return deserializeJSONObject(request(url, HttpMethod.POST, map), CreateTeam.class);
    }

    public CreateTeam createTeam(String name, Integer VMConcurrency, String description) throws IOException {
        return createTeam(name, new Settings.Builder().setVirtualMachines(VMConcurrency).build(), description);
    }

    public Organizations getOrganization() throws IOException {
        String url = getBaseEndpoint() + "organizations";

        return deserializeJSONObject(request(url, HttpMethod.GET), Organizations.class);
    }

    /**
     * Deletes the specified team from the organization of the requesting account.
     *
     * @param teamID The unique identifier of the team. You can look up the IDs of teams in your organization using the {@link LookupTeams} endpoint.
     * @return
     * @throws IOException API request failed
     */
    public Response deleteTeam(String teamID) throws IOException {
        String url = getBaseEndpoint() + "teams/" + teamID;

        return request(url, HttpMethod.DELETE);
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

        return deserializeJSONObject(request(url, HttpMethod.PUT, map), UpdateTeam.class);
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

        return deserializeJSONObject(request(url, HttpMethod.PATCH, updateTeam.toJson()), UpdateTeam.class);
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

        return deserializeJSONObject(request(url, HttpMethod.GET), TeamMembers.class);
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

        return deserializeJSONArray(request(url, HttpMethod.POST, ""), ResetAccessKeyForTeam.class);
    }

    /**
     * Queries the organization of the requesting account and returns the number of users matching the query and a basic profile of each user, including the ID value, which may be a required parameter of other API calls related to a specific user.
     *
     * @return {@link LookupUsers}
     * @throws IOException API request failed
     */
    public LookupUsers lookupUsers() throws IOException {
        String url = getBaseEndpoint() + "users/";

        return deserializeJSONObject(request(url, HttpMethod.GET), LookupUsers.class);
    }

    /**
     * Queries the organization of the requesting account and returns the number of users matching the query and a basic profile of each user, including the ID value, which may be a required parameter of other API calls related to a specific user.
     *
     * @param lookupUsersParameter {@link LookupUsersParameter}
     * @return {@link LookupUsers}
     * @throws IOException API request failed
     */
    public LookupUsers lookupUsers(LookupUsersParameter lookupUsersParameter) throws IOException {
        String url = getBaseEndpoint() + "users/";

        return deserializeJSONObject(requestWithQueryParameters(url, HttpMethod.GET, lookupUsersParameter.toMap()), LookupUsers.class);
    }

    /**
     * Returns the full profile of the specified user. The ID of the user is the only valid unique identifier.
     *
     * @param userID The user's unique identifier. You can look up the IDs of users in your organization using the {@link LookupUsers} endpoint.
     * @return {@link User}
     * @throws IOException API request failed
     */
    public User getUser(String userID) throws IOException {
        String url = getBaseEndpoint() + "users/" + userID;

        return deserializeJSONObject(request(url, HttpMethod.GET), User.class);
    }

    /**
     * Creates a new user in the Sauce Labs platform.
     *
     * @param createUser {@link CreateUser}
     * @return {@link User}
     * @throws IOException API request failed
     */
    public User createUser(CreateUser createUser) throws IOException {
        String url = getBaseEndpoint() + "users/";

        return deserializeJSONObject(request(url, HttpMethod.POST, createUser.toMap()), User.class);
    }

    /**
     * Replaces all values of the specified user profile with the new set of parameters passed in the request. To update only certain parameters, see Partially Update a User.
     *
     * @param updateUser {@link UpdateUser}
     * @return {@link User}
     * @throws IOException API request failed
     */
    public User updateUser(UpdateUser updateUser) throws IOException {
        String url = getBaseEndpoint() + "users/" + updateUser.userID;

        return deserializeJSONObject(request(url, HttpMethod.PUT, updateUser.toMap()), User.class);
    }

    /**
     * Allows you to update individual user values without replacing the entire profile.
     *
     * @param updateUser {@link UpdateUser}
     * @return {@link User}
     * @throws IOException API request failed
     */
    public User partiallyUpdateUser(UpdateUser updateUser) throws IOException {
        String url = getBaseEndpoint() + "users/" + updateUser.userID;

        return deserializeJSONObject(request(url, HttpMethod.PATCH, updateUser.toMap()), User.class);
    }

    /**
     * Returns details about the current in-use virtual machines and real devices along with the maximum allowed values. <br> <br>
     * NOTE:
     * At this time, the current usage for real devices is not accurately returned in the response. As a workaround, use the following endpoint: {@link RealDevicesEndpoint#getConcurrency()}
     *
     * @param username The username of the user whose concurrency you are looking up. You can look up a user's name using a variety of filtering parameters with the {@link LookupUsers} endpoint.
     * @return {@link UserConcurrency}
     * @throws IOException API request failed
     */
    public UserConcurrency getUserConcurrency(String username) throws IOException {
        String url = super.getBaseEndpoint() + "rest/v1.2/users/" + username + "/concurrency";

        return deserializeJSONObject(request(url, HttpMethod.GET), UserConcurrency.class);
    }

    /**
     * Returns the number of teams a user belongs to and provides information about each team, including whether it is the default and its concurrency settings.
     *
     * @param userID The unique identifier of the user. You can look up a user's ID using the {@link LookupUsers} endpoint.
     * @return {@link UsersTeam}
     * @throws IOException API request failed
     */
    public UsersTeam getUsersTeam(String userID) throws IOException {
        String url = getBaseEndpoint() + "users/" + userID + "/teams/";

        return deserializeJSONObject(request(url, HttpMethod.GET), UsersTeam.class);
    }

    /**
     * Set a user's team affiliation. Users are limited to one team affiliation, so if the user is already a member of a different team, this call will remove them from that team.
     * Also, By default, the user will not have team-admin privileges, even if they did on a prior team.
     *
     * @param userID The unique identifier of the Sauce Labs user to be added to the team.You can look up the ID of a user in your organization using the {@link LookupUsers} endpoint.
     * @param teamID The identifier of the team to which the user will be added. You can look up the ID of a team in your organization using the {@link LookupTeams} endpoint.
     * @return {@link User}
     * @throws IOException API request failed
     */
    public SetTeam setUsersTeam(String userID, String teamID) throws IOException {
        String url = getBaseEndpoint() + "membership";

        Map map = ImmutableMap.of("user", userID, "team", teamID);

        return deserializeJSONObject(request(url, HttpMethod.POST, map), SetTeam.class);
    }

    /**
     * Assigns administrator rights to the user within their organization. Organization Admins automatically have Team Admin rights in all the teams in the Organization.
     *
     * @param userID The unique identifier of the user. You can look up a user's ID using the {@link LookupUsers} endpoint.
     * @return {@link User}
     * @throws IOException API request failed
     */
    public User setAdmin(String userID) throws IOException {
        String url = getBaseEndpoint() + "users/" + userID + "/set-admin";

        return deserializeJSONObject(request(url, HttpMethod.POST), User.class);
    }

    /**
     * Assigns team administrator rights to the user within their current team. If the user is currently assigned an Org Admin role, this call would reduce the rights to only those of a Team Admin.
     *
     * @param userID The unique identifier of the user. You can look up a user's ID using the {@link LookupUsers} endpoint.
     * @return {@link User}
     * @throws IOException API request failed
     */
    public User setTeamAdmin(String userID) throws IOException {
        String url = getBaseEndpoint() + "users/" + userID + "/set-team-admin";

        return deserializeJSONObject(request(url, HttpMethod.POST), User.class);
    }

    /**
     * Assigns the member role to the user. If the user is currently assigned any Admin rights, this call removes those rights.
     *
     * @param userID The unique identifier of the user. You can look up a user's ID using the {@link LookupUsers} endpoint.
     * @return {@link User}
     * @throws IOException API request failed
     */
    public User setMember(String userID) throws IOException {
        String url = getBaseEndpoint() + "users/" + userID + "/set-member";

        return deserializeJSONObject(request(url, HttpMethod.POST), User.class);
    }

    /**
     * Retrieves the Sauce Labs access key for the specified user.
     *
     * @param userID The unique identifier of the user. You can look up a user's ID using the {@link LookupUsers} endpoint.
     * @return {@link User}
     * @throws IOException API request failed
     */
    public User getAccessKey(String userID) throws IOException {
        String url = getBaseEndpoint() + "users/" + userID + "/access-key";

        return deserializeJSONObject(request(url, HttpMethod.GET), User.class);
    }

    /**
     * Creates a new auto-generated access key for the specified user. <br> <br>
     * Regenerating an access key invalidates the previous value and any tests containing the prior value will fail, so make sure you update any tests and credential environment variables with the new value.
     *
     * @param userID The unique identifier of the user. You can look up a user's ID using the {@link LookupUsers} endpoint.
     * @return {@link User}
     * @throws IOException API request failed
     */
    public List<User> resetAccessKey(String userID) throws IOException {
        String url = getBaseEndpoint() + "users/" + userID + "/reset-access-key";

        return deserializeJSONArray(request(url, HttpMethod.POST), User.class);
    }

    /**
     * Suspends the specified user's account, preventing all access to Sauce Labs while deactivated.
     *
     * @param userID The unique identifier of the user. You can look up a user's ID using the {@link LookupUsers} endpoint.
     * @return {@link User}
     * @throws IOException API request failed
     */
    public User deactivateUser(String userID) throws IOException {
        String url = getBaseEndpoint() + "users/" + userID + "/deactivate";

        return deserializeJSONObject(request(url, HttpMethod.POST), User.class);
    }

    /**
     * Re-activates the specified user's account, if it had been previously deactivated.
     *
     * @param userID The unique identifier of the user. You can look up a user's ID using the {@link LookupUsers} endpoint.
     * @return {@link User}
     * @throws IOException API request failed
     */
    public User activateUser(String userID) throws IOException {
        String url = getBaseEndpoint() + "users/" + userID + "/activate";

        return deserializeJSONObject(request(url, HttpMethod.POST), User.class);
    }
}