package com.saucelabs.saucerest.model.insights;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/** Query parameters for {@code GET /v2/insights/{source}/test-cases}. */
public class TestCasesParameter {
    private final String orgId;
    private final String userId;
    private final String groupId;
    private final String teamId;
    private final Interval interval;
    private final String start;
    private final String end;
    private final String[] browser;
    private final String[] build;
    private final String[] device;
    private final String[] os;
    private final Status[] status;
    private final String[] tag;
    private final TagFilterMode tagFilterMode;
    private final Integer limit;
    private final Integer offset;
    private final TestCasesSortBy sortBy;
    private final Sort sort;
    private final String[] automationBackend;
    private final DeviceGroup deviceGroup;

    private TestCasesParameter(Builder builder) {
        orgId = builder.orgId;
        userId = builder.userId;
        groupId = builder.groupId;
        teamId = builder.teamId;
        interval = builder.interval;
        start = builder.start;
        end = builder.end;
        browser = builder.browser;
        build = builder.build;
        device = builder.device;
        os = builder.os;
        status = builder.status;
        tag = builder.tag;
        tagFilterMode = builder.tagFilterMode;
        limit = builder.limit;
        offset = builder.offset;
        sortBy = builder.sortBy;
        sort = builder.sort;
        automationBackend = builder.automationBackend;
        deviceGroup = builder.deviceGroup;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> parameters = new HashMap<>();

        Stream.of(
                        new AbstractMap.SimpleEntry<>("org_id", orgId),
                        new AbstractMap.SimpleEntry<>("user_id", userId),
                        new AbstractMap.SimpleEntry<>("group_id", groupId),
                        new AbstractMap.SimpleEntry<>("team_id", teamId),
                        new AbstractMap.SimpleEntry<>("interval", interval == null ? null : interval.value),
                        new AbstractMap.SimpleEntry<>("start", start),
                        new AbstractMap.SimpleEntry<>("end", end),
                        new AbstractMap.SimpleEntry<>("browser", browser),
                        new AbstractMap.SimpleEntry<>("build", build),
                        new AbstractMap.SimpleEntry<>("device", device),
                        new AbstractMap.SimpleEntry<>("os", os),
                        new AbstractMap.SimpleEntry<>("status", ParameterUtils.valuesOf(status, s -> s.value)),
                        new AbstractMap.SimpleEntry<>("tag", tag),
                        new AbstractMap.SimpleEntry<>("tag_filter_mode", tagFilterMode == null ? null : tagFilterMode.value),
                        new AbstractMap.SimpleEntry<>("limit", limit),
                        new AbstractMap.SimpleEntry<>("offset", offset),
                        new AbstractMap.SimpleEntry<>("sort_by", sortBy == null ? null : sortBy.value),
                        new AbstractMap.SimpleEntry<>("sort", sort == null ? null : sort.value),
                        new AbstractMap.SimpleEntry<>("automation_backend", automationBackend),
                        new AbstractMap.SimpleEntry<>("device_group", deviceGroup == null ? null : deviceGroup.value)
                )
                .filter(e -> e.getValue() != null)
                .forEach(e -> parameters.put(e.getKey(), e.getValue()));

        return parameters;
    }

    public static final class Builder {
        private final String orgId;
        private String userId;
        private String groupId;
        private String teamId;
        private Interval interval;
        private String start;
        private String end;
        private String[] browser;
        private String[] build;
        private String[] device;
        private String[] os;
        private Status[] status;
        private String[] tag;
        private TagFilterMode tagFilterMode;
        private Integer limit;
        private Integer offset;
        private TestCasesSortBy sortBy;
        private Sort sort;
        private String[] automationBackend;
        private DeviceGroup deviceGroup;

        /** @param orgId The organization to return results for (required). */
        public Builder(String orgId) {
            this.orgId = orgId;
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

        public Builder setInterval(Interval val) {
            interval = val;
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

        public Builder setBrowser(String[] val) {
            browser = val;
            return this;
        }

        public Builder setBuild(String[] val) {
            build = val;
            return this;
        }

        public Builder setDevice(String[] val) {
            device = val;
            return this;
        }

        public Builder setOs(String[] val) {
            os = val;
            return this;
        }

        public Builder setStatus(Status[] val) {
            status = val;
            return this;
        }

        public Builder setTag(String[] val) {
            tag = val;
            return this;
        }

        public Builder setTagFilterMode(TagFilterMode val) {
            tagFilterMode = val;
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

        public Builder setSortBy(TestCasesSortBy val) {
            sortBy = val;
            return this;
        }

        public Builder setSort(Sort val) {
            sort = val;
            return this;
        }

        public Builder setAutomationBackend(String[] val) {
            automationBackend = val;
            return this;
        }

        public Builder setDeviceGroup(DeviceGroup val) {
            deviceGroup = val;
            return this;
        }

        public TestCasesParameter build() {
            return new TestCasesParameter(this);
        }
    }
}
