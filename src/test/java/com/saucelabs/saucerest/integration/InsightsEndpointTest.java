package com.saucelabs.saucerest.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.InsightsEndpoint;
import com.saucelabs.saucerest.model.insights.TestResult;
import com.saucelabs.saucerest.model.insights.TestResultParameter;
import java.io.IOException;
import java.time.LocalDateTime;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class InsightsEndpointTest {
    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getTestResultTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        InsightsEndpoint insightsEndpoint = sauceREST.getInsightsEndpoint();

        int startYear = LocalDateTime.now().getYear();
        int startMonth = LocalDateTime.now().getMonthValue();

        TestResultParameter parameter = new TestResultParameter.Builder()
            .setStart(LocalDateTime.of(startYear, startMonth, 1, 0, 0, 0))
            .setEnd(LocalDateTime.now())
            .build();
        TestResult testResult = insightsEndpoint.getTestResults(parameter);

        assertTrue(testResult.items.size() > 0);
    }
}