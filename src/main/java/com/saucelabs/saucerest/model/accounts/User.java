package com.saucelabs.saucerest.model.accounts;

import java.util.List;

public class User {

    public String id;
    public String email;
    public String firstName;
    public String lastName;
    public String username;
    public String createdAt;
    public List<Group> groups;
    public Boolean isActive;
    public Boolean isOrganizationAdmin;
    public Boolean isTeamAdmin;
    public Boolean isStaff;
    public Boolean isSuperuser;
    public Organization organization;
    public String phone;
    public List<Role> roles;
    public List<Team> teams;
    public String updatedAt;
    public String userType;
    public String accessKey;

    /**
     * No args constructor for use in serialization
     */
    public User() {
    }

    public User(String id, String email, String firstName, String lastName, String username, String createdAt, List<Group> groups, Boolean isActive, Boolean isOrganizationAdmin, Boolean isTeamAdmin, Boolean isStaff, Boolean isSuperuser, Organization organization, String phone, List<Role> roles, List<Team> teams, String updatedAt, String userType, String accessKey) {
        super();
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.createdAt = createdAt;
        this.groups = groups;
        this.isActive = isActive;
        this.isOrganizationAdmin = isOrganizationAdmin;
        this.isTeamAdmin = isTeamAdmin;
        this.isStaff = isStaff;
        this.isSuperuser = isSuperuser;
        this.organization = organization;
        this.phone = phone;
        this.roles = roles;
        this.teams = teams;
        this.updatedAt = updatedAt;
        this.userType = userType;
        this.accessKey = accessKey;
    }
}