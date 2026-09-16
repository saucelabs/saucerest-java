package com.saucelabs.saucerest.model.insights;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/** Query parameters for {@code GET /v2/insights/{source}/errors}. */
public class ErrorsParameter {
    private final String orgId;
    private final Interval interval;
    private final String userId;
    private final String groupId;
    private final String teamId;
    private final String[] build;
    private final String[] os;
    private final String[] device;
    private final String start;
    private final String end;
    private final Integer limit;
    private final Integer offset;
    private final String[] automationBackend;

    private ErrorsParameter(Builder builder) {
        orgId = builder.orgId;
        interval = builder.interval;
        userId = builder.userId;
        groupId = builder.groupId;
        teamId = builder.teamId;
        build = builder.build;
        os = builder.os;
        device = builder.device;
        start = builder.start;
        end = builder.end;
        limit = builder.limit;
        offset = builder.offset;
        automationBackend = builder.automationBackend;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> parameters = new HashMap<>();

        Stream.of(
                        new AbstractMap.SimpleEntry<>("org_id", orgId),
                        new AbstractMap.SimpleEntry<>("interval", interval == null ? null : interval.value),
                        new AbstractMap.SimpleEntry<>("user_id", userId),
                        new AbstractMap.SimpleEntry<>("group_id", groupId),
                        new AbstractMap.SimpleEntry<>("team_id", teamId),
                        new AbstractMap.SimpleEntry<>("build", build),
                        new AbstractMap.SimpleEntry<>("os", os),
                        new AbstractMap.SimpleEntry<>("device", device),
                        new AbstractMap.SimpleEntry<>("start", start),
                        new AbstractMap.SimpleEntry<>("end", end),
                        new AbstractMap.SimpleEntry<>("limit", limit),
                        new AbstractMap.SimpleEntry<>("offset", offset),
                        new AbstractMap.SimpleEntry<>("automation_backend", automationBackend)
                )
                .filter(e -> e.getValue() != null)
                .forEach(e -> parameters.put(e.getKey(), e.getValue()));

        return parameters;
    }

    public static final class Builder {
        private final String orgId;
        private Interval interval;
        private String userId;
        private String groupId;
        private String teamId;
        private String[] build;
        private String[] os;
        private String[] device;
        private String start;
        private String end;
        private Integer limit;
        private Integer offset;
        private String[] automationBackend;

        /** @param orgId The organization to return results for (required). */
        public Builder(String orgId) {
            this.orgId = orgId;
        }

        public Builder setInterval(Interval val) {
            interval = val;
            return this;
        }

        public Builder setUserId(String val) {
            userId = val;
            return this;
        }

        public Builder setGroupId(String val) {
            groupId = val;
            return this;
        }

        public Builder setTeamId(String val) {
            teamId = val;
            return this;
        }

        public Builder setBuild(String[] val) {
            build = val;
            return this;
        }

        public Builder setOs(String[] val) {
            os = val;
            return this;
        }

        public Builder setDevice(String[] val) {
            device = val;
            return this;
        }

        public Builder setStart(LocalDateTime val) {
            start = DateTimeUtils.toUtcString(val);
            return this;
        }

        public Builder setEnd(LocalDateTime val) {
            end = DateTimeUtils.toUtcString(val);
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

        public Builder setAutomationBackend(String[] val) {
            automationBackend = val;
            return this;
        }

        public ErrorsParameter build() {
            return new ErrorsParameter(this);
        }
    }
}
