package com.saucelabs.saucerest.unit;

import com.saucelabs.saucerest.SauceShareableLink;
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
}