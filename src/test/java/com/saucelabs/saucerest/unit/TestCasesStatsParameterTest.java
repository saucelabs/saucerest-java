package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.saucelabs.saucerest.model.insights.DeviceGroup;
import com.saucelabs.saucerest.model.insights.Interval;
import com.saucelabs.saucerest.model.insights.TestCasesStatsParameter;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class TestCasesStatsParameterTest {

    @Test
    public void testToMap() {
        TestCasesStatsParameter params = new TestCasesStatsParameter.Builder("org123")
                .setInterval(Interval.ONE_DAY)
                .setDeviceGroup(DeviceGroup.PRIVATE)
                .build();

        Map<String, Object> map = params.toMap();
        assertEquals("org123", map.get("org_id"));
        assertEquals("1d", map.get("interval"));
        assertEquals("private", map.get("device_group"));
    }

    @Test
    public void testToMapWithOnlyRequiredValues() {
        TestCasesStatsParameter params = new TestCasesStatsParameter.Builder("org123").build();

        Map<String, Object> map = params.toMap();
        assertEquals(1, map.size());
        assertEquals("org123", map.get("org_id"));
    }
}
