package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.saucelabs.saucerest.model.insights.Interval;
import com.saucelabs.saucerest.model.insights.TrendsErrorsParameter;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class TrendsErrorsParameterTest {

    @Test
    public void testToMap() {
        TrendsErrorsParameter params = new TrendsErrorsParameter.Builder("org123")
                .setInterval(Interval.THIRTY_DAYS)
                .setTeamId("team123")
                .build();

        Map<String, Object> map = params.toMap();
        assertEquals("org123", map.get("org_id"));
        assertEquals("30d", map.get("interval"));
        assertEquals("team123", map.get("team_id"));
    }

    @Test
    public void testToMapWithOnlyRequiredValues() {
        TrendsErrorsParameter params = new TrendsErrorsParameter.Builder("org123").build();

        Map<String, Object> map = params.toMap();
        assertEquals(1, map.size());
        assertEquals("org123", map.get("org_id"));
    }
}
