package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.saucelabs.saucerest.model.performance.GetPerformanceMetricsParameter;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class GetPerformanceMetricsParameterTest {

    @Test
    public void testToMap() {
        GetPerformanceMetricsParameter params = new GetPerformanceMetricsParameter.Builder()
                .setPageUrl("https://saucelabs.com/")
                .setMetricNames(new String[] {"speedIndex", "load"})
                .setStartDate(LocalDateTime.of(2024, 1, 1, 0, 0, 0))
                .setEndDate(LocalDateTime.of(2024, 1, 2, 0, 0, 0))
                .build();

        Map<String, Object> map = params.toMap();
        assertEquals("https://saucelabs.com/", map.get("page_url"));
        assertArrayEquals(new String[] {"speedIndex", "load"}, (String[]) map.get("metric_names"));
        assertTrue(((String) map.get("start_date")).matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z"));
        assertTrue(((String) map.get("end_date")).matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z"));
    }

    @Test
    public void testToMapWithNoValues() {
        GetPerformanceMetricsParameter params = new GetPerformanceMetricsParameter.Builder().build();

        Map<String, Object> map = params.toMap();
        assertTrue(map.isEmpty());
    }
}
