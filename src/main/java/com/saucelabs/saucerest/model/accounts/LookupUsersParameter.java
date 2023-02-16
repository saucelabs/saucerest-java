package com.saucelabs.saucerest.model.accounts;

import java.util.HashMap;
import java.util.Map;

public class LookupUsersParameter {
    private final String username;
    private final String teams;
    private final String teamName;
    private final Integer roles;
    private final String phrase;
    private final String status;
    private final Integer limit;
    private final Integer offset;

    public enum Status {
        ACTIVE("active"),
        PENDING("pending"),
        INACTIVE("inactive");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public LookupUsersParameter(String username, String teams, String teamName, Integer roles, String phrase, String status, Integer limit, Integer offset) {
        this.username = username;
        this.teams = teams;
        this.teamName = teamName;
        this.roles = roles;
        this.phrase = phrase;
        this.status = status;
        this.limit = limit;
        this.offset = offset;
    }

    private LookupUsersParameter(Builder builder) {
        username = builder.username;
        teams = builder.teams;
        teamName = builder.teamName;
        roles = builder.roles;
        phrase = builder.phrase;
        status = builder.status;
        limit = builder.limit;
        offset = builder.offset;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> parameters = new HashMap<>();

        if (this.username != null) {
            parameters.put("username", this.username);
        }

        if (this.teams != null) {
            parameters.put("teams", this.teams);
        }

        if (this.teamName != null) {
            parameters.put("team_name", this.teamName);
        }

        if (this.roles != null) {
            parameters.put("roles", this.roles);
        }

        if (this.phrase != null) {
            parameters.put("phrase", this.phrase);
        }

        if (this.status != null) {
            parameters.put("status", this.status);
        }

        if (this.limit != null) {
            parameters.put("limit", this.limit);
        }

        if (this.offset != null) {
            parameters.put("offset", this.offset);
        }

        return parameters;
    }

    public static final class Builder {
        private String username;
        private String teams;
        private String teamName;
        private Integer roles;
        private String phrase;
        private String status;
        private Integer limit;
        private Integer offset;

        public Builder() {
        }

        public Builder setUsername(String val) {
            username = val;
            return this;
        }

        /**
         * Comma separated list of team ids
         */
        public Builder setTeams(String val) {
            teams = val;
            return this;
        }

        public Builder setTeams(String... val) {
            teams = String.join(",", val);
            return this;
        }

        public Builder setTeamName(String val) {
            teamName = val;
            return this;
        }

        public Builder setRoles(Roles val) {
            roles = val.getValue();
            return this;
        }

        public Builder setPhrase(String val) {
            phrase = val;
            return this;
        }

        public Builder setStatus(Status val) {
            status = val.value;
            return this;
        }

        public Builder setLimit(Integer val) {
            limit = val;
            return this;
        }

        public Builder setOffset(Integer val) {
            offset = val;
            return this;
        }

        public LookupUsersParameter build() {
            if (this.limit == null) {
                this.limit = 20;
            }

            if (this.limit > 100) {
                throw new IllegalArgumentException("Limit cannot be greater than 100");
            }

            return new LookupUsersParameter(this);
        }
    }
}