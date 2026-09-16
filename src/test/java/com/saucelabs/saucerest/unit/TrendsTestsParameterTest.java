package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.saucelabs.saucerest.model.insights.Interval;
import com.saucelabs.saucerest.model.insights.TrendsTestsParameter;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class TrendsTestsParameterTest {

    @Test
    public void testToMap() {
        TrendsTestsParameter params = new TrendsTestsParameter.Builder("org123")
                .setInterval(Interval.SIX_HOURS)
                .setTimeZone("+01:00")
                .build();

        Map<String, Object> map = params.toMap();
        assertEquals("org123", map.get("org_id"));
        assertEquals("6h", map.get("interval"));
        assertEquals("+01:00", map.get("time_zone"));
    }

    @Test
    public void testToMapWithOnlyRequiredValues() {
        TrendsTestsParameter params = new TrendsTestsParameter.Builder("org123").build();

        Map<String, Object> map = params.toMap();
        assertEquals(1, map.size());
        assertEquals("org123", map.get("org_id"));
    }
}
