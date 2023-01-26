
package com.saucelabs.saucerest.model.accounts;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class CreateTeam {

    @com.squareup.moshi.Json(name = "created_at")
    public String createdAt;
    @com.squareup.moshi.Json(name = "description")
    public String description;
    @com.squareup.moshi.Json(name = "group")
    public Group group;
    @com.squareup.moshi.Json(name = "id")
    public String id;
    @com.squareup.moshi.Json(name = "is_default")
    public Boolean isDefault;
    @com.squareup.moshi.Json(name = "name")
    public String name;
    @com.squareup.moshi.Json(name = "org_uuid")
    public String orgUuid;
    @com.squareup.moshi.Json(name = "settings")
    public Settings settings;
    @com.squareup.moshi.Json(name = "updated_at")
    public String updatedAt;

    /**
     * No args constructor for use in serialization
     */
    public CreateTeam() {
    }

    /**
     * @param createdAt
     * @param settings
     * @param isDefault
     * @param name
     * @param description
     * @param id
     * @param group
     * @param orgUuid
     * @param updatedAt
     */
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
