package com.saucelabs.saucerest.model.accounts;

public class Team {

    public String id;
    public Settings settings;
    public String createdAt;
    public String description;
    public Group group;
    public Boolean isDefault;
    public String name;
    public String orgUuid;
    public String updatedAt;
    public Current current;
    public Allowed allowed;

    /**
     * No args constructor for use in serialization
     */
    public Team() {
    }

    /**
     * @param settings
     * @param createdAt
     * @param isDefault
     * @param name
     * @param description
     * @param id
     * @param group
     * @param orgUuid
     * @param updatedAt
     */
    public Team(String id, Settings settings, String createdAt, String description, Group group, Boolean isDefault, String name, String orgUuid, String updatedAt, Current current, Allowed allowed) {
        super();
        this.id = id;
        this.settings = settings;
        this.createdAt = createdAt;
        this.description = description;
        this.group = group;
        this.isDefault = isDefault;
        this.name = name;
        this.orgUuid = orgUuid;
        this.updatedAt = updatedAt;
        this.current = current;
        this.allowed = allowed;
    }
}