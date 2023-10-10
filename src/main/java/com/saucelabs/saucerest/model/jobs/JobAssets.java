package com.saucelabs.saucerest.model.jobs;

import com.squareup.moshi.Json;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobAssets {

    @Json(name = "video.mp4")
    public String videoMp4;
    @Json(name = "selenium-log")
    public String seleniumLog;
    @Json(name = "ios-syslog.log")
    public String iosSyslogLog;
    @Json(name = "sauce-log")
    public String sauceLog;
    @Json(name = "video")
    public String video;
    @Json(name = "screenshots")
    public List<String> screenshots;
    @Json(name = "logcat.log")
    public String logcatLog;

    public JobAssets() {
    }

    public JobAssets(String videoMp4, String seleniumLog, String iosSyslogLog, String sauceLog, String video, List<String> screenshots, String logcatLog) {
        super();
        this.videoMp4 = videoMp4;
        this.seleniumLog = seleniumLog;
        this.iosSyslogLog = iosSyslogLog;
        this.sauceLog = sauceLog;
        this.video = video;
        this.screenshots = screenshots;
        this.logcatLog = logcatLog;
    }

    public Map<String, String> getAvailableAssets() {
        Map<String, String> assetMap = new HashMap<>();

        // Duplicate video
//        if (this.videoMp4 != null) {
//            assetMap.put("video.mp4", this.videoMp4);
//        }

        if (this.seleniumLog != null) {
            assetMap.put("selenium-log", this.seleniumLog);
        }

        if (this.iosSyslogLog != null) {
            assetMap.put("ios-syslog.log", this.iosSyslogLog);
        }

        if (this.sauceLog != null) {
            assetMap.put("sauce-log", this.sauceLog);
        }

        if (this.video != null) {
            assetMap.put("video", this.video);
        }

        // Use different method to get screenshots
//        if (this.screenshots != null) {
//            assetMap.put("screenshots", this.screenshots);
//        }

        if (this.logcatLog != null) {
            assetMap.put("logcat.log", this.logcatLog);
        }

        return assetMap;
    }
}