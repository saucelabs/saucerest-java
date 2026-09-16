package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.saucelabs.saucerest.model.insights.DeviceGroup;
import com.saucelabs.saucerest.model.insights.Sort;
import com.saucelabs.saucerest.model.insights.TagFilterMode;
import com.saucelabs.saucerest.model.insights.TestsParameter;
import com.saucelabs.saucerest.model.insights.TestsSortBy;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class TestsParameterTest {

    @Test
    public void testToMap() {
        TestsParameter params = new TestsParameter.Builder("org123")
                .setUserId("user123")
                .setGroupId("group123")
                .setTeamId("team123")
                .setBuild(new String[] {"build1"})
                .setOs(new String[] {"Windows 10"})
                .setDevice(new String[] {"iPhone"})
                .setBrowser(new String[] {"chrome"})
                .setTag(new String[] {"smoke"})
                .setTagFilterMode(TagFilterMode.AND)
                .setError(new String[] {"timeout"})
                .setName("my test")
                .setStart(LocalDateTime.of(2024, 1, 1, 0, 0, 0))
                .setEnd(LocalDateTime.of(2024, 1, 2, 0, 0, 0))
                .setLimit(50)
                .setOffset(0)
                .setSortBy(TestsSortBy.DURATION)
                .setSort(Sort.ASC)
                .setAutomationBackend(new String[] {"webdriver"})
                .setDeviceGroup(DeviceGroup.PUBLIC)
                .build();

        Map<String, Object> map = params.toMap();
        assertEquals("org123", map.get("org_id"));
        assertEquals("user123", map.get("user_id"));
        assertEquals("group123", map.get("group_id"));
        assertEquals("team123", map.get("team_id"));
        assertEquals("and", map.get("tag_filter_mode"));
        assertEquals("my test", map.get("name"));
        assertEquals(50, map.get("limit"));
        assertEquals(0, map.get("offset"));
        assertEquals("duration", map.get("sort_by"));
        assertEquals("asc", map.get("sort"));
        assertEquals("public", map.get("device_group"));
        assertTrue(((String) map.get("start")).matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z"));
        assertTrue(((String) map.get("end")).matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z"));
    }

    @Test
    public void testToMapWithOnlyRequiredValues() {
        TestsParameter params = new TestsParameter.Builder("org123").build();

        Map<String, Object> map = params.toMap();
        assertEquals(1, map.size());
        assertEquals("org123", map.get("org_id"));
    }
}
