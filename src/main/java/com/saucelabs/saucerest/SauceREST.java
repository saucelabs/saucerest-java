package com.saucelabs.saucerest;

import com.saucelabs.saucerest.api.AccountsEndpoint;
import com.saucelabs.saucerest.api.BuildsEndpoint;
import com.saucelabs.saucerest.api.HttpClientConfig;
import com.saucelabs.saucerest.api.InsightsEndpoint;
import com.saucelabs.saucerest.api.JobsEndpoint;
import com.saucelabs.saucerest.api.PerformanceEndpoint;
import com.saucelabs.saucerest.api.PlatformEndpoint;
import com.saucelabs.saucerest.api.RealDevicesEndpoint;
import com.saucelabs.saucerest.api.SauceConnectEndpoint;
import com.saucelabs.saucerest.api.StorageEndpoint;

import java.io.Serializable;

/**
 * This class provides a simple interface to the Sauce REST API. <br>
 * It will contain methods to make it easier to do the most used frequent tasks like downloading log
 * files. Accessing the public API is done by using the methods in {@link
 * com.saucelabs.saucerest.api}. They are modelled after this documentation: <a
 * href="https://docs.saucelabs.com/dev/api/">here</a>
 */
public class SauceREST implements Serializable {
  private final String server;
  private final String apiServer;
  private final String edsServer;
  private final String appServer;
  private final HttpClientConfig config;
  protected String username;
  protected String accessKey;

  public SauceREST(DataCenter dataCenter) {
    this(System.getenv("SAUCE_USERNAME"), System.getenv("SAUCE_ACCESS_KEY"), dataCenter);
  }

  public SauceREST(String username, String accessKey, DataCenter dataCenter) {
    this(username, accessKey, dataCenter, HttpClientConfig.defaultConfig());
  }

  public SauceREST(
      String username, String accessKey, DataCenter dataCenter, HttpClientConfig config) {
    this.username = username;
    this.accessKey = accessKey;
    this.server = buildUrl(dataCenter.server(), "SAUCE_REST_ENDPOINT", "saucerest-java.base_url");
    this.appServer =
        buildUrl(dataCenter.appServer(), "SAUCE_REST_APP_ENDPOINT", "saucerest-java.base_app_url");
    this.apiServer =
        buildUrl(dataCenter.apiServer(), "SAUCE_API_ENDPOINT", "saucerest-java.base_api_url");
    this.edsServer =
        buildUrl(dataCenter.edsServer(), "SAUCE_REST_EDS_ENDPOINT", "saucerest-java.base_eds_url");
    this.config = config;
  }

  /**
   * Build URL with environment variable, or system property, or default URL.
   *
   * @param defaultUrl default URL if no URL is found in environment variables and system properties
   * @param envVarName the name of the environment variable that may contain URL
   * @param systemPropertyName the name of the system property that may contain URL
   * @return URL to use
   */
  private String buildUrl(String defaultUrl, String envVarName, String systemPropertyName) {
    String envVar = System.getenv(envVarName);
    return envVar != null ? envVar : System.getProperty(systemPropertyName, defaultUrl);
  }

  public JobsEndpoint getJobsEndpoint() {
    JobsEndpoint endpoint = new JobsEndpoint(this.username, this.accessKey, this.apiServer);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public JobsEndpoint getJobsEndpoint(DataCenter dataCenter) {
    JobsEndpoint endpoint = new JobsEndpoint(this.username, this.accessKey, dataCenter);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public JobsEndpoint getJobsEndpoint(String apiServer) {
    JobsEndpoint endpoint = new JobsEndpoint(this.username, this.accessKey, apiServer);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public StorageEndpoint getStorageEndpoint() {
    StorageEndpoint endpoint = new StorageEndpoint(this.username, this.accessKey, this.apiServer);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public StorageEndpoint getStorageEndpoint(DataCenter dataCenter) {
    StorageEndpoint endpoint = new StorageEndpoint(this.username, this.accessKey, dataCenter);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public StorageEndpoint getStorageEndpoint(String apiServer) {
    StorageEndpoint endpoint = new StorageEndpoint(this.username, this.accessKey, apiServer);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public PlatformEndpoint getPlatformEndpoint() {
    PlatformEndpoint endpoint = new PlatformEndpoint(this.apiServer);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public PlatformEndpoint getPlatformEndpoint(DataCenter dataCenter) {
    PlatformEndpoint endpoint = new PlatformEndpoint(dataCenter);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public PlatformEndpoint getPlatformEndpoint(String apiServer) {
    PlatformEndpoint endpoint = new PlatformEndpoint(apiServer);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public RealDevicesEndpoint getRealDevicesEndpoint(DataCenter dataCenter) {
    RealDevicesEndpoint endpoint =
        new RealDevicesEndpoint(this.username, this.accessKey, dataCenter);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public RealDevicesEndpoint getRealDevicesEndpoint() {
    RealDevicesEndpoint endpoint =
        new RealDevicesEndpoint(this.username, this.accessKey, this.apiServer);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public RealDevicesEndpoint getRealDevicesEndpoint(String apiServer) {
    RealDevicesEndpoint endpoint =
        new RealDevicesEndpoint(this.username, this.accessKey, apiServer);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public SauceConnectEndpoint getSauceConnectEndpoint() {
    SauceConnectEndpoint endpoint =
        new SauceConnectEndpoint(this.username, this.accessKey, this.apiServer);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public SauceConnectEndpoint getSauceConnectEndpoint(String apiServer) {
    SauceConnectEndpoint endpoint =
        new SauceConnectEndpoint(this.username, this.accessKey, apiServer);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public SauceConnectEndpoint getSauceConnectEndpoint(DataCenter dataCenter) {
    SauceConnectEndpoint endpoint =
        new SauceConnectEndpoint(this.username, this.accessKey, dataCenter);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public AccountsEndpoint getAccountsEndpoint() {
    AccountsEndpoint endpoint = new AccountsEndpoint(this.username, this.accessKey, this.apiServer);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public AccountsEndpoint getAccountsEndpoint(String apiServer) {
    AccountsEndpoint endpoint = new AccountsEndpoint(this.username, this.accessKey, apiServer);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public AccountsEndpoint getAccountsEndpoint(DataCenter dataCenter) {
    AccountsEndpoint endpoint = new AccountsEndpoint(this.username, this.accessKey, dataCenter);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public BuildsEndpoint getBuildsEndpoint() {
    BuildsEndpoint endpoint = new BuildsEndpoint(this.username, this.accessKey, this.apiServer);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public BuildsEndpoint getBuildsEndpoint(String apiServer) {
    BuildsEndpoint endpoint = new BuildsEndpoint(this.username, this.accessKey, apiServer);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public BuildsEndpoint getBuildsEndpoint(DataCenter dataCenter) {
    BuildsEndpoint endpoint = new BuildsEndpoint(this.username, this.accessKey, dataCenter);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public InsightsEndpoint getInsightsEndpoint() {
    InsightsEndpoint endpoint = new InsightsEndpoint(this.username, this.accessKey, this.apiServer);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public InsightsEndpoint getInsightsEndpoint(String apiServer) {
    InsightsEndpoint endpoint = new InsightsEndpoint(this.username, this.accessKey, apiServer);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public InsightsEndpoint getInsightsEndpoint(DataCenter dataCenter) {
    InsightsEndpoint endpoint = new InsightsEndpoint(this.username, this.accessKey, dataCenter);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public PerformanceEndpoint getPerformanceEndpoint() {
    PerformanceEndpoint endpoint =
        new PerformanceEndpoint(this.username, this.accessKey, this.apiServer);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public PerformanceEndpoint getPerformanceEndpoint(String apiServer) {
    PerformanceEndpoint endpoint =
        new PerformanceEndpoint(this.username, this.accessKey, apiServer);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public PerformanceEndpoint getPerformanceEndpoint(DataCenter dataCenter) {
    PerformanceEndpoint endpoint =
        new PerformanceEndpoint(this.username, this.accessKey, dataCenter);
    endpoint.createHttpClient(this.config);
    return endpoint;
  }

  public String getServer() {
    return this.server;
  }

  public String getEdsServer() {
    return this.edsServer;
  }

  public String getAppServer() {
    return this.appServer;
  }

  public String getUsername() {
    return this.username;
  }

  public String getAccessKey() {
    return this.accessKey;
  }
}
