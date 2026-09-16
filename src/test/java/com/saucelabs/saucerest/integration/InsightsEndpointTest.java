package com.saucelabs.saucerest.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.JobSource;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.AccountsEndpoint;
import com.saucelabs.saucerest.api.InsightsEndpoint;
import com.saucelabs.saucerest.model.insights.AnalyticsTrendsParameter;
import com.saucelabs.saucerest.model.insights.Errors;
import com.saucelabs.saucerest.model.insights.ErrorsParameter;
import com.saucelabs.saucerest.model.insights.TestCases;
import com.saucelabs.saucerest.model.insights.TestCasesParameter;
import com.saucelabs.saucerest.model.insights.TestCasesStats;
import com.saucelabs.saucerest.model.insights.TestCasesStatsParameter;
import com.saucelabs.saucerest.model.insights.TestMetrics;
import com.saucelabs.saucerest.model.insights.TestMetricsParameter;
import com.saucelabs.saucerest.model.insights.TestResult;
import com.saucelabs.saucerest.model.insights.TestResultParameter;
import com.saucelabs.saucerest.model.insights.Tests;
import com.saucelabs.saucerest.model.insights.TestTrends;
import com.saucelabs.saucerest.model.insights.TestsParameter;
import com.saucelabs.saucerest.model.insights.TrendsErrors;
import com.saucelabs.saucerest.model.insights.TrendsErrorsParameter;
import com.saucelabs.saucerest.model.insights.TrendsTestsParameter;
import java.io.IOException;
import java.time.LocalDateTime;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class InsightsEndpointTest {
    private static String getOrgId(SauceREST sauceREST) throws IOException {
        AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();
        return accountsEndpoint.getOrganization().results.get(0).id;
    }

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

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getTestMetricsSummaryTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        InsightsEndpoint insightsEndpoint = sauceREST.getInsightsEndpoint();

        TestMetricsParameter parameter = new TestMetricsParameter.Builder("AnalyticsSeleniumTest")
            .setStart(LocalDateTime.now().minusDays(30))
            .setEnd(LocalDateTime.now())
            .build();
        TestMetrics testMetrics = insightsEndpoint.getTestMetricsSummary(parameter);

        assertNotNull(testMetrics.aggs);
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getTestTrendsTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        InsightsEndpoint insightsEndpoint = sauceREST.getInsightsEndpoint();

        AnalyticsTrendsParameter parameter = new AnalyticsTrendsParameter.Builder()
            .setStart(LocalDateTime.now().minusDays(30))
            .setEnd(LocalDateTime.now())
            .build();
        TestTrends testTrends = insightsEndpoint.getTestTrends(parameter);

        assertNotNull(testTrends.buckets);
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getTestsTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        InsightsEndpoint insightsEndpoint = sauceREST.getInsightsEndpoint();

        TestsParameter parameter = new TestsParameter.Builder(getOrgId(sauceREST))
            .setStart(LocalDateTime.now().minusDays(30))
            .setEnd(LocalDateTime.now())
            .build();
        Tests tests = insightsEndpoint.getTests(JobSource.VDC, parameter);

        assertNotNull(tests.items);
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getTestCasesTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        InsightsEndpoint insightsEndpoint = sauceREST.getInsightsEndpoint();

        TestCasesParameter parameter = new TestCasesParameter.Builder(getOrgId(sauceREST))
            .setStart(LocalDateTime.now().minusDays(30))
            .setEnd(LocalDateTime.now())
            .build();
        TestCases testCases = insightsEndpoint.getTestCases(JobSource.VDC, parameter);

        assertNotNull(testCases.testCases);
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getTestCasesStatsTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        InsightsEndpoint insightsEndpoint = sauceREST.getInsightsEndpoint();

        TestCasesStatsParameter parameter = new TestCasesStatsParameter.Builder(getOrgId(sauceREST))
            .setStart(LocalDateTime.now().minusDays(30))
            .setEnd(LocalDateTime.now())
            .build();
        TestCasesStats testCasesStats = insightsEndpoint.getTestCasesStats(JobSource.VDC, parameter);

        assertNotNull(testCasesStats);
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getErrorsTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        InsightsEndpoint insightsEndpoint = sauceREST.getInsightsEndpoint();

        ErrorsParameter parameter = new ErrorsParameter.Builder(getOrgId(sauceREST))
            .setStart(LocalDateTime.now().minusDays(30))
            .setEnd(LocalDateTime.now())
            .build();
        Errors errors = insightsEndpoint.getErrors(JobSource.VDC, parameter);

        assertNotNull(errors.buckets);
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getTrendsTestsTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        InsightsEndpoint insightsEndpoint = sauceREST.getInsightsEndpoint();

        TrendsTestsParameter parameter = new TrendsTestsParameter.Builder(getOrgId(sauceREST))
            .setStart(LocalDateTime.now().minusDays(30))
            .setEnd(LocalDateTime.now())
            .build();
        TestTrends testTrends = insightsEndpoint.getTrendsTests(JobSource.VDC, parameter);

        assertNotNull(testTrends.buckets);
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getTrendsErrorsTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        InsightsEndpoint insightsEndpoint = sauceREST.getInsightsEndpoint();

        TrendsErrorsParameter parameter = new TrendsErrorsParameter.Builder(getOrgId(sauceREST))
            .setStart(LocalDateTime.now().minusDays(30))
            .setEnd(LocalDateTime.now())
            .build();
        TrendsErrors trendsErrors = insightsEndpoint.getTrendsErrors(JobSource.VDC, parameter);

        assertNotNull(trendsErrors.buckets);
    }
}