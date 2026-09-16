package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.saucelabs.saucerest.model.insights.Scope;
import com.saucelabs.saucerest.model.insights.TestMetricsParameter;
import com.saucelabs.saucerest.model.insights.TestResultParameter;
import com.saucelabs.saucerest.model.insights.TimeRange;
import com.saucelabs.saucerest.model.insights.Unit;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class TestMetricsParameterTest {

    @Test
    public void testToMapWithStartAndEnd() {
        TestMetricsParameter params = new TestMetricsParameter.Builder("my test")
                .setStart(LocalDateTime.of(2024, 1, 1, 0, 0, 0))
                .setEnd(LocalDateTime.of(2024, 1, 2, 0, 0, 0))
                .setScope(Scope.ORGANIZATION)
                .setStatus(TestResultParameter.Status.PASSED)
                .build();

        Map<String, Object> map = params.toMap();
        assertEquals("my test", map.get("query"));
        assertEquals("organization", map.get("scope"));
        assertEquals("passed", map.get("status"));
        assertTrue(((String) map.get("start")).matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z"));
        assertTrue(((String) map.get("end")).matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z"));
    }

    @Test
    public void testToMapWithTimeRange() {
        TestMetricsParameter params = new TestMetricsParameter.Builder("my test")
                .setTimeRange(new TimeRange(7, Unit.D))
                .build();

        Map<String, Object> map = params.toMap();
        assertEquals("my test", map.get("query"));
        assertEquals("7d", map.get("time_range"));
    }

    @Test
    public void testBuildThrowsWhenNeitherTimeRangeNorStartEndSet() {
        assertThrows(IllegalStateException.class, () -> new TestMetricsParameter.Builder("my test").build());
    }

    @Test
    public void testBuildThrowsWhenBothTimeRangeAndStartEndSet() {
        assertThrows(
                IllegalStateException.class,
                () -> new TestMetricsParameter.Builder("my test")
                        .setStart(LocalDateTime.of(2024, 1, 1, 0, 0, 0))
                        .setEnd(LocalDateTime.of(2024, 1, 2, 0, 0, 0))
                        .setTimeRange(new TimeRange(7, Unit.D))
                        .build());
    }
}
