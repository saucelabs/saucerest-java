package com.saucelabs.saucerest.unit;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SauceRESTTest {

    private final SauceREST sauceREST = new SauceREST("testuser", "testkey", DataCenter.US_WEST);

    @Test
    public void testGetUsername() {
        assertEquals("testuser", sauceREST.getUsername());
    }

    @Test
    public void testGetAccessKey() {
        assertEquals("testkey", sauceREST.getAccessKey());
    }

    @Test
    public void testGetServer() {
        assertEquals("https://saucelabs.com/", sauceREST.getServer());
    }

    @Test
    public void testGetAppServer() {
        assertEquals("https://app.saucelabs.com/", sauceREST.getAppServer());
    }

    @Test
    public void testGetEdsServer() {
        assertEquals("https://api.us-west-1.saucelabs.com/v1/eds/", sauceREST.getEdsServer());
    }

    @Test
    public void testGetJobsEndpoint() {
        JobsEndpoint jobsEndpoint = sauceREST.getJobsEndpoint();
        assertNotNull(jobsEndpoint);
    }

    @Test
    public void testGetJobsEndpointWithDataCenter() {
        JobsEndpoint jobsEndpoint = sauceREST.getJobsEndpoint(DataCenter.US_EAST);
        assertNotNull(jobsEndpoint);
    }

    @Test
    public void testGetJobsEndpointWithApiServer() {
        JobsEndpoint jobsEndpoint = sauceREST.getJobsEndpoint("https://eu-central-1.saucelabs.com/rest/v1");
        assertNotNull(jobsEndpoint);
    }

    @Test
    public void testGetStorageEndpoint() {
        StorageEndpoint storageEndpoint = sauceREST.getStorageEndpoint();
        assertNotNull(storageEndpoint);
    }

    @Test
    public void testGetStorageEndpointWithDataCenter() {
        StorageEndpoint storageEndpoint = sauceREST.getStorageEndpoint(DataCenter.US_EAST);
        assertNotNull(storageEndpoint);
    }

    @Test
    public void testGetStorageEndpointWithApiServer() {
        StorageEndpoint storageEndpoint = sauceREST.getStorageEndpoint("https://eu-central-1.saucelabs.com/rest/v1/storage");
        assertNotNull(storageEndpoint);
    }

    @Test
    public void testGetPlatformEndpoint() {
        PlatformEndpoint platformEndpoint = sauceREST.getPlatformEndpoint();
        assertNotNull(platformEndpoint);
    }

    @Test
    public void testGetPlatformEndpointWithDataCenter() {
        PlatformEndpoint platformEndpoint = sauceREST.getPlatformEndpoint(DataCenter.US_EAST);
        assertNotNull(platformEndpoint);
    }

    @Test
    public void testGetPlatformEndpointWithApiServer() {
        PlatformEndpoint platformEndpoint = sauceREST.getPlatformEndpoint("https://eu-central-1.saucelabs.com/rest/v1/platforms");
        assertNotNull(platformEndpoint);
    }

    @Test
    public void testGetRealDevicesEndpoint() {
        RealDevicesEndpoint realDevicesEndpoint = sauceREST.getRealDevicesEndpoint();
        assertNotNull(realDevicesEndpoint);
    }

    @Test
    public void testGetRealDevicesEndpointWithDataCenter() {
        RealDevicesEndpoint realDevicesEndpoint = sauceREST.getRealDevicesEndpoint(DataCenter.US_EAST);
        assertNotNull(realDevicesEndpoint);
    }

    @Test
    public void testGetRealDevicesEndpointWithApiServer() {
        RealDevicesEndpoint realDevicesEndpoint = sauceREST.getRealDevicesEndpoint("https://eu-central-1.saucelabs.com/rest/v1/rdc");
        assertNotNull(realDevicesEndpoint);
    }

    @Test
    public void testGetSauceConnectEndpoint() {
        SauceConnectEndpoint sauceConnectEndpoint = sauceREST.getSauceConnectEndpoint();
        assertNotNull(sauceConnectEndpoint);
    }

    @Test
    public void testGetSauceConnectEndpointWithDataCenter() {
        SauceConnectEndpoint sauceConnectEndpoint = sauceREST.getSauceConnectEndpoint(DataCenter.US_EAST);
        assertNotNull(sauceConnectEndpoint);
    }

    @Test
    public void testGetSauceConnectEndpointWithApiServer() {
        SauceConnectEndpoint sauceConnectEndpoint = sauceREST.getSauceConnectEndpoint("https://eu-central-1.saucelabs.com/rest/v1/sc");
        assertNotNull(sauceConnectEndpoint);
    }

    @Test
    public void testGetAccountsEndpoint() {
        AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();
        assertNotNull(accountsEndpoint);
    }

    @Test
    public void testGetAccountsEndpointWithDataCenter() {
        AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint(DataCenter.US_EAST);
        assertNotNull(accountsEndpoint);
    }

    @Test
    public void testGetAccountsEndpointWithApiServer() {
        AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint("https://eu-central-1.saucelabs.com/rest/v1/users");
        assertNotNull(accountsEndpoint);
    }

    @Test
    public void testGetBuildsEndpoint() {
        BuildsEndpoint buildsEndpoint = sauceREST.getBuildsEndpoint();
        assertNotNull(buildsEndpoint);
    }

    @Test
    public void testGetBuildsEndpointWithDataCenter() {
        BuildsEndpoint buildsEndpoint = sauceREST.getBuildsEndpoint(DataCenter.US_EAST);
        assertNotNull(buildsEndpoint);
    }

    @Test
    public void testGetBuildsEndpointWithApiServer() {
        BuildsEndpoint buildsEndpoint = sauceREST.getBuildsEndpoint("https://eu-central-1.saucelabs.com/rest/v1/builds");
        assertNotNull(buildsEndpoint);
    }

    @Test
    public void testGetBuildsEndpointWithoutSauceREST() {
        BuildsEndpoint buildsEndpoint = new BuildsEndpoint(DataCenter.US_WEST);
        assertNotNull(buildsEndpoint);
    }

    @Test
    public void testGetBuildsEndpointWithoutSauceRESTAndAPIServer() {
        BuildsEndpoint buildsEndpoint = new BuildsEndpoint("https://eu-central-1.saucelabs.com/rest/v1/builds");
        assertNotNull(buildsEndpoint);
    }

    //TODO add more unit tests for the other constructors
}