package com.saucelabs.saucerest.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.PerformanceEndpoint;
import com.saucelabs.saucerest.model.performance.GetPerformanceMetricsParameter;
import com.saucelabs.saucerest.model.performance.PerformanceMetrics;
import java.io.IOException;
import java.time.LocalDateTime;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class PerformanceEndpointTest {
    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getPerformanceResultsTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        PerformanceEndpoint performanceEndpoint = sauceREST.getPerformanceEndpoint();

        GetPerformanceMetricsParameter parameter = new GetPerformanceMetricsParameter.Builder()
            .setStartDate(LocalDateTime.now().minusDays(30))
            .setEndDate(LocalDateTime.now())
            .build();
        PerformanceMetrics performanceMetrics = performanceEndpoint.getPerformanceResults(parameter);

        assertNotNull(performanceMetrics.items);
    }
}
