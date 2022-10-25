package com.saucelabs.saucerest.unit;

import com.saucelabs.saucerest.DataCenter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataCenterTests
{
    @CsvSource({
        "US,      US",
        "us,      US",
        "EU,      EU",
        "Eu,      EU",
        "US_EAST, US_EAST",
        "us_EaSt, US_EAST",
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
