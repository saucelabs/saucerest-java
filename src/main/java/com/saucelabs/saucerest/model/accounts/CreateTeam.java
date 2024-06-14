package com.saucelabs.saucerest.model.accounts;

public class CreateTeam {

    public String createdAt;
    public String description;
    public Group group;
    public String id;
    public Boolean isDefault;
    public String name;
    public String orgUuid;
    public Settings settings;
    public String updatedAt;

    /**
     * No args constructor for use in serialization
     */
    public CreateTeam() {
    }

    public CreateTeam(String createdAt, String description, Group group, String id, Boolean isDefault, String name, String orgUuid, Settings settings, String updatedAt) {
        super();
        this.createdAt = createdAt;
        this.description = description;
        this.group = group;
        this.id = id;
        this.isDefault = isDefault;
        this.name = name;
        this.orgUuid = orgUuid;
        this.settings = settings;
        this.updatedAt = updatedAt;
    }
}