
package com.saucelabs.saucerest.model.accounts;

import com.squareup.moshi.Json;

public class UpdateTeam {

    @Json(name = "id")
    public String id;
    @Json(name = "settings")
    public Settings settings;
    @Json(name = "created_at")
    public String createdAt;
    @Json(name = "description")
    public String description;
    @Json(name = "group")
    public Group group;
    @Json(name = "is_default")
    public Boolean isDefault;
    @Json(name = "name")
    public String name;
    @Json(name = "org_uuid")
    public String orgUuid;
    @Json(name = "updated_at")
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

}
