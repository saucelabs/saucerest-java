package com.saucelabs.saucerest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

class DataCenterTests
{
    @CsvSource({
        "US,      US",
        "us,      US",
        "EU,      EU",
        "Eu,      EU",
        "US_EAST, US_EAST",
        "us_EaSt, US_EAST",
        "unknown, US",
    })
    @ParameterizedTest
    void testFromString(String input, DataCenter expected)
    {
        assertEquals(expected, DataCenter.fromString(input));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    void testEdsServer(DataCenter datacenter)
    {
        assertTrue(datacenter.edsServer().endsWith("saucelabs.com/v1/eds/"));
    }
}
