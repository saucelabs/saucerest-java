package com.saucelabs.saucerest.model.accounts;

import com.squareup.moshi.Json;

import java.util.List;

public class User {

    @Json(name = "id")
    public String id;
    @Json(name = "email")
    public String email;
    @Json(name = "first_name")
    public String firstName;
    @Json(name = "last_name")
    public String lastName;
    @Json(name = "username")
    public String username;
    @Json(name = "created_at")
    public String createdAt;
    @Json(name = "groups")
    public List<Group> groups;
    @Json(name = "is_active")
    public Boolean isActive;
    @Json(name = "is_organization_admin")
    public Boolean isOrganizationAdmin;
    @Json(name = "is_team_admin")
    public Boolean isTeamAdmin;
    @Json(name = "is_staff")
    public Boolean isStaff;
    @Json(name = "is_superuser")
    public Boolean isSuperuser;
    @Json(name = "organization")
    public Organization organization;
    @Json(name = "phone")
    public String phone;
    @Json(name = "roles")
    public List<Role> roles;
    @Json(name = "teams")
    public List<Team> teams;
    @Json(name = "updated_at")
    public String updatedAt;
    @Json(name = "user_type")
    public String userType;

    /**
     * No args constructor for use in serialization
     */
    public User() {
    }

    public User(String id, String email, String firstName, String lastName, String username, String createdAt, List<Group> groups, Boolean isActive, Boolean isOrganizationAdmin, Boolean isTeamAdmin, Boolean isStaff, Boolean isSuperuser, Organization organization, String phone, List<Role> roles, List<Team> teams, String updatedAt, String userType) {
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
    }
}