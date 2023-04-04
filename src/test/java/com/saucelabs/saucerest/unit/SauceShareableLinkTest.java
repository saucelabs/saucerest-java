package com.saucelabs.saucerest.unit;

import com.saucelabs.saucerest.SauceShareableLink;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SauceShareableLinkTest {

    public static final String EXPECTED_DIGEST = "0a7a857b4eba79ef23eefbfa332a10c7";
    private static final String USERNAME = "myUsername";
    private static final String ACCESS_KEY = "myAccessKey";
    private static final String JOB_ID = "12345";
    private static final String SERVER = "https://saucelabs.com/rest/v1/";

    @Test
    public void testGetShareableLink() throws NoSuchAlgorithmException, InvalidKeyException {
        String expectedLink = "https://saucelabs.com/rest/v1/tests/" + JOB_ID + "?auth=" + EXPECTED_DIGEST;
        String actualLink = SauceShareableLink.getShareableLink(USERNAME, ACCESS_KEY, JOB_ID, SERVER);
        assertEquals(expectedLink, actualLink);
    }

    @Test
    void testGetShareableLinkWithNullSauceJobId() {
        assertThrows(IllegalArgumentException.class, () -> SauceShareableLink.getShareableLink("myUsername", "myAccessKey", null, "https://mydatacenter.saucelabs.com/"));
    }

    @Test
    void testGetShareableLinkWithEmptySauceJobId() {
        assertThrows(IllegalArgumentException.class, () -> SauceShareableLink.getShareableLink("myUsername", "myAccessKey", "", "https://mydatacenter.saucelabs.com/"));
    }

    @Test
    void testGetShareableLinkWithNullDataCenterEndpoint() {
        assertThrows(IllegalArgumentException.class, () -> SauceShareableLink.getShareableLink("myUsername", "myAccessKey", "12345", null));
    }

    @Test
    void testGetShareableLinkWithEmptyDataCenterEndpoint() {
        assertThrows(IllegalArgumentException.class, () -> SauceShareableLink.getShareableLink("myUsername", "myAccessKey", "12345", ""));
    }

    @Test
    @Disabled
    public void testGetShareableLinkWithDefaultUsername() throws NoSuchAlgorithmException, InvalidKeyException {
        String defaultUsername = "myDefaultUsername";
        System.setProperty("SAUCE_USERNAME", defaultUsername);

        String accessKey = "myAccessKey";
        String sauceJobId = "myJobId";
        String dataCenterEndpoint = "https://my-datacenter.saucelabs.com/rest/v1/";

        String expectedLink = "https://my-datacenter.saucelabs.com/rest/v1/tests/myJobId?auth=f0ea74946bbe5c0e531dedf5a4445d7a";

        String actualLink = SauceShareableLink.getShareableLink(null, accessKey, sauceJobId, dataCenterEndpoint);

        assertEquals(expectedLink, actualLink);
    }

    @Test
    @Disabled
    public void testGetShareableLinkWithDefaultAccessKey() throws NoSuchAlgorithmException, InvalidKeyException {
        String defaultAccessKey = "myDefaultAccessKey";
        System.setProperty("SAUCE_ACCESS_KEY", defaultAccessKey);

        String username = "myUsername";
        String sauceJobId = "myJobId";
        String dataCenterEndpoint = "https://my-datacenter.saucelabs.com/rest/v1/";

        String expectedLink = "https://my-datacenter.saucelabs.com/rest/v1/tests/myJobId?auth=1c6e5cb81e1bbb43869debe34d81c3fe";

        String actualLink = SauceShareableLink.getShareableLink(username, null, sauceJobId, dataCenterEndpoint);

        assertEquals(expectedLink, actualLink);
    }
}