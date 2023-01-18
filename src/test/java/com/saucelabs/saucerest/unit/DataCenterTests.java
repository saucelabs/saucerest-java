package com.saucelabs.saucerest.unit;

import com.saucelabs.saucerest.DataCenter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class DataCenterTests
{
    @ParameterizedTest
    @CsvSource({
        "US_WEST,        US_WEST",
        "us_WeSt,        US_WEST",
        "EU_CENTRAL,     EU_CENTRAL",
        "Eu_central,     EU_CENTRAL",
        "US_EAST,        US_EAST",
        "us_EaSt,        US_EAST",
        "APAC_SOUTHEAST, APAC_SOUTHEAST",
        "apac_southeast, APAC_SOUTHEAST",
    })
    void testFromString(String input, DataCenter expected)
    {
        assertEquals(expected, DataCenter.fromString(input));
    }

    @Test
    void testInvalidString() {
        assertNull(DataCenter.fromString("unknown"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    void testEdsServer(DataCenter datacenter)
    {
        assertTrue(datacenter.edsServer().endsWith("saucelabs.com/v1/eds/"));
    }
}
