package com.saucelabs.saucerest.model.storage;

public class Recent {

    public String id;
    public Owner owner;
    public String name;
    public Integer uploadTimestamp;
    public String etag;
    public String kind;
    public Integer groupId;
    public Integer size;
    public String description;
    public Metadata metadata;
    public Access access;
    public String sha256;
    public String[] tags;

    public Recent() {
    }

    public Recent(String id, Owner owner, String name, Integer uploadTimestamp, String etag, String kind, Integer groupId, Integer size, String description, Metadata metadata, Access access, String sha256, String[] tags) {
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
        this.tags = tags;
    }
}