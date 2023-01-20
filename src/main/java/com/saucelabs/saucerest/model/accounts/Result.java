package com.saucelabs.saucerest.model.accounts;

import com.squareup.moshi.Json;

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

    /**
     * No args constructor for use in serialization
     */
    public Result() {
    }

    /**
     * @param settings
     * @param isDefault
     * @param userCount
     * @param name
     * @param id
     * @param group
     * @param orgUuid
     */
    public Result(String id, Settings settings, Group group, Boolean isDefault, String name, String orgUuid, Integer userCount) {
        super();
        this.id = id;
        this.settings = settings;
        this.group = group;
        this.isDefault = isDefault;
        this.name = name;
        this.orgUuid = orgUuid;
        this.userCount = userCount;
    }

}
