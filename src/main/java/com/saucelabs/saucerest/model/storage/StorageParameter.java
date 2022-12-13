package com.saucelabs.saucerest.model.storage;

import java.util.HashMap;
import java.util.Map;

public class StorageParameter {
    private final String q;
    private final String name;
    private final String[] kind;
    private final String[] fileId;
    private final String sha256;
    private final String[] teamId;
    private final String[] orgId;
    private final int page;
    private final String perPage;
    private final String[] groupIds;

    private StorageParameter(Builder builder) {
        q = builder.q;
        name = builder.name;
        kind = builder.kind;
        fileId = builder.fileId;
        sha256 = builder.sha256;
        teamId = builder.teamId;
        orgId = builder.orgId;
        page = builder.page;
        perPage = builder.perPage;
        groupIds = builder.groupIds;
    }

    /**
     * @return A map to be used as parameter when using an endpoint that takes query parameters.
     */
    public Map<String, Object> toMap() {
        Map<String, Object> parameters = new HashMap<>();

        if (this.q != null) {
            parameters.put("q", this.q);
        }

        if (this.name != null) {
            parameters.put("name", this.name);
        }

        if (this.kind != null) {
            parameters.put("kind", this.kind);
        }

        if (this.fileId != null) {
            parameters.put("file_id", this.fileId);
        }

        if (this.sha256 != null) {
            parameters.put("sha256", this.sha256);
        }

        if (this.teamId != null) {
            parameters.put("team_id", this.teamId);
        }

        if (this.orgId != null) {
            parameters.put("org_id", this.orgId);
        }

        // Default is returning results on page 1. Set to 1 in build()
        if (this.page != 0 && this.page != 1) {
            parameters.put("page", this.page);
        }

        if (this.perPage != null) {
            parameters.put("per_page", this.perPage);
        }

        if (this.groupIds != null) {
            parameters.put("group_id", this.groupIds);
        }

        return parameters;
    }

    public static final class Builder {
        private String q;
        private String name;
        private String[] kind;
        private String[] fileId;
        private String sha256;
        private String[] teamId;
        private String[] orgId;
        private int page;
        private String perPage;
        private String[] groupIds;

        public Builder() {
        }

        public Builder setQ(String val) {
            q = val;
            return this;
        }

        public Builder setName(String val) {
            name = val;
            return this;
        }

        public Builder setKind(String[] val) {
            kind = val;
            return this;
        }

        public Builder setFileId(String[] val) {
            fileId = val;
            return this;
        }

        public Builder setSha256(String val) {
            sha256 = val;
            return this;
        }

        public Builder setTeamId(String[] val) {
            teamId = val;
            return this;
        }

        public Builder setOrgId(String[] val) {
            orgId = val;
            return this;
        }

        public Builder setPage(int val) {
            page = val;
            return this;
        }

        public Builder setPerPage(String val) {
            perPage = val;
            return this;
        }

        public Builder setGroupIds(String[] val) {
            groupIds = val;
            return this;
        }

        public StorageParameter build() {
            if (page == 0) {
                page = 1;
            }

            return new StorageParameter(this);
        }
    }
}
