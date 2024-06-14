package com.saucelabs.saucerest.model.accounts;

import java.util.List;

public class Result {

    public String id;
    public Settings settings;
    public Group group;
    public Boolean isDefault;
    public String name;
    public String orgUuid;
    public Integer userCount;
    public String email;
    public String firstName;
    public String lastName;
    public Boolean isActive;
    public Organization organization;
    public List<Role> roles;
    public List<Team> teams;
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