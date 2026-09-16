package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.saucelabs.saucerest.model.insights.ErrorsParameter;
import com.saucelabs.saucerest.model.insights.Interval;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ErrorsParameterTest {

    @Test
    public void testToMap() {
        ErrorsParameter params = new ErrorsParameter.Builder("org123")
                .setInterval(Interval.ONE_HOUR)
                .setUserId("user123")
                .setLimit(10)
                .setOffset(0)
                .build();

        Map<String, Object> map = params.toMap();
        assertEquals("org123", map.get("org_id"));
        assertEquals("1h", map.get("interval"));
        assertEquals("user123", map.get("user_id"));
        assertEquals(10, map.get("limit"));
        assertEquals(0, map.get("offset"));
    }

    @Test
    public void testToMapWithOnlyRequiredValues() {
        ErrorsParameter params = new ErrorsParameter.Builder("org123").build();

        Map<String, Object> map = params.toMap();
        assertEquals(1, map.size());
        assertEquals("org123", map.get("org_id"));
    }
}
