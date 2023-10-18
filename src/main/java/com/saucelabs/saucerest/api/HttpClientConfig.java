package com.saucelabs.saucerest.api;

import lombok.Getter;
import okhttp3.Authenticator;
import okhttp3.Interceptor;

import java.net.Proxy;
import java.time.Duration;

@Getter
public class HttpClientConfig {

  private final Duration connectTimeout;
  private final Duration readTimeout;
  private final Duration writeTimeout;
  private final Proxy proxy;
  private final Authenticator authenticator;
  private final Interceptor interceptor;

  protected HttpClientConfig(
      Duration connectTimeout,
      Duration readTimeout,
      Duration writeTimeout,
      Proxy proxy,
      Authenticator authenticator,
      Interceptor interceptor) {
    this.connectTimeout = connectTimeout;
    this.readTimeout = readTimeout;
    this.writeTimeout = writeTimeout;
    this.proxy = proxy;
    this.authenticator = authenticator;
    this.interceptor = interceptor;
  }

  public static HttpClientConfig defaultConfig() {
    return new HttpClientConfig(
        Duration.ofSeconds(300),
        Duration.ofSeconds(300),
        Duration.ofSeconds(300),
        null,
        null,
        null);
  }

  public HttpClientConfig connectTimeout(Duration connectTimeout) {
    return new HttpClientConfig(
        connectTimeout, readTimeout, writeTimeout, proxy, authenticator, interceptor);
  }

  public HttpClientConfig readTimeout(Duration readTimeout) {
    return new HttpClientConfig(
        connectTimeout, readTimeout, writeTimeout, proxy, authenticator, interceptor);
  }

  public HttpClientConfig writeTimeout(Duration writeTimeout) {
    return new HttpClientConfig(
        connectTimeout, readTimeout, writeTimeout, proxy, authenticator, interceptor);
  }

  public HttpClientConfig proxy(Proxy proxy) {
    return new HttpClientConfig(
        connectTimeout, readTimeout, writeTimeout, proxy, authenticator, interceptor);
  }

  public HttpClientConfig authenticator(Authenticator authenticator) {
    return new HttpClientConfig(
        connectTimeout, readTimeout, writeTimeout, proxy, authenticator, interceptor);
  }

  public HttpClientConfig interceptor(Interceptor interceptor) {
    return new HttpClientConfig(
        connectTimeout, readTimeout, writeTimeout, proxy, authenticator, interceptor);
  }
}
