package com.saucelabs.saucerest;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    PATCH("PATCH"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS");

    public final String label;

    HttpMethod(String label) {
        this.label = label;
    }
}
