package com.saucelabs.saucerest.unit;

import com.saucelabs.saucerest.HttpMethod;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpMethodTest {
    @Test
    public void testLabel() {
        assertAll("HttpMethod label",
            () -> assertEquals("GET", HttpMethod.GET.label),
            () -> assertEquals("POST", HttpMethod.POST.label),
            () -> assertEquals("PUT", HttpMethod.PUT.label),
            () -> assertEquals("DELETE", HttpMethod.DELETE.label),
            () -> assertEquals("PATCH", HttpMethod.PATCH.label),
            () -> assertEquals("HEAD", HttpMethod.HEAD.label),
            () -> assertEquals("OPTIONS", HttpMethod.OPTIONS.label)
        );
    }
}