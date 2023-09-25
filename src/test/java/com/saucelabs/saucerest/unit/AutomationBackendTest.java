package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.saucelabs.saucerest.AutomationBackend;
import org.junit.jupiter.api.Test;

public class AutomationBackendTest {
    @Test
    public void testLabel() {
        assertAll("AutomationBackend label",
            () -> assertEquals("appium", AutomationBackend.APPIUM.label),
            () -> assertEquals("webdriver", AutomationBackend.WEBDRIVER.label)
        );
    }
}