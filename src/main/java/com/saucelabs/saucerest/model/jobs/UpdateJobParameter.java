package com.saucelabs.saucerest.model.jobs;

import com.google.gson.annotations.SerializedName;
import com.saucelabs.saucerest.JobVisibility;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateJobParameter {
    private final String name;
    private final List<String> tags;
    @SerializedName("public")
    private final JobVisibility visibility;
    private final Boolean passed;
    private final String build;
    @SerializedName("custom-data")
    private final Map<String, String> customData;

    public UpdateJobParameter(String name, List<String> tags, JobVisibility visibility, boolean passed, String build, Map<String, String> customData) {
        this.name = name;
        this.tags = tags;
        this.visibility = visibility;
        this.passed = passed;
        this.build = build;
        this.customData = customData;
    }

    private UpdateJobParameter(Builder builder) {
        name = builder.name;
        tags = builder.tags;
        visibility = builder.visibility;
        passed = builder.passed;
        build = builder.build;
        customData = builder.customData;
    }

    public static final class Builder {
        private String name;
        private List<String> tags;
        private JobVisibility visibility;
        private Boolean passed;
        private String build;
        private Map<String, String> customData;

        public Builder setName(String val) {
            name = val;
            return this;
        }

        public Builder setTags(List<String> val) {
            tags = val;
            return this;
        }

        public Builder setVisibility(JobVisibility val) {
            visibility = val;
            return this;
        }

        public Builder setPassed(boolean val) {
            passed = val;
            return this;
        }

        public Builder setBuild(String val) {
            build = val;
            return this;
        }

        public Builder setCustomData(Map<String, String> val) {
            customData = val;
            return this;
        }

        public UpdateJobParameter build() {
            return new UpdateJobParameter(this);
        }
    }
}