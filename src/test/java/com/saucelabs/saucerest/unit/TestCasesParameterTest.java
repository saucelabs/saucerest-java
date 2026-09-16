package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.saucelabs.saucerest.model.insights.Interval;
import com.saucelabs.saucerest.model.insights.Sort;
import com.saucelabs.saucerest.model.insights.Status;
import com.saucelabs.saucerest.model.insights.TestCasesParameter;
import com.saucelabs.saucerest.model.insights.TestCasesSortBy;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class TestCasesParameterTest {

    @Test
    public void testToMap() {
        TestCasesParameter params = new TestCasesParameter.Builder("org123")
                .setInterval(Interval.SEVEN_DAYS)
                .setStatus(new Status[] {Status.PASSED, Status.FAILED})
                .setLimit(25)
                .setOffset(5)
                .setSortBy(TestCasesSortBy.AVG_DURATION)
                .setSort(Sort.DESC)
                .build();

        Map<String, Object> map = params.toMap();
        assertEquals("org123", map.get("org_id"));
        assertEquals("7d", map.get("interval"));
        assertArrayEquals(new String[] {"passed", "failed"}, (String[]) map.get("status"));
        assertEquals(25, map.get("limit"));
        assertEquals(5, map.get("offset"));
        assertEquals("avg_duration", map.get("sort_by"));
        assertEquals("desc", map.get("sort"));
    }

    @Test
    public void testToMapWithOnlyRequiredValues() {
        TestCasesParameter params = new TestCasesParameter.Builder("org123").build();

        Map<String, Object> map = params.toMap();
        assertEquals(1, map.size());
        assertEquals("org123", map.get("org_id"));
    }
}
