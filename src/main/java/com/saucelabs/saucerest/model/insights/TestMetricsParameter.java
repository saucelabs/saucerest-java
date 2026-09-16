package com.saucelabs.saucerest.model.insights;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/** Query parameters for {@code GET /v1/analytics/insights/test-metrics}. */
public class TestMetricsParameter {
    private final String query;
    private final String start;
    private final String end;
    private final TimeRange timeRange;
    private final Scope scope;
    private final String[] owner;
    private final TestResultParameter.Status status;
    private final String[] os;
    private final String[] browser;

    private TestMetricsParameter(Builder builder) {
        query = builder.query;
        start = builder.start;
        end = builder.end;
        timeRange = builder.timeRange;
        scope = builder.scope;
        owner = builder.owner;
        status = builder.status;
        os = builder.os;
        browser = builder.browser;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> parameters = new HashMap<>();

        Stream.of(
                        new AbstractMap.SimpleEntry<>("query", query),
                        new AbstractMap.SimpleEntry<>("start", start),
                        new AbstractMap.SimpleEntry<>("end", end),
                        new AbstractMap.SimpleEntry<>("time_range", timeRange == null ? null : timeRange.toString()),
                        new AbstractMap.SimpleEntry<>("scope", scope == null ? null : scope.value),
                        new AbstractMap.SimpleEntry<>("owner", owner),
                        new AbstractMap.SimpleEntry<>("status", status == null ? null : status.getValue()),
                        new AbstractMap.SimpleEntry<>("os", os),
                        new AbstractMap.SimpleEntry<>("browser", browser)
                )
                .filter(e -> e.getValue() != null)
                .forEach(e -> parameters.put(e.getKey(), e.getValue()));

        return parameters;
    }

    public static final class Builder {
        private final String query;
        private String start;
        private String end;
        private TimeRange timeRange;
        private Scope scope;
        private String[] owner;
        private TestResultParameter.Status status;
        private String[] os;
        private String[] browser;

        /** @param query The name of the test to return metrics for (required). */
        public Builder(String query) {
            this.query = query;
        }

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

        /** Required when {@link #setScope(Scope)} is {@link Scope#SINGLE}. */
        public Builder setOwner(String[] val) {
            owner = val;
            return this;
        }

        public Builder setStatus(TestResultParameter.Status val) {
            status = val;
            return this;
        }

        public Builder setOs(String[] val) {
            os = val;
            return this;
        }

        public Builder setBrowser(String[] val) {
            browser = val;
            return this;
        }

        public TestMetricsParameter build() {
            boolean isTimeRangeUsed = timeRange != null;
            boolean isStartEndUsed = start != null && end != null;

            if (!isTimeRangeUsed && !isStartEndUsed) {
                throw new IllegalStateException("Either 'time_range' or 'start' and 'end' must be set.");
            }
            if (isTimeRangeUsed && isStartEndUsed) {
                throw new IllegalStateException("Only one of 'time_range' or 'start' and 'end' can be set, not both.");
            }

            return new TestMetricsParameter(this);
        }
    }
}
