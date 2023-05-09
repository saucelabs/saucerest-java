package com.saucelabs.saucerest.unit;

import com.saucelabs.saucerest.model.builds.LookupBuildsParameters;
import com.saucelabs.saucerest.model.builds.Status;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LookupBuildsParametersTest {

    @Test
    public void testToMap() {
        LookupBuildsParameters params = new LookupBuildsParameters.Builder()
                .setUserID("user123")
                .setOrgID("org456")
                .setStatus(new Status[]{Status.success, Status.failed})
                .setStart(0)
                .setEnd(10)
                .setLimit(50)
                .setName("build-1")
                .setOffset(10)
                .build();

        Map<String, Object> map = params.toMap();
        assertEquals("user123", map.get("user_id"));
        assertEquals("org456", map.get("org_id"));
        assertArrayEquals(new Status[]{Status.success, Status.failed}, (Status[]) map.get("status"));
        assertEquals(0, map.get("start"));
        assertEquals(10, map.get("end"));
        assertEquals(50, map.get("limit"));
        assertEquals("build-1", map.get("name"));
        assertEquals(10, map.get("offset"));
    }

    @Test
    public void testToMapWithNullValues() {
        LookupBuildsParameters params = new LookupBuildsParameters.Builder()
                .setUserID(null)
                .setOrgID(null)
                .setStatus(null)
                .setStart(null)
                .setEnd(null)
                .setLimit(null)
                .setName(null)
                .setOffset(null)
                .setSort(null)
                .build();

        Map<String, Object> map = params.toMap();
        assertTrue(map.isEmpty());
    }
}