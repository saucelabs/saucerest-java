package com.saucelabs.saucerest;

import java.util.stream.Stream;

public enum DataCenter {
    US_WEST("https://saucelabs.com/", "https://api.us-west-1.saucelabs.com/", "https://app.saucelabs.com/"),
    EU_CENTRAL("https://eu-central-1.saucelabs.com/", "https://api.eu-central-1.saucelabs.com/", "https://app.eu-central-1.saucelabs.com/"),
    US_EAST("https://us-east-1.saucelabs.com/", "https://api.us-east-1.saucelabs.com/", "https://app.us-east-1.saucelabs.com/"),
    APAC_SOUTHEAST("https://apac-southeast-1.saucelabs.com/", "https://api.apac-southeast-1.saucelabs.com/", "https://app.apac-southeast-1.saucelabs.com/");

    public final String server;
    public final String apiServer;
    public final String appServer;

    DataCenter(String server, String apiServer, String appServer) {
        this.server = server;
        this.apiServer = apiServer;
        this.appServer = appServer;
    }

    public String server() {
        return server;
    }

    public String apiServer() {
        return apiServer;
    }

    public String edsServer() {
        return apiServer + "v1/eds/";
    }

    public String appServer() {
        return appServer;
    }

    public static DataCenter fromString(String dataCenter) {
        return Stream.of(values()).filter(dc -> dc.name().equalsIgnoreCase(dataCenter)).findFirst().orElse(null);
    }
}
