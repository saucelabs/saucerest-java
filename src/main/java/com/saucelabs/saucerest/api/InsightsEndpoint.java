package com.saucelabs.saucerest.api;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.HttpMethod;
import com.saucelabs.saucerest.JobSource;
import com.saucelabs.saucerest.Unfinished;
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
import com.saucelabs.saucerest.model.insights.TestsParameter;
import com.saucelabs.saucerest.model.insights.TestTrends;
import com.saucelabs.saucerest.model.insights.TrendsErrors;
import com.saucelabs.saucerest.model.insights.TrendsErrorsParameter;
import com.saucelabs.saucerest.model.insights.TrendsTestsParameter;
import java.io.IOException;

@Unfinished("This endpoint does not yet cover the Filters, Activity, Concurrency, and Coverage Insights APIs, "
        + "nor the cross-source (all sources) variants of the Test Cases, Errors, and Trends APIs")
public class InsightsEndpoint extends AbstractEndpoint {
    public InsightsEndpoint(DataCenter dataCenter) {
        super(dataCenter);
    }

    public InsightsEndpoint(String apiServer) {
        super(apiServer);
    }

    public InsightsEndpoint(String username, String accessKey, DataCenter dataCenter) {
        super(username, accessKey, dataCenter);
    }

    public InsightsEndpoint(String username, String accessKey, String apiServer) {
        super(username, accessKey, apiServer);
    }

    /**
     * Retrieves run data for all tests matching the specified criteria.
     *
     * @param parameter A {@link TestResultParameter} object containing the parameters to filter the results
     * @return A {@link TestResult} object
     * @throws IOException when the request fails
     */
    public TestResult getTestResults(TestResultParameter parameter) throws IOException {
        String url = getBaseEndpoint() + "v1/analytics/tests";

        return deserializeJSONObject(requestWithQueryParameters(url, HttpMethod.GET, parameter.toMap()), TestResult.class);
    }

    /**
     * Returns aggregated metric values, such as fastest/slowest run and status breakdown, for a specific test
     * across a given period.
     *
     * @param parameter A {@link TestMetricsParameter} object containing the parameters to filter the results
     * @return A {@link TestMetrics} object
     * @throws IOException when the request fails
     */
    public TestMetrics getTestMetricsSummary(TestMetricsParameter parameter) throws IOException {
        String url = getBaseEndpoint() + "v1/analytics/insights/test-metrics";

        return deserializeJSONObject(requestWithQueryParameters(url, HttpMethod.GET, parameter.toMap()), TestMetrics.class);
    }

    /**
     * Returns time-bucketed data representing the tests executed in the requested interval.
     *
     * @param parameter A {@link AnalyticsTrendsParameter} object containing the parameters to filter the results
     * @return A {@link TestTrends} object
     * @throws IOException when the request fails
     */
    public TestTrends getTestTrends(AnalyticsTrendsParameter parameter) throws IOException {
        String url = getBaseEndpoint() + "v1/analytics/trends/tests";

        return deserializeJSONObject(requestWithQueryParameters(url, HttpMethod.GET, parameter.toMap()), TestTrends.class);
    }

    /**
     * Returns an array containing details of individual test executions matching the specified criteria.
     *
     * @param jobSource The type of device for which you are getting tests. Valid values are: {@link JobSource}
     * @param parameter A {@link TestsParameter} object containing the parameters to filter the results
     * @return A {@link Tests} object
     * @throws IOException when the request fails
     */
    public Tests getTests(JobSource jobSource, TestsParameter parameter) throws IOException {
        String url = getBaseEndpoint(jobSource) + "tests";

        return deserializeJSONObject(requestWithQueryParameters(url, HttpMethod.GET, parameter.toMap()), Tests.class);
    }

    /**
     * Returns an array of test cases, grouped by name, with statistical details such as run counts and pass/fail
     * rates.
     *
     * @param jobSource The type of device for which you are getting test cases. Valid values are: {@link JobSource}
     * @param parameter A {@link TestCasesParameter} object containing the parameters to filter the results
     * @return A {@link TestCases} object
     * @throws IOException when the request fails
     */
    public TestCases getTestCases(JobSource jobSource, TestCasesParameter parameter) throws IOException {
        String url = getBaseEndpoint(jobSource) + "test-cases";

        return deserializeJSONObject(requestWithQueryParameters(url, HttpMethod.GET, parameter.toMap()), TestCases.class);
    }

    /**
     * Returns a high-level statistical summary of test case consistency and reliability, such as counts of
     * consistently passing, failing, or erroring test cases.
     *
     * @param jobSource The type of device for which you are getting test case stats. Valid values are: {@link
     *     JobSource}
     * @param parameter A {@link TestCasesStatsParameter} object containing the parameters to filter the results
     * @return A {@link TestCasesStats} object
     * @throws IOException when the request fails
     */
    public TestCasesStats getTestCasesStats(JobSource jobSource, TestCasesStatsParameter parameter) throws IOException {
        String url = getBaseEndpoint(jobSource) + "test-cases/stats";

        return deserializeJSONObject(requestWithQueryParameters(url, HttpMethod.GET, parameter.toMap()), TestCasesStats.class);
    }

    /**
     * Returns an array of errors, with occurrence counts, for all tests matching the specified criteria.
     *
     * @param jobSource The type of device for which you are getting errors. Valid values are: {@link JobSource}
     * @param parameter A {@link ErrorsParameter} object containing the parameters to filter the results
     * @return A {@link Errors} object
     * @throws IOException when the request fails
     */
    public Errors getErrors(JobSource jobSource, ErrorsParameter parameter) throws IOException {
        String url = getBaseEndpoint(jobSource) + "errors";

        return deserializeJSONObject(requestWithQueryParameters(url, HttpMethod.GET, parameter.toMap()), Errors.class);
    }

    /**
     * Returns an array of buckets with aggregations, such as the number of tests run per browser, device, OS, or
     * framework, for the requested interval.
     *
     * @param jobSource The type of device for which you are getting trends. Valid values are: {@link JobSource}
     * @param parameter A {@link TrendsTestsParameter} object containing the parameters to filter the results
     * @return A {@link TestTrends} object
     * @throws IOException when the request fails
     */
    public TestTrends getTrendsTests(JobSource jobSource, TrendsTestsParameter parameter) throws IOException {
        String url = getBaseEndpoint(jobSource) + "trends/tests";

        return deserializeJSONObject(requestWithQueryParameters(url, HttpMethod.GET, parameter.toMap()), TestTrends.class);
    }

    /**
     * Returns error statistics, including the tests affected by each error, for the requested interval.
     *
     * @param jobSource The type of device for which you are getting error trends. Valid values are: {@link
     *     JobSource}
     * @param parameter A {@link TrendsErrorsParameter} object containing the parameters to filter the results
     * @return A {@link TrendsErrors} object
     * @throws IOException when the request fails
     */
    public TrendsErrors getTrendsErrors(JobSource jobSource, TrendsErrorsParameter parameter) throws IOException {
        String url = getBaseEndpoint(jobSource) + "trends/errors";

        return deserializeJSONObject(requestWithQueryParameters(url, HttpMethod.GET, parameter.toMap()), TrendsErrors.class);
    }

    /** The base endpoint of the v2 Insights endpoint APIs. */
    protected String getBaseEndpoint(JobSource jobSource) {
        return super.getBaseEndpoint() + "v2/insights/" + jobSource.value + "/";
    }
}
