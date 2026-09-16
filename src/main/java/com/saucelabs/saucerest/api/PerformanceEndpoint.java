package com.saucelabs.saucerest.api;

import com.google.gson.reflect.TypeToken;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.HttpMethod;
import com.saucelabs.saucerest.model.performance.Baseline;
import com.saucelabs.saucerest.model.performance.BaselineParameter;
import com.saucelabs.saucerest.model.performance.BaselineResetStatus;
import com.saucelabs.saucerest.model.performance.DiscardedTests;
import com.saucelabs.saucerest.model.performance.GetPerformanceMetricsParameter;
import com.saucelabs.saucerest.model.performance.MetricAssertion;
import com.saucelabs.saucerest.model.performance.PerformanceMetrics;
import com.saucelabs.saucerest.model.performance.Regime;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Response;

public class PerformanceEndpoint extends AbstractEndpoint {
    public PerformanceEndpoint(DataCenter dataCenter) {
        super(dataCenter);
    }

    public PerformanceEndpoint(String apiServer) {
        super(apiServer);
    }

    public PerformanceEndpoint(String username, String accessKey, DataCenter dataCenter) {
        super(username, accessKey, dataCenter);
    }

    public PerformanceEndpoint(String username, String accessKey, String apiServer) {
        super(username, accessKey, apiServer);
    }

    /**
     * Retrieves performance test results and metric values for the requesting account.
     *
     * @param parameter A {@link GetPerformanceMetricsParameter} object containing the parameters to filter the
     *     results
     * @return A {@link PerformanceMetrics} object
     * @throws IOException when the request fails
     */
    public PerformanceMetrics getPerformanceResults(GetPerformanceMetricsParameter parameter) throws IOException {
        return deserializeJSONObject(
                requestWithQueryParameters(getBaseEndpoint(), HttpMethod.GET, parameter.toMap()), PerformanceMetrics.class);
    }

    /**
     * Retrieves the full results for a specific performance test run.
     *
     * @param jobID The unique identifier of the test
     * @return A {@link PerformanceMetrics} object
     * @throws IOException when the request fails
     */
    public PerformanceMetrics getPerformanceResults(String jobID) throws IOException {
        return getPerformanceResults(jobID, true);
    }

    /**
     * Retrieves the results for a specific performance test run.
     *
     * @param jobID The unique identifier of the test
     * @param full When {@code false}, only basic data is returned instead of the full metric set
     * @return A {@link PerformanceMetrics} object
     * @throws IOException when the request fails
     */
    public PerformanceMetrics getPerformanceResults(String jobID, boolean full) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("full", full);

        return deserializeJSONObject(
                requestWithQueryParameters(getBaseEndpoint(jobID), HttpMethod.GET, params), PerformanceMetrics.class);
    }

    /**
     * Returns information about outlier values in the test for the specified metrics.
     *
     * @param jobID The unique identifier of the test
     * @param metricNames The metrics to evaluate
     * @param orderIndex The record number to begin returning results from
     * @return A {@link Map} of metric name to {@link MetricAssertion}
     * @throws IOException when the request fails
     */
    public Map<String, MetricAssertion> getTestAssertions(String jobID, String[] metricNames, int orderIndex)
            throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("metric_names", metricNames);
        params.put("order_index", orderIndex);

        Type type = TypeToken.getParameterized(Map.class, String.class, MetricAssertion.class).getType();
        return deserializeJSON(requestWithQueryParameters(getBaseEndpoint(jobID) + "assert/", HttpMethod.GET, params), type);
    }

    /**
     * Returns the acceptable upper and lower boundary values for the specified metrics, as determined by the test's
     * baseline.
     *
     * @param jobID The unique identifier of the test
     * @param parameter A {@link BaselineParameter} object containing the parameters to filter the results
     * @return A {@link Map} of metric name to {@link Baseline}
     * @throws IOException when the request fails
     */
    public Map<String, Baseline> getTestBaseline(String jobID, BaselineParameter parameter) throws IOException {
        Type type = TypeToken.getParameterized(Map.class, String.class, Baseline.class).getType();
        return deserializeJSON(
                requestWithQueryParameters(getBaseEndpoint(jobID) + "baseline/", HttpMethod.GET, parameter.toMap()), type);
    }

    /**
     * Indicates whether a baseline reset has occurred for the specified test.
     *
     * @param jobID The unique identifier of the test
     * @return A {@link BaselineResetStatus} object
     * @throws IOException when the request fails
     */
    public BaselineResetStatus getBaselineResetHistory(String jobID) throws IOException {
        String url = getBaseEndpoint(jobID) + "baseline/reset/";

        return deserializeJSONObject(request(url, HttpMethod.GET), BaselineResetStatus.class);
    }

    /**
     * Resets the baseline calculation point for the specified test; tests run prior to the reset are excluded from
     * future baseline calculations.
     *
     * @param jobID The unique identifier of the test
     * @return {@link Response}
     * @throws IOException when the request fails
     */
    public Response resetBaseline(String jobID) throws IOException {
        String url = getBaseEndpoint(jobID) + "baseline/reset/";

        return request(url, HttpMethod.POST);
    }

    /**
     * Returns the list of tests that have been discarded from the baseline as outliers.
     *
     * @param jobID The unique identifier of the test
     * @param orderIndex The record number to begin returning results from
     * @return A {@link DiscardedTests} object
     * @throws IOException when the request fails
     */
    public DiscardedTests getDiscardedTests(String jobID, int orderIndex) throws IOException {
        return getDiscardedTests(jobID, orderIndex, null);
    }

    /**
     * Returns the list of tests that have been discarded from the baseline as outliers.
     *
     * @param jobID The unique identifier of the test
     * @param orderIndex The record number to begin returning results from
     * @param limit The maximum number of results to return
     * @return A {@link DiscardedTests} object
     * @throws IOException when the request fails
     */
    public DiscardedTests getDiscardedTests(String jobID, int orderIndex, Integer limit) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("order_index", orderIndex);
        if (limit != null) {
            params.put("limit", limit);
        }

        return deserializeJSONObject(
                requestWithQueryParameters(getBaseEndpoint(jobID) + "discarded/", HttpMethod.GET, params), DiscardedTests.class);
    }

    /**
     * Excludes outlier results from future baseline calculations, marking them as flaky.
     *
     * @param jobID The unique identifier of the test
     * @param orderIndex The record number to begin discarding results from
     * @return {@link Response}
     * @throws IOException when the request fails
     */
    public Response discardOutliers(String jobID, int orderIndex) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("order_index", orderIndex);

        return requestWithQueryParameters(getBaseEndpoint(jobID) + "discarded/", HttpMethod.POST, params);
    }

    /**
     * Returns the test history for the specified test.
     *
     * @param jobID The unique identifier of the test
     * @param orderIndex The record number to begin returning results from
     * @return A {@link PerformanceMetrics} object
     * @throws IOException when the request fails
     */
    public PerformanceMetrics getTestHistory(String jobID, int orderIndex) throws IOException {
        return getTestHistory(jobID, orderIndex, null);
    }

    /**
     * Returns the test history for the specified test.
     *
     * @param jobID The unique identifier of the test
     * @param orderIndex The record number to begin returning results from
     * @param limit The maximum number of results to return
     * @return A {@link PerformanceMetrics} object
     * @throws IOException when the request fails
     */
    public PerformanceMetrics getTestHistory(String jobID, int orderIndex, Integer limit) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("order_index", orderIndex);
        if (limit != null) {
            params.put("limit", limit);
        }

        return deserializeJSONObject(
                requestWithQueryParameters(getBaseEndpoint(jobID) + "history/", HttpMethod.GET, params), PerformanceMetrics.class);
    }

    /**
     * Returns the starting and ending job counts in the current regime for each of the specified metrics.
     *
     * @param jobID The unique identifier of the test
     * @param metricNames The metrics to return regime information for
     * @param orderIndex The record number to limit results to
     * @return A {@link Map} of metric name to a {@link List} of {@link Regime} objects
     * @throws IOException when the request fails
     */
    public Map<String, List<Regime>> getMetricRegimes(String jobID, String[] metricNames, int orderIndex)
            throws IOException {
        return getMetricRegimes(jobID, metricNames, orderIndex, null);
    }

    /**
     * Returns the starting and ending job counts in the current regime for each of the specified metrics.
     *
     * @param jobID The unique identifier of the test
     * @param metricNames The metrics to return regime information for
     * @param orderIndex The record number to limit results to
     * @param includeBaseline Whether to include baseline values
     * @return A {@link Map} of metric name to a {@link List} of {@link Regime} objects
     * @throws IOException when the request fails
     */
    public Map<String, List<Regime>> getMetricRegimes(
            String jobID, String[] metricNames, int orderIndex, Boolean includeBaseline) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("metric_names", metricNames);
        params.put("order_index", orderIndex);
        if (includeBaseline != null) {
            params.put("include_baseline", includeBaseline);
        }

        Type type = TypeToken.getParameterized(
                Map.class, String.class, TypeToken.getParameterized(List.class, Regime.class).getType()).getType();
        return deserializeJSON(
                requestWithQueryParameters(getBaseEndpoint(jobID) + "regimes/", HttpMethod.GET, params), type);
    }

    /**
     * Confirms that the new regime values for the specified test are acceptable.
     *
     * @param jobID The unique identifier of the test
     * @param orderIndex The record number to limit results to
     * @return {@link Response}
     * @throws IOException when the request fails
     */
    public Response acknowledgeRegimes(String jobID, int orderIndex) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("order_index", orderIndex);

        return requestWithQueryParameters(getBaseEndpoint(jobID) + "regimes/acknowledge/", HttpMethod.POST, params);
    }

    /** The base endpoint of the Performance endpoint APIs. */
    protected String getBaseEndpoint() {
        return super.getBaseEndpoint() + "v2/performance/metrics/";
    }

    /** The base endpoint of the Performance endpoint APIs for a specific test. */
    protected String getBaseEndpoint(String jobID) {
        return getBaseEndpoint() + jobID + "/";
    }
}
