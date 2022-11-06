package com.saucelabs.saucerest.model.storage;

import com.squareup.moshi.Json;

public class Proxy {

    @Json(name = "host")
    public String host;
    @Json(name = "port")
    public Integer port;

    /**
     * No args constructor for use in serialization
     */
    public Proxy() {
    }

    /**
     * @param port
     * @param host
     */
    public Proxy(String host, Integer port) {
        super();
        this.host = host;
        this.port = port;
    }
}