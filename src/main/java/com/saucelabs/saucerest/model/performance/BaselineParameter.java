package com.saucelabs.saucerest.model.performance;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/** Query parameters for {@code GET /v2/performance/metrics/{job_id}/baseline/}. */
public class BaselineParameter {
    private final String[] metricNames;
    private final int orderIndex;
    private final Integer regimeStart;
    private final Integer regimeEnd;

    private BaselineParameter(Builder builder) {
        metricNames = builder.metricNames;
        orderIndex = builder.orderIndex;
        regimeStart = builder.regimeStart;
        regimeEnd = builder.regimeEnd;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> parameters = new HashMap<>();

        Stream.of(
                        new AbstractMap.SimpleEntry<>("metric_names", metricNames),
                        new AbstractMap.SimpleEntry<>("order_index", orderIndex),
                        new AbstractMap.SimpleEntry<>("regime_start", regimeStart),
                        new AbstractMap.SimpleEntry<>("regime_end", regimeEnd)
                )
                .filter(e -> e.getValue() != null)
                .forEach(e -> parameters.put(e.getKey(), e.getValue()));

        return parameters;
    }

    public static final class Builder {
        private final String[] metricNames;
        private final int orderIndex;
        private Integer regimeStart;
        private Integer regimeEnd;

        /**
         * @param metricNames The metrics to return baseline information for (required).
         * @param orderIndex The record number to begin returning results from (required).
         */
        public Builder(String[] metricNames, int orderIndex) {
            this.metricNames = metricNames;
            this.orderIndex = orderIndex;
        }

        public Builder setRegimeStart(Integer val) {
            regimeStart = val;
            return this;
        }

        public Builder setRegimeEnd(Integer val) {
            regimeEnd = val;
            return this;
        }

        public BaselineParameter build() {
            return new BaselineParameter(this);
        }
    }
}
