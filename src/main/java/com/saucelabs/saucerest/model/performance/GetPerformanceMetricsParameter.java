package com.saucelabs.saucerest.model.performance;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/** Query parameters for {@code GET /v2/performance/metrics/}. */
public class GetPerformanceMetricsParameter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private final String pageUrl;
    private final String[] metricNames;
    private final String startDate;
    private final String endDate;

    private GetPerformanceMetricsParameter(Builder builder) {
        pageUrl = builder.pageUrl;
        metricNames = builder.metricNames;
        startDate = builder.startDate;
        endDate = builder.endDate;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> parameters = new HashMap<>();

        Stream.of(
                        new AbstractMap.SimpleEntry<>("page_url", pageUrl),
                        new AbstractMap.SimpleEntry<>("metric_names", metricNames),
                        new AbstractMap.SimpleEntry<>("start_date", startDate),
                        new AbstractMap.SimpleEntry<>("end_date", endDate)
                )
                .filter(e -> e.getValue() != null)
                .forEach(e -> parameters.put(e.getKey(), e.getValue()));

        return parameters;
    }

    public static final class Builder {
        private String pageUrl;
        private String[] metricNames;
        private String startDate;
        private String endDate;

        public Builder setPageUrl(String val) {
            pageUrl = val;
            return this;
        }

        public Builder setMetricNames(String[] val) {
            metricNames = val;
            return this;
        }

        public Builder setStartDate(LocalDateTime val) {
            startDate = toUtcString(val);
            return this;
        }

        public Builder setEndDate(LocalDateTime val) {
            endDate = toUtcString(val);
            return this;
        }

        private static String toUtcString(LocalDateTime val) {
            ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(val);
            LocalDateTime utcDateTime = val.minusSeconds(offset.getTotalSeconds());

            return utcDateTime.format(FORMATTER);
        }

        public GetPerformanceMetricsParameter build() {
            return new GetPerformanceMetricsParameter(this);
        }
    }
}
