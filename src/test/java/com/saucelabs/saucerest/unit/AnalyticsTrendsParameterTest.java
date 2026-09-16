package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.saucelabs.saucerest.model.insights.AnalyticsTrendsParameter;
import com.saucelabs.saucerest.model.insights.Interval;
import com.saucelabs.saucerest.model.insights.TimeRange;
import com.saucelabs.saucerest.model.insights.Unit;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class AnalyticsTrendsParameterTest {

    @Test
    public void testToMapWithTimeRange() {
        AnalyticsTrendsParameter params = new AnalyticsTrendsParameter.Builder()
                .setTimeRange(new TimeRange(1, Unit.H))
                .setInterval(Interval.FIFTEEN_MINUTES)
                .build();

        Map<String, Object> map = params.toMap();
        assertEquals("1h", map.get("time_range"));
        assertEquals("15m", map.get("interval"));
    }

    @Test
    public void testBuildThrowsWhenNeitherTimeRangeNorStartEndSet() {
        assertThrows(IllegalStateException.class, () -> new AnalyticsTrendsParameter.Builder().build());
    }
}
