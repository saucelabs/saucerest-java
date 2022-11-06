package com.saucelabs.saucerest.model.storage;

import com.squareup.moshi.Json;

public class Proxy {

    @Json(name = "host")
    public String host;
    @Json(name = "port")
    public Integer port;

    public Proxy() {
    }

    public Proxy(String host, Integer port) {
        super();
        this.host = host;
        this.port = port;
    }
}