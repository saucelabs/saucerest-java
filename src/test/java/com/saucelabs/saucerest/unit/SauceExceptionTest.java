package com.saucelabs.saucerest.unit;

import com.saucelabs.saucerest.SauceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SauceExceptionTest {

    public static final String MESSAGE = "This is a test message";

    @Test
    public void testDefaultConstructor() {
        SauceException exception = new SauceException();
        assertNull(exception.getMessage());
    }

    @Test
    public void testMessageConstructor() {
        SauceException exception = new SauceException(MESSAGE);
        assertEquals(MESSAGE, exception.getMessage());
    }

    @Test
    public void testUnknownErrorConstructor() {
        SauceException.UnknownError exception = new SauceException.UnknownError(MESSAGE);
        assertEquals(MESSAGE, exception.getMessage());
    }

    @Test
    public void testNotAuthorizedConstructor() {
        SauceException.NotAuthorized exception = new SauceException.NotAuthorized(MESSAGE);
        assertEquals(MESSAGE, exception.getMessage());
    }

    @Test
    public void testNotFoundConstructor() {
        SauceException.NotFound exception = new SauceException.NotFound(MESSAGE);
        assertEquals(MESSAGE, exception.getMessage());
    }

    @Test
    public void testTooManyRequestsConstructor() {
        assertThrows(SauceException.TooManyRequests.class, () -> {
            throw new SauceException.TooManyRequests();
        });
    }

    @Test
    public void testNotYetDoneConstructor() {
        SauceException.NotYetDone exception = new SauceException.NotYetDone(MESSAGE);
        assertEquals(MESSAGE, exception.getMessage());
    }

    @Test
    public void testResigningNotAllowedConstructor() {
        SauceException.ResigningNotAllowed exception = new SauceException.ResigningNotAllowed(MESSAGE);
        assertEquals(MESSAGE, exception.getMessage());
    }

    @Test
    public void testInstrumentationNotAllowedConstructor() {
        SauceException.InstrumentationNotAllowed exception = new SauceException.InstrumentationNotAllowed(MESSAGE);
        assertEquals(MESSAGE, exception.getMessage());
    }

    @Test
    public void testDeviceLockOnlyOnAndroidConstructor() {
        SauceException.DeviceLockOnlyOnAndroid exception = new SauceException.DeviceLockOnlyOnAndroid(MESSAGE);
        assertEquals(MESSAGE, exception.getMessage());
    }
}