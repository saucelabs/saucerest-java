package com.saucelabs.saucerest.unit;

import com.saucelabs.saucerest.SauceShareableLink;
import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SauceShareableLinkTest {

    public static final String EXPECTED_DIGEST = "0a7a857b4eba79ef23eefbfa332a10c7";
    private static final String USERNAME = "myUsername";
    private static final String ACCESS_KEY = "myAccessKey";
    private static final String JOB_ID = "12345";
    private static final String SERVER = "https://saucelabs.com/rest/v1/";

    @Test
    public void testGetJobAuthDigest() throws NoSuchAlgorithmException, InvalidKeyException {
        String actualDigest = SauceShareableLink.getJobAuthDigest(USERNAME, ACCESS_KEY, JOB_ID);
        assertEquals(EXPECTED_DIGEST, actualDigest);
    }

    @Test
    public void testGetShareableLink() throws NoSuchAlgorithmException, InvalidKeyException {
        String expectedLink = "https://saucelabs.com/rest/v1/tests/" + JOB_ID + "?auth=" + EXPECTED_DIGEST;
        String actualLink = SauceShareableLink.getShareableLink(USERNAME, ACCESS_KEY, JOB_ID, SERVER);
        assertEquals(expectedLink, actualLink);
    }
}