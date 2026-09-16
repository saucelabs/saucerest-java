package com.saucelabs.saucerest.model.insights;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/** Query parameters for {@code GET /v2/insights/{source}/trends/tests}. */
public class TrendsTestsParameter {
    private final String orgId;
    private final Interval interval;
    private final String timeZone;
    private final String userId;
    private final String groupId;
    private final String teamId;
    private final String start;
    private final String end;
    private final String[] browser;
    private final String[] build;
    private final String[] device;
    private final String[] os;
    private final Status[] status;
    private final String[] tag;
    private final TagFilterMode tagFilterMode;
    private final String[] automationBackend;
    private final DeviceGroup deviceGroup;

    private TrendsTestsParameter(Builder builder) {
        orgId = builder.orgId;
        interval = builder.interval;
        timeZone = builder.timeZone;
        userId = builder.userId;
        groupId = builder.groupId;
        teamId = builder.teamId;
        start = builder.start;
        end = builder.end;
        browser = builder.browser;
        build = builder.build;
        device = builder.device;
        os = builder.os;
        status = builder.status;
        tag = builder.tag;
        tagFilterMode = builder.tagFilterMode;
        automationBackend = builder.automationBackend;
        deviceGroup = builder.deviceGroup;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> parameters = new HashMap<>();

        Stream.of(
                        new AbstractMap.SimpleEntry<>("org_id", orgId),
                        new AbstractMap.SimpleEntry<>("interval", interval == null ? null : interval.value),
                        new AbstractMap.SimpleEntry<>("time_zone", timeZone),
                        new AbstractMap.SimpleEntry<>("user_id", userId),
                        new AbstractMap.SimpleEntry<>("group_id", groupId),
                        new AbstractMap.SimpleEntry<>("team_id", teamId),
                        new AbstractMap.SimpleEntry<>("start", start),
                        new AbstractMap.SimpleEntry<>("end", end),
                        new AbstractMap.SimpleEntry<>("browser", browser),
                        new AbstractMap.SimpleEntry<>("build", build),
                        new AbstractMap.SimpleEntry<>("device", device),
                        new AbstractMap.SimpleEntry<>("os", os),
                        new AbstractMap.SimpleEntry<>("status", ParameterUtils.valuesOf(status, s -> s.value)),
                        new AbstractMap.SimpleEntry<>("tag", tag),
                        new AbstractMap.SimpleEntry<>("tag_filter_mode", tagFilterMode == null ? null : tagFilterMode.value),
                        new AbstractMap.SimpleEntry<>("automation_backend", automationBackend),
                        new AbstractMap.SimpleEntry<>("device_group", deviceGroup == null ? null : deviceGroup.value)
                )
                .filter(e -> e.getValue() != null)
                .forEach(e -> parameters.put(e.getKey(), e.getValue()));

        return parameters;
    }

    public static final class Builder {
        private final String orgId;
        private Interval interval;
        private String timeZone;
        private String userId;
        private String groupId;
        private String teamId;
        private String start;
        private String end;
        private String[] browser;
        private String[] build;
        private String[] device;
        private String[] os;
        private Status[] status;
        private String[] tag;
        private TagFilterMode tagFilterMode;
        private String[] automationBackend;
        private DeviceGroup deviceGroup;

        /** @param orgId The organization to return results for (required). */
        public Builder(String orgId) {
            this.orgId = orgId;
        }

        public Builder setInterval(Interval val) {
            interval = val;
            return this;
        }

        /** @param val UTC offset, e.g. {@code "+01:00"} (default: {@code "+00:00"}). */
        public Builder setTimeZone(String val) {
            timeZone = val;
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

        public Builder setAutomationBackend(String[] val) {
            automationBackend = val;
            return this;
        }

        public Builder setDeviceGroup(DeviceGroup val) {
            deviceGroup = val;
            return this;
        }

        public TrendsTestsParameter build() {
            return new TrendsTestsParameter(this);
        }
    }
}
