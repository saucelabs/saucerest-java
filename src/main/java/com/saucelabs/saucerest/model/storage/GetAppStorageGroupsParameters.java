package com.saucelabs.saucerest.model.storage;

import java.util.HashMap;
import java.util.Map;

public class GetAppStorageGroupsParameters {
    private final String q;
    private final String kind;
    private final String[] groupIds;
    private final int page;
    private String perPage;

    private GetAppStorageGroupsParameters(Builder builder) {
        q = builder.q;
        kind = builder.kind;
        groupIds = builder.groupIds;
        page = builder.page;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> parameters = new HashMap<>();

        if (this.q != null) {
            parameters.put("q", this.q);
        }

        if (this.kind != null) {
            parameters.put("kind", this.kind);
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
        private String kind;
        private String[] groupIds;
        private int page;

        public Builder() {
        }

        public Builder setQ(String val) {
            q = val;
            return this;
        }

        public Builder setKind(String val) {
            kind = val;
            return this;
        }

        public Builder setGroupIds(String[] val) {
            groupIds = val;
            return this;
        }

        public Builder setPage(int val) {
            page = val;
            return this;
        }

        public GetAppStorageGroupsParameters build() {
            return new GetAppStorageGroupsParameters(this);
        }
    }
}
