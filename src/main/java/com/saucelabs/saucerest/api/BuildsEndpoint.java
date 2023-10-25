package com.saucelabs.saucerest.api;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.HttpMethod;
import com.saucelabs.saucerest.JobSource;
import com.saucelabs.saucerest.model.builds.Build;
import com.saucelabs.saucerest.model.builds.LookupBuildsParameters;
import com.saucelabs.saucerest.model.builds.LookupJobsParameters;
import com.saucelabs.saucerest.model.jobs.Job;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class BuildsEndpoint extends AbstractEndpoint {

  public BuildsEndpoint(DataCenter dataCenter) {
    super(dataCenter);
  }

  public BuildsEndpoint(String apiServer) {
    super(apiServer);
  }

  public BuildsEndpoint(String username, String accessKey, DataCenter dataCenter) {
    super(username, accessKey, dataCenter);
  }

  public BuildsEndpoint(String username, String accessKey, String apiServer) {
    super(username, accessKey, apiServer);
  }

  /**
   * Queries the requesting account and returns a summary of each build matching the query,
   * including the ID value, which may be a required parameter of other API calls related to a
   * specific build.
   *
   * <p>You can narrow the results of your query using any of the optional filtering parameters.
   *
   * @param jobSource The type of device for which you are getting builds. Valid values are: {@link
   *     JobSource}
   * @return A list of {@link Build} objects
   * @throws IOException when the request fails
   */
  public List<Build> lookupBuilds(JobSource jobSource) throws IOException {
    String url = getBaseEndpoint(jobSource);

    return deserializeJSONObject(
        request(url, HttpMethod.GET), Collections.singletonList(Build.class));
  }

  /**
   * Queries the requesting account and returns a summary of each build matching the query,
   * including the ID value, which may be a required parameter of other API calls related to a
   * specific build.
   *
   * <p>You can narrow the results of your query using any of the optional filtering parameters.
   *
   * @param jobSource The type of device for which you are getting builds. Valid values are: {@link
   *     JobSource}
   * @param parameters A {@link LookupBuildsParameters} object containing the parameters to filter
   *     the results
   * @return A list of {@link Build} objects
   * @throws IOException when the request fails
   */
  public List<Build> lookupBuilds(JobSource jobSource, LookupBuildsParameters parameters)
      throws IOException {
    String url = getBaseEndpoint(jobSource);

    return deserializeJSONObject(
        requestWithQueryParameters(url, HttpMethod.GET, parameters.toMap()),
        Collections.singletonList(Build.class));
  }

  /**
   * Queries the requesting account and returns a summary of each job matching the query, including
   * the ID value, which may be a required parameter of other API calls related to a specific job.
   *
   * <p>You can narrow the results of your query using any of the optional filtering parameters.
   *
   * @param jobSource The type of device for which you are getting builds. Valid values are: {@link
   *     JobSource}
   * @param parameters A {@link LookupJobsParameters} object containing the parameters to filter the
   *     results
   * @return A list of {@link Job} objects
   * @throws IOException when the request fails
   */
  public List<Job> lookupJobsForBuild(
      JobSource jobSource, String buildID, LookupJobsParameters parameters) throws IOException {
    String url = getBaseEndpoint(jobSource) + buildID + "/jobs/";

    return deserializeJSONObject(
        requestWithQueryParameters(url, HttpMethod.GET, parameters.toMap()),
        Collections.singletonList(Job.class));
  }

  /**
   * Retrieve the details related to a specific build by passing its unique ID in the request.
   *
   * @param jobSource The type of test device associated with the job and build. Valid values are:
   *     {@link JobSource#VDC} and {@link JobSource#RDC}
   * @param buildID The unique identifier of the build to retrieve. You can look up build IDs in
   *     your organization using {@link #lookupBuilds(JobSource)} method.
   * @return A {@link Build} object
   * @throws IOException when the request fails
   */
  public Build getSpecificBuild(JobSource jobSource, String buildID) throws IOException {
    String url = getBaseEndpoint(jobSource) + buildID + "/";

    return deserializeJSONObject(request(url, HttpMethod.GET), Build.class);
  }

  /** The base endpoint of the Builds endpoint APIs. */
  protected String getBaseEndpoint(JobSource jobSource) {
    return super.getBaseEndpoint() + "v2/builds/" + jobSource.value + "/";
  }
}
