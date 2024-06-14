package com.saucelabs.saucerest.model.accounts;

public class UpdateTeam {

    public String id;
    public Settings settings;
    public String createdAt;
    public String description;
    public Group group;
    public Boolean isDefault;
    public String name;
    public String orgUuid;
    public String updatedAt;

    /**
     * No args constructor for use in serialization
     */
    public UpdateTeam() {
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
    public UpdateTeam(String id, Settings settings, String createdAt, String description, Group group, Boolean isDefault, String name, String orgUuid, String updatedAt) {
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
    }

    private UpdateTeam(Builder builder) {
        settings = builder.settings;
        description = builder.description;
        name = builder.name;
    }

    public static final class Builder {
        private Settings settings;
        private String description;
        private String name;

        public Builder setSettings(Settings val) {
            settings = val;
            return this;
        }

        public Builder setDescription(String val) {
            description = val;
            return this;
        }

        public Builder setName(String val) {
            name = val;
            return this;
        }

        public UpdateTeam build() {
            return new UpdateTeam(this);
        }
    }
}