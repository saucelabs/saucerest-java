package com.saucelabs.saucerest.integration;

// Yes, duplicating instead of using DataCenter enum to restrict and control where these tests run.
enum DataCenter {
    US("https://ondemand.us-west-1.saucelabs.com/wd/hub"),
    EU("https://ondemand.eu-central-1.saucelabs.com/wd/hub");
    // Turn off testing against APAC as not all APIs are supported in this region yet
    //APAC_SOUTHEAST("https://ondemand.apac-southeast-1.saucelabs.com/wd/hub");

    public final String label;

    DataCenter(String label) {
        this.label = label;
    }
}