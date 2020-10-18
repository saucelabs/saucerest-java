package com.saucelabs.saucerest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
}
