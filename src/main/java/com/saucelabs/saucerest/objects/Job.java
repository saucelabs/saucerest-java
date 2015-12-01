package com.saucelabs.saucerest.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.saucelabs.saucerest.deserializers.UnixtimeDeserializer;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

@JsonIgnoreProperties({"url"})
public class Job {
    @JsonProperty("browser_short_version")
    private String browser_short_version;

    public String getBrowserShortVersion() { return this.browser_short_version; }

    @JsonProperty("video_url")
    private URL video_url;

    public URL getVideoUrl() { return this.video_url; }

    @JsonProperty("creation_time")
    @JsonDeserialize(using=UnixtimeDeserializer.class)
    private Date creation_time;

    public Date getCreationTime() { return this.creation_time; }

    @JsonProperty("custom-data")
    private Object custom_data;

    public Object getCustomData() { return this.custom_data; }

    @JsonProperty("browser_version")
    private String browser_version;

    public String getBrowserVersion() { return this.browser_version; }

    @JsonProperty("owner")
    private String owner;

    public String getOwner() { return this.owner; }

    @JsonProperty("automation_backend")
    private String automation_backend;

    public String getAutomationBackend() { return this.automation_backend; }

    @JsonProperty("id")
    private String id;

    public String getId() { return this.id; }

    @JsonProperty("collects_automator_log")
    private boolean collects_automator_log;

    public boolean getCollectsAutomatorLog() { return this.collects_automator_log; }

    @JsonProperty("record_screenshots")
    private boolean record_screenshots;

    public boolean getRecordScreenshots() { return this.record_screenshots; }

    @JsonProperty("record_video")
    private boolean record_video;

    public boolean getRecordVideo() { return this.record_video; }

    @JsonProperty("build")
    private String build;

    public String getBuild() { return this.build; }

    @JsonProperty("passed")
    private boolean passed;

    public boolean getPassed() { return this.passed; }

    @JsonProperty("public")
    private String privacy;

    public String getPublic() { return this.privacy; }

    @JsonProperty("assigned_tunnel_id")
    private String assigned_tunnel_id;

    public String getAssignedTunnelId() { return this.assigned_tunnel_id; }

    @JsonProperty("status")
    private String status;

    public String getStatus() { return this.status; }

    @JsonProperty("log_url")
    private URL log_url;

    public URL getLogUrl() { return this.log_url; }

    @JsonProperty("start_time")
    @JsonDeserialize(using=UnixtimeDeserializer.class)
    private Date start_time;

    public Date getStartTime() { return this.start_time; }

    @JsonProperty("proxied")
    private boolean proxied;

    public boolean getProxied() { return this.proxied; }

    // TODO - this is returned by getBuildJobs but not by getJobInfo
    @JsonProperty("proxy_host")
    private boolean proxy_host;

    public boolean getProxyHost() { return this.proxy_host; }

    @JsonProperty("modification_time")
    @JsonDeserialize(using=UnixtimeDeserializer.class)
    private Date modification_time;

    public Date getModificationTime() { return this.modification_time; }

    @JsonProperty("tags")
    private ArrayList<String> tags;

    public ArrayList<String> getTags() { return this.tags; }

    @JsonProperty("name")
    private String name;

    public String getName() { return this.name; }

    @JsonProperty("commands_not_successful")
    private int commands_not_successful;

    public int getCommandsNotSuccessful() { return this.commands_not_successful; }

    @JsonProperty("consolidated_status")
    private String consolidated_status;

    public String getConsolidatedStatus() { return this.consolidated_status; }

    @JsonProperty("selenium_version")
    private String selenium_version;

    public String getSeleniumVersion() { return this.selenium_version; }

    @JsonProperty("manual")
    private boolean manual;

    public boolean getManual() { return this.manual; }

    @JsonProperty("end_time")
    @JsonDeserialize(using=UnixtimeDeserializer.class)
    private Date end_time;

    public Date getEndTime() { return this.end_time; }

    // TODO - this is returned by getBuildJobs but not by getJobInfo
    @JsonProperty("deletion_time")
    @JsonDeserialize(using=UnixtimeDeserializer.class)
    private Date deletion_time;

    public Date getDeletionTime() { return this.deletion_time; }

    @JsonProperty("error")
    private String error;

    public String getError() { return this.error; }

    @JsonProperty("os")
    private String os;

    public String getOs() { return this.os; }

    @JsonProperty("breakpointed")
    private Object breakpointed;

    public Object getBreakpointed() { return this.breakpointed; }

    @JsonProperty("browser")
    private String browser;

    public String getBrowser() { return this.browser; }

    public static class CommandCountStats {
        @JsonProperty("All")
        private int all;
        public int getAll() { return this.all; }

        @JsonProperty("Error")
        private int error;
        public int getError() { return this.error; }
    }

    // TODO - this is returned by getBuildJobs but not by getJobInfo
    @JsonProperty("command_counts")
    private CommandCountStats command_counts;

    public CommandCountStats getCommandCounts() { return this.command_counts; }

    public static class BaseConfig {
        @JsonProperty("status")
        private String status;

        @JsonProperty("platform")
        private String platform;

        @JsonProperty("browserName")
        private String browserName;

        @JsonProperty("version")
        private int version;
        public int getVersion() { return this.version; }

        @JsonProperty("name")
        private String name;
        public String getName() { return this.name; }

        public String getBrowserName() {
            return browserName;
        }

        public String getPlatform() {
            return platform;
        }

        public String getStatus() {
            return status;
        }
    }

    // TODO - this is returned by getBuildJobs but not by getJobInfo
    @JsonProperty("base_config")
    private BaseConfig base_config;

    public BaseConfig getBaseConfig() { return this.base_config; }
}
