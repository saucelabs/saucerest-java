package com.saucelabs.saucerest.model.insights;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class TestResultParameter {
    private final String build;
    private final Boolean build_missing;
    private final Boolean descending;
    private final String end;
    private final String error;
    private final String name;
    private final int from;
    private final String owner;
    private final Scope scope;
    private final int size;
    private final String start;
    private final Status[] status;
    private final TimeRange time_range;

    private TestResultParameter(Builder builder) {
        build = builder.build;
        build_missing = builder.build_missing;
        descending = builder.descending;
        end = builder.end;
        error = builder.error;
        name = builder.name;
        from = builder.from;
        owner = builder.owner;
        scope = builder.scope;
        size = builder.size;
        start = builder.start;
        status = builder.status;
        time_range = builder.time_range;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> parameters = new HashMap<>();

        if (this.build != null) {
            parameters.put("build", this.build);
        }

        if (this.build_missing != null) {
            parameters.put("build_missing", this.build_missing);
        }

        if (this.descending != null) {
            parameters.put("descending", this.descending);
        }

        if (this.end != null) {
            parameters.put("end", this.end);
        }

        if (this.error != null) {
            parameters.put("error", this.error);
        }

        if (this.name != null) {
            parameters.put("name", this.name);
        }

        if (this.from != 0) {
            parameters.put("from", this.from);
        }

        if (this.owner != null) {
            parameters.put("owner", this.owner);
        }

        if (this.scope != null) {
            parameters.put("scope", this.scope.getValue());
        }

        if (this.size != 0) {
            parameters.put("size", this.size);
        }

        if (this.start != null) {
            parameters.put("start", this.start);
        }

        if (this.status != null) {
            parameters.put("status", this.status);
        }

        if (this.time_range != null) {
            parameters.put("time_range", this.time_range.toString());
        }

        return parameters;
    }

    public static final class Builder {
        private String build;
        private Boolean build_missing;
        private Boolean descending;
        private String end;
        private String error;
        private String name;
        private int from;
        private String owner;
        private Scope scope;
        private int size;
        private String start;
        private Status[] status;
        private TimeRange time_range;

        public Builder() {
        }

        public Builder setBuild(String val) {
            build = val;
            return this;
        }

        public Builder setBuild_missing(Boolean val) {
            build_missing = val;
            return this;
        }

        public Builder setDescending(Boolean val) {
            descending = val;
            return this;
        }

        public Builder setEnd(LocalDateTime val) {
            // Get the time zone offset for the given LocalDateTime value
            ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(val);

            // Convert the LocalDateTime value to UTC by applying the offset
            LocalDateTime utcDateTime = val.minusSeconds(offset.getTotalSeconds());

            // Set the end field to the UTC equivalent value
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            end = utcDateTime.format(formatter);

            return this;
        }

        public Builder setError(String val) {
            error = val;
            return this;
        }

        public Builder setName(String val) {
            name = val;
            return this;
        }

        public Builder setFrom(int val) {
            from = val;
            return this;
        }

        public Builder setOwner(String val) {
            owner = val;
            return this;
        }

        public Builder setScope(Scope val) {
            scope = val;
            return this;
        }

        public Builder setSize(int val) {
            size = val;
            return this;
        }

        public Builder setStart(LocalDateTime val) {
            // Get the time zone offset for the given LocalDateTime value
            ZoneOffset offset = ZoneId.systemDefault().getRules().getOffset(val);

            // Convert the LocalDateTime value to UTC by applying the offset
            LocalDateTime utcDateTime = val.minusSeconds(offset.getTotalSeconds());

            // Set the end field to the UTC equivalent value
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            start = utcDateTime.format(formatter);

            return this;
        }

        public Builder setStatus(Status[] val) {
            status = val;
            return this;
        }

        public Builder setTime_range(TimeRange val) {
            time_range = val;
            return this;
        }

        public TestResultParameter build() {
            boolean isTimeRangeUsed = time_range != null;
            boolean isStartEndUsed = start != null && end != null;

            if (!isTimeRangeUsed && !isStartEndUsed) {
                throw new IllegalStateException("Either 'time_range' or 'start' and 'end' must be set.");
            }
            if (isTimeRangeUsed && isStartEndUsed) {
                throw new IllegalStateException("Only one of 'time_range' or 'start' and 'end' can be set, not both.");
            }
            if (isStartEndUsed) {
                if (!isStartBeforeEnd()) {
                    throw new IllegalStateException("'start' must be before 'end'.");
                }
            }
            if (size == 0) {
                size = 10;
            }

            return new TestResultParameter(this);
        }

        private Boolean isStartBeforeEnd() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

            LocalDateTime dateTime1 = LocalDateTime.parse(start, formatter);
            LocalDateTime dateTime2 = LocalDateTime.parse(end, formatter);

            if (dateTime1.isBefore(dateTime2)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public enum Scope {
        ME("me"),
        ORGANIZATION("organization"),
        SINGLE("single");

        private final String value;

        Scope(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum Unit {
        D("d"),
        H("h"),
        M("m"),
        S("s");

        private final String value;

        Unit(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static class TimeRange {
        private final int value;
        private final Unit unit;

        public TimeRange(int value, Unit unit) {
            this.value = value;
            this.unit = unit;
        }

        @Override
        public String toString() {
            return String.format("%d%s", value, unit.getValue());
        }
    }

    public enum Status {
        PASSED("passed"),
        ERRORED("errored"),
        FAILED("failed"),
        COMPLETE("complete");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}