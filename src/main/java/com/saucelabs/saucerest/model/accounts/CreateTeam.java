
package com.saucelabs.saucerest.model.accounts;

import com.squareup.moshi.Json;

public class CreateTeam {

    @Json(name = "created_at")
    public String createdAt;
    @Json(name = "description")
    public String description;
    @Json(name = "group")
    public Group group;
    @Json(name = "id")
    public String id;
    @Json(name = "is_default")
    public Boolean isDefault;
    @Json(name = "name")
    public String name;
    @Json(name = "org_uuid")
    public String orgUuid;
    @Json(name = "settings")
    public Settings settings;
    @Json(name = "updated_at")
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
