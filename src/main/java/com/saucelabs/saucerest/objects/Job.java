package com.saucelabs.saucerest.objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {
    public static class JobDateImpl extends StdDeserializer<Date> {
        public JobDateImpl() {
            super(Date.class);
        }

        @Override
        public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return new Date(Long.parseLong(jsonParser.getValueAsString())*1000);
        }
    }

//    "browser_version": "10.0.9200.17089.",
    @JsonProperty("browser_short_version")
    private String browserShortVersion;

    @JsonProperty("video_url")
    private URL videoURL;
    @JsonProperty("log_url")
    private URL logURL;

    @JsonProperty("creation_time")
    @JsonDeserialize(using=JobDateImpl.class)
    private Date creationTime;

    @JsonProperty("deletion_time")
    @JsonDeserialize(using=JobDateImpl.class)
    private Date deletionTime;

    @JsonProperty("start_time")
    @JsonDeserialize(using=JobDateImpl.class)
    private Date startTime;

    @JsonProperty("modification_time")
    @JsonDeserialize(using=JobDateImpl.class)
    private Date modificationTime;

    @JsonProperty("end_time")
    @JsonDeserialize(using=JobDateImpl.class)
    private Date endTime;

    private String owner;
    private String id;
    private String name;
    private boolean passed;
    private boolean proxied;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public Date getDeletionTime() {
        return deletionTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getModificationTime() {
        return modificationTime;
    }

/*
        "custom-data": null,
        "automation_backend": "webdriver",
        "record_screenshots": true,
        "record_video": true,
        "build": "test_sauce__22",
        "public": null,
        "assigned_tunnel_id": null,
        "status": "complete",
        "tags": [],
        "consolidated_status": "failed",
        "commands_not_successful": 76,
        "command_counts": {
        "All": 85,
            "Error": 76
    },
        "name": "Sauce Sample Test",
        "proxy_host": null,
        "": 1445299886,
        "error": null,
        "os": "Windows 2008",
        "breakpointed": null,
        "browser": "iexplore"
        */
}
