package com.saucelabs.saucerest.model.insights;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/** Query parameters for {@code GET /v1/analytics/trends/tests}. */
public class AnalyticsTrendsParameter {
    private final String start;
    private final String end;
    private final TimeRange timeRange;
    private final Scope scope;
    private final Interval interval;
    private final String[] browser;
    private final String[] build;
    private final String[] device;
    private final String[] os;
    private final TestResultParameter.Status status;
    private final String[] tag;
    private final TagFilterMode tagFilterMode;

    private AnalyticsTrendsParameter(Builder builder) {
        start = builder.start;
        end = builder.end;
        timeRange = builder.timeRange;
        scope = builder.scope;
        interval = builder.interval;
        browser = builder.browser;
        build = builder.build;
        device = builder.device;
        os = builder.os;
        status = builder.status;
        tag = builder.tag;
        tagFilterMode = builder.tagFilterMode;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> parameters = new HashMap<>();

        Stream.of(
                        new AbstractMap.SimpleEntry<>("start", start),
                        new AbstractMap.SimpleEntry<>("end", end),
                        new AbstractMap.SimpleEntry<>("time_range", timeRange == null ? null : timeRange.toString()),
                        new AbstractMap.SimpleEntry<>("scope", scope == null ? null : scope.value),
                        new AbstractMap.SimpleEntry<>("interval", interval == null ? null : interval.value),
                        new AbstractMap.SimpleEntry<>("browser", browser),
                        new AbstractMap.SimpleEntry<>("build", build),
                        new AbstractMap.SimpleEntry<>("device", device),
                        new AbstractMap.SimpleEntry<>("os", os),
                        new AbstractMap.SimpleEntry<>("status", status == null ? null : status.getValue()),
                        new AbstractMap.SimpleEntry<>("tag", tag),
                        new AbstractMap.SimpleEntry<>("tag_filter_mode", tagFilterMode == null ? null : tagFilterMode.value)
                )
                .filter(e -> e.getValue() != null)
                .forEach(e -> parameters.put(e.getKey(), e.getValue()));

        return parameters;
    }

    public static final class Builder {
        private String start;
        private String end;
        private TimeRange timeRange;
        private Scope scope;
        private Interval interval;
        private String[] browser;
        private String[] build;
        private String[] device;
        private String[] os;
        private TestResultParameter.Status status;
        private String[] tag;
        private TagFilterMode tagFilterMode;

        public Builder setStart(LocalDateTime val) {
            start = DateTimeUtils.toUtcString(val);
            return this;
        }

        public Builder setEnd(LocalDateTime val) {
            end = DateTimeUtils.toUtcString(val);
            return this;
        }

        public Builder setTimeRange(TimeRange val) {
            timeRange = val;
            return this;
        }

        public Builder setScope(Scope val) {
            scope = val;
            return this;
        }

        public Builder setInterval(Interval val) {
            interval = val;
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

        public Builder setStatus(TestResultParameter.Status val) {
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

        public AnalyticsTrendsParameter build() {
            boolean isTimeRangeUsed = timeRange != null;
            boolean isStartEndUsed = start != null && end != null;

            if (!isTimeRangeUsed && !isStartEndUsed) {
                throw new IllegalStateException("Either 'time_range' or 'start' and 'end' must be set.");
            }
            if (isTimeRangeUsed && isStartEndUsed) {
                throw new IllegalStateException("Only one of 'time_range' or 'start' and 'end' can be set, not both.");
            }
            if (interval == null) {
                interval = Interval.ONE_DAY;
            }

            return new AnalyticsTrendsParameter(this);
        }
    }
}
