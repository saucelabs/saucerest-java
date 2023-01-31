package com.saucelabs.saucerest.model.accounts;

import com.squareup.moshi.Json;

import java.util.List;

public class Result {

    @Json(name = "id")
    public String id;
    @Json(name = "settings")
    public Settings settings;
    @Json(name = "group")
    public Group group;
    @Json(name = "is_default")
    public Boolean isDefault;
    @Json(name = "name")
    public String name;
    @Json(name = "org_uuid")
    public String orgUuid;
    @Json(name = "user_count")
    public Integer userCount;
    @Json(name = "email")
    public String email;
    @Json(name = "first_name")
    public String firstName;
    @Json(name = "last_name")
    public String lastName;
    @Json(name = "is_active")
    public Boolean isActive;
    @Json(name = "organization")
    public Organization organization;
    @Json(name = "roles")
    public List<Role> roles;
    @Json(name = "teams")
    public List<Team> teams;
    @Json(name = "username")
    public String username;

    /**
     * No args constructor for use in serialization
     */
    public Result() {
    }

    public Result(String id, Settings settings, Group group, Boolean isDefault, String name, String orgUuid, Integer userCount, String email, String firstName, String lastName, Boolean isActive, Organization organization, List<Role> roles, List<Team> teams, String username) {
        this.id = id;
        this.settings = settings;
        this.group = group;
        this.isDefault = isDefault;
        this.name = name;
        this.orgUuid = orgUuid;
        this.userCount = userCount;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.organization = organization;
        this.roles = roles;
        this.teams = teams;
        this.username = username;
    }
}