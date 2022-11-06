
package com.saucelabs.saucerest.model.storage.editfiledescription;

import com.saucelabs.saucerest.model.storage.Access;
import com.saucelabs.saucerest.model.storage.Owner;
import com.squareup.moshi.Json;

public class Item {

    @Json(name = "id")
    public String id;
    @Json(name = "owner")
    public Owner owner;
    @Json(name = "name")
    public String name;
    @Json(name = "upload_timestamp")
    public Integer uploadTimestamp;
    @Json(name = "etag")
    public String etag;
    @Json(name = "kind")
    public String kind;
    @Json(name = "group_id")
    public Integer groupId;
    @Json(name = "size")
    public Integer size;
    @Json(name = "description")
    public String description;
    @Json(name = "metadata")
    public Metadata metadata;
    @Json(name = "access")
    public Access access;
    @Json(name = "sha256")
    public String sha256;

    /**
     * No args constructor for use in serialization
     */
    public Item() {
    }

    /**
     * @param owner
     * @param metadata
     * @param access
     * @param size
     * @param sha256
     * @param uploadTimestamp
     * @param kind
     * @param groupId
     * @param name
     * @param description
     * @param etag
     * @param id
     */
    public Item(String id, Owner owner, String name, Integer uploadTimestamp, String etag, String kind, Integer groupId, Integer size, String description, Metadata metadata, Access access, String sha256) {
        super();
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.uploadTimestamp = uploadTimestamp;
        this.etag = etag;
        this.kind = kind;
        this.groupId = groupId;
        this.size = size;
        this.description = description;
        this.metadata = metadata;
        this.access = access;
        this.sha256 = sha256;
    }

}
