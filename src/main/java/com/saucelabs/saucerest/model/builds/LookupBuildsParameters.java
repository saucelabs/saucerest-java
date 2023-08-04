package com.saucelabs.saucerest.model.builds;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class LookupBuildsParameters {
    private final String userID;
    private final String orgID;
    private final String groupID;
    private final String teamID;
    private final Status[] status;
    private final Integer start;
    private final Integer end;
    private final Integer limit;
    private final String name;
    private final Integer offset;
    private final Sort sort;

    private LookupBuildsParameters(Builder builder) {
        userID = builder.userID;
        orgID = builder.orgID;
        groupID = builder.groupID;
        teamID = builder.teamID;
        status = builder.status;
        start = builder.start;
        end = builder.end;
        limit = builder.limit;
        name = builder.name;
        offset = builder.offset;
        sort = builder.sort;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> parameters = new HashMap<>();

        Stream.of(
                        new AbstractMap.SimpleEntry<>("user_id", userID),
                        new AbstractMap.SimpleEntry<>("org_id", orgID),
                        new AbstractMap.SimpleEntry<>("group_id", groupID),
                        new AbstractMap.SimpleEntry<>("team_id", teamID),
                        new AbstractMap.SimpleEntry<>("status", status),
                        new AbstractMap.SimpleEntry<>("start", start),
                        new AbstractMap.SimpleEntry<>("end", end),
                        new AbstractMap.SimpleEntry<>("limit", limit),
                        new AbstractMap.SimpleEntry<>("name", name),
                        new AbstractMap.SimpleEntry<>("offset", offset),
                        new AbstractMap.SimpleEntry<>("sort", sort == null ? null : sort.value)
                )
                .filter(e -> e.getValue() != null)
                .forEach(e -> parameters.put(e.getKey(), e.getValue()));

        return parameters;
    }

    /**
     * {@code LookupBuildsParameters} builder static inner class.
     */
    public static final class Builder {
        private String userID;
        private String orgID;
        private String groupID;
        private String teamID;
        private Status[] status;
        private Integer start;
        private Integer end;
        private Integer limit;
        private String name;
        private Integer offset;
        private Sort sort;

        public Builder() {
        }

        /**
         * Sets the {@code userID} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code userID} to set
         * @return a reference to this Builder
         */
        public Builder setUserID(String val) {
            userID = val;
            return this;
        }

        /**
         * Sets the {@code orgID} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code orgID} to set
         * @return a reference to this Builder
         */
        public Builder setOrgID(String val) {
            orgID = val;
            return this;
        }

        /**
         * Sets the {@code groupID} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code groupID} to set
         * @return a reference to this Builder
         */
        public Builder setGroupID(String val) {
            groupID = val;
            return this;
        }

        /**
         * Sets the {@code teamID} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code teamID} to set
         * @return a reference to this Builder
         */
        public Builder setTeamID(String val) {
            teamID = val;
            return this;
        }

        /**
         * Sets the {@code status} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code status} to set
         * @return a reference to this Builder
         */
        public Builder setStatus(Status[] val) {
            status = val;
            return this;
        }

        /**
         * Sets the {@code start} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code start} to set
         * @return a reference to this Builder
         */
        public Builder setStart(Integer val) {
            start = val;
            return this;
        }

        /**
         * Sets the {@code end} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code end} to set
         * @return a reference to this Builder
         */
        public Builder setEnd(Integer val) {
            end = val;
            return this;
        }

        /**
         * Sets the {@code limit} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code limit} to set
         * @return a reference to this Builder
         */
        public Builder setLimit(Integer val) {
            limit = val;
            return this;
        }

        /**
         * Sets the {@code name} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code name} to set
         * @return a reference to this Builder
         */
        public Builder setName(String val) {
            name = val;
            return this;
        }

        /**
         * Sets the {@code offset} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code offset} to set
         * @return a reference to this Builder
         */
        public Builder setOffset(Integer val) {
            offset = val;
            return this;
        }

        /**
         * Sets the {@code sort} and returns a reference to this Builder enabling method chaining.
         *
         * @param val the {@code sort} to set
         * @return a reference to this Builder
         */
        public Builder setSort(Sort val) {
            sort = val;
            return this;
        }

        /**
         * Returns a {@code LookupBuildsParameters} built from the parameters previously set.
         *
         * @return a {@code LookupBuildsParameters} built with parameters of this {@code LookupBuildsParameters.Builder}
         */
        public LookupBuildsParameters build() {
            return new LookupBuildsParameters(this);
        }
    }
}