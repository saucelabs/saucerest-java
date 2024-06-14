package com.saucelabs.saucerest.model.storage;

public class Proxy {

    public String host;
    public Integer port;

    private Proxy(Builder builder) {
        host = builder.host;
        port = builder.port;
    }

    public static final class Builder {
        private String host;
        private Integer port;

        public Builder setHost(String val) {
            host = val;
            return this;
        }

        public Builder setPort(Integer val) {
            port = val;
            return this;
        }

        public Proxy build() {
            return new Proxy(this);
        }
    }
}