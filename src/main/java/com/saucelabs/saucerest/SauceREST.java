package com.saucelabs.saucerest;

import com.saucelabs.saucerest.api.*;

import java.io.Serializable;

/**
 * This class provides a simple interface to the Sauce REST API. <br>
 * It will contain methods to make it easier to do the most used frequent tasks like downloading log files.
 * Accessing the public API is done by using the methods in {@link com.saucelabs.saucerest.api}.
 * They are modelled after this documentation: <a href="https://docs.saucelabs.com/dev/api/">here</a>
 */
public class SauceREST implements Serializable {
    private final String server;
    private final String apiServer;
    private final String edsServer;
    private final String appServer;
    protected String username;
    protected String accessKey;

    public SauceREST(DataCenter dataCenter) {
        this(System.getenv("SAUCE_USERNAME"), System.getenv("SAUCE_ACCESS_KEY"), dataCenter);
    }

    public SauceREST(String username, String accessKey, DataCenter dataCenter) {
        this.username = username;
        this.accessKey = accessKey;
        this.server = buildUrl(dataCenter.server(), "SAUCE_REST_ENDPOINT", "saucerest-java.base_url");
        this.appServer = buildUrl(dataCenter.appServer(), "SAUCE_REST_APP_ENDPOINT", "saucerest-java.base_app_url");
        this.apiServer = buildUrl(dataCenter.apiServer(), "SAUCE_API_ENDPOINT", "saucerest-java.base_api_url");
        this.edsServer = buildUrl(dataCenter.edsServer(), "SAUCE_REST_EDS_ENDPOINT", "saucerest-java.base_eds_url");
    }

    public String getUsername() {
        return username;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getServer() {
        return server;
    }

    public String getAppServer() {
        return appServer;
    }

    public String getEdsServer() {
        return edsServer;
    }

    /**
     * Build URL with environment variable, or system property, or default URL.
     *
     * @param defaultUrl         default URL if no URL is found in environment variables and system properties
     * @param envVarName         the name of the environment variable that may contain URL
     * @param systemPropertyName the name of the system property that may contain URL
     * @return URL to use
     */
    private String buildUrl(String defaultUrl, String envVarName, String systemPropertyName) {
        String envVar = System.getenv(envVarName);
        return envVar != null ? envVar : System.getProperty(systemPropertyName, defaultUrl);
    }

    public JobsEndpoint getJobsEndpoint() {
        return new JobsEndpoint(this.username, this.accessKey, this.apiServer);
    }

    public JobsEndpoint getJobsEndpoint(DataCenter dataCenter) {
        return new JobsEndpoint(this.username, this.accessKey, dataCenter);
    }

    public JobsEndpoint getJobsEndpoint(String apiServer) {
        return new JobsEndpoint(this.username, this.accessKey, apiServer);
    }

    public StorageEndpoint getStorageEndpoint() {
        return new StorageEndpoint(this.username, this.accessKey, this.apiServer);
    }

    public StorageEndpoint getStorageEndpoint(DataCenter dataCenter) {
        return new StorageEndpoint(this.username, this.accessKey, dataCenter);
    }

    public StorageEndpoint getStorageEndpoint(String apiServer) {
        return new StorageEndpoint(this.username, this.accessKey, apiServer);
    }

    public PlatformEndpoint getPlatformEndpoint() {
        return new PlatformEndpoint(this.username, this.accessKey, this.apiServer);
    }

    public PlatformEndpoint getPlatformEndpoint(DataCenter dataCenter) {
        return new PlatformEndpoint(this.username, this.accessKey, dataCenter);
    }

    public PlatformEndpoint getPlatformEndpoint(String apiServer) {
        return new PlatformEndpoint(this.username, this.accessKey, apiServer);
    }

    public RealDevicesEndpoint getRealDevicesEndpoint(DataCenter dataCenter) {
        return new RealDevicesEndpoint(this.username, this.accessKey, dataCenter);
    }

    public RealDevicesEndpoint getRealDevicesEndpoint() {
        return new RealDevicesEndpoint(this.username, this.accessKey, this.apiServer);
    }

    public RealDevicesEndpoint getRealDevicesEndpoint(String apiServer) {
        return new RealDevicesEndpoint(this.username, this.accessKey, apiServer);
    }

    public SauceConnectEndpoint getSauceConnectEndpoint() {
        return new SauceConnectEndpoint(this.username, this.accessKey, this.apiServer);
    }

    public SauceConnectEndpoint getSauceConnectEndpoint(String apiServer) {
        return new SauceConnectEndpoint(this.username, this.accessKey, apiServer);
    }

    public SauceConnectEndpoint getSauceConnectEndpoint(DataCenter dataCenter) {
        return new SauceConnectEndpoint(this.username, this.accessKey, dataCenter);
    }

    public AccountsEndpoint getAccountsEndpoint() {
        return new AccountsEndpoint(this.username, this.accessKey, this.apiServer);
    }

    public AccountsEndpoint getAccountsEndpoint(String apiServer) {
        return new AccountsEndpoint(this.username, this.accessKey, apiServer);
    }

    public AccountsEndpoint getAccountsEndpoint(DataCenter dataCenter) {
        return new AccountsEndpoint(this.username, this.accessKey, dataCenter);
    }

    public BuildsEndpoint getBuildsEndpoint() {
        return new BuildsEndpoint(this.username, this.accessKey, this.apiServer);
    }

    public BuildsEndpoint getBuildsEndpoint(String apiServer) {
        return new BuildsEndpoint(this.username, this.accessKey, apiServer);
    }

    public BuildsEndpoint getBuildsEndpoint(DataCenter dataCenter) {
        return new BuildsEndpoint(this.username, this.accessKey, dataCenter);
    }

    public InsightsEndpoint getInsightsEndpoint() {
        return new InsightsEndpoint(this.username, this.accessKey, this.apiServer);
    }

    public InsightsEndpoint getInsightsEndpoint(String apiServer) {
        return new InsightsEndpoint(this.username, this.accessKey, apiServer);
    }

    public InsightsEndpoint getInsightsEndpoint(DataCenter dataCenter) {
        return new InsightsEndpoint(this.username, this.accessKey, dataCenter);
    }

}