package com.saucelabs.saucerest;

public enum DataCenter {
    US ("https://saucelabs.com/", "https://api.us-west-1.saucelabs.com/v1/eds/", "https://app.saucelabs.com/"),
    EU ("https://eu-central-1.saucelabs.com/", "https://api.eu-central-1.saucelabs.com/v1/eds/", "https://app.eu-central-1.saucelabs.com/"),
    US_EAST ("https://us-east-1.saucelabs.com/", "https://api.us-east-1.saucelabs.com/v1/eds/", "https://app.us-east-1.saucelabs.com/");
    public final String server;
    public final String edsServer;
    public final String appServer;

    DataCenter(String server, String edsServer, String appServer) {
        this.server = server;
        this.edsServer = edsServer;
        this.appServer = appServer;
    }

    public String server() {
        return server;
    }

    public String edsServer() {
        return edsServer;
    }

    public String appServer() {
        return appServer;
    }

    public static DataCenter fromString(String dataCenter) {
        for (DataCenter dc : DataCenter.values()) {
            if (dc.name().equals(dataCenter)) {
                return dc;
            }
        }
        return US; // default to US
    }
}
