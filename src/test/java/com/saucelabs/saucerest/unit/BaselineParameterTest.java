package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.saucelabs.saucerest.model.performance.BaselineParameter;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class BaselineParameterTest {

    @Test
    public void testToMap() {
        BaselineParameter params = new BaselineParameter.Builder(new String[] {"speedIndex"}, 0)
                .setRegimeStart(0)
                .setRegimeEnd(5)
                .build();

        Map<String, Object> map = params.toMap();
        assertArrayEquals(new String[] {"speedIndex"}, (String[]) map.get("metric_names"));
        assertEquals(0, map.get("order_index"));
        assertEquals(0, map.get("regime_start"));
        assertEquals(5, map.get("regime_end"));
    }

    @Test
    public void testToMapWithOnlyRequiredValues() {
        BaselineParameter params = new BaselineParameter.Builder(new String[] {"speedIndex"}, 2).build();

        Map<String, Object> map = params.toMap();
        assertEquals(2, map.size());
        assertArrayEquals(new String[] {"speedIndex"}, (String[]) map.get("metric_names"));
        assertEquals(2, map.get("order_index"));
    }
}
