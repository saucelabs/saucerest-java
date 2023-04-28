package com.saucelabs.saucerest.model.jobs;

import java.util.HashMap;
import java.util.Map;

public class GetJobsParameters {
    private final String username;
    private final int limit;
    private final int skip;
    private final int from;
    private final int to;
    private final Format format;

    public GetJobsParameters(String username, int limit, int skip, int from, int to, Format format) {
        this.username = username;
        this.limit = limit;
        this.skip = skip;
        this.from = from;
        this.to = to;
        this.format = format;
    }

    private GetJobsParameters(Builder builder) {
        username = builder.username;
        limit = builder.limit;
        skip = builder.skip;
        from = builder.from;
        to = builder.to;
        format = builder.format;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> parameters = new HashMap<>();

        if (this.username != null) {
            parameters.put("username", this.username);
        }

        if (this.limit > 0) {
            parameters.put("limit", this.limit);
        }

        if (this.skip > 0) {
            parameters.put("skip", this.skip);
        }

        if (this.from > 0) {
            parameters.put("from", this.from);
        }

        if (this.to > 0) {
            parameters.put("to", this.to);
        }

        if (this.format != null) {
            parameters.put("format", this.format);
        }

        return parameters;
    }

    public enum Format {
        JSON("json"),
        CSV("csv");

        private final String value;

        Format(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static final class Builder {
        private String username;
        private int limit;
        private int skip;
        private int from;
        private int to;
        private Format format;

        public Builder() {
        }

        public Builder setUsername(String val) {
            username = val;
            return this;
        }

        public Builder setLimit(int val) {
            limit = val;
            return this;
        }

        public Builder setSkip(int val) {
            skip = val;
            return this;
        }

        public Builder setFrom(int val) {
            from = val;
            return this;
        }

        public Builder setTo(int val) {
            to = val;
            return this;
        }

        public Builder setFormat(Format val) {
            format = val;
            return this;
        }

        public GetJobsParameters build() {
            return new GetJobsParameters(this);
        }
    }
}