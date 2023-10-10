package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.*;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceShareableLink;
import org.junit.jupiter.api.Test;

public class SauceShareableLinkTest {
    @Test
    void testGetShareableLinkWithValidInputs() {
        String shareableLink = SauceShareableLink.getShareableLink("1234", DataCenter.EU_CENTRAL);

        assertAll(
            () -> assertNotNull(shareableLink),
            () -> assertFalse(shareableLink.isEmpty()),
            () -> assertTrue(shareableLink.contains("1234")),
            () -> assertTrue(shareableLink.contains(DataCenter.EU_CENTRAL.appServer))
        );
    }

    @Test
    void testGetShareableLinkWithDefaultCredentials() {
        String shareableLink = SauceShareableLink.getShareableLink("username", "accessKey", "1234", DataCenter.EU_CENTRAL);
        String expectedShareableLink = "https://app.eu-central-1.saucelabs.com/tests/1234?auth=6cfae591bc67fc04059684ce9b737f81";

        assertAll(
            () -> assertNotNull(shareableLink),
            () -> assertFalse(shareableLink.isEmpty()),
            () -> assertTrue(shareableLink.contains("1234")),
            () -> assertTrue(shareableLink.contains(DataCenter.EU_CENTRAL.appServer)),
            () -> assertEquals(expectedShareableLink, shareableLink)
        );
    }

    @Test
    void testGetShareableLinkWithEmptySauceJobId() {
        assertThrows(IllegalArgumentException.class, () -> {
            SauceShareableLink.getShareableLink(null, null, "", DataCenter.EU_CENTRAL);
        });
    }

    @Test
    void testGetShareableLinkWithNullDataCenter() {
        assertThrows(IllegalArgumentException.class, () -> {
            SauceShareableLink.getShareableLink("username", "accessKey", "1234", null);
        });
    }
}