package com.saucelabs.saucerest.unit;

import com.saucelabs.saucerest.AutomationBackend;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AutomationBackendTest {
    @Test
    public void testLabel() {
        assertAll("AutomationBackend label",
            () -> assertEquals("appium", AutomationBackend.APPIUM.label),
            () -> assertEquals("webdriver", AutomationBackend.WEBDRIVER.label)
        );
    }
}