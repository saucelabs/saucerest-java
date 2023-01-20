package com.saucelabs.saucerest.api;

import com.google.common.collect.ImmutableMap;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.model.realdevices.*;

import java.io.IOException;

public class RealDevices extends AbstractEndpoint {
    public RealDevices(DataCenter dataCenter) {
        super(dataCenter);
    }

    public RealDevices(String apiServer) {
        super(apiServer);
    }

    public RealDevices(String username, String accessKey, DataCenter dataCenter) {
        super(username, accessKey, dataCenter);
    }

    public RealDevices(String username, String accessKey, String apiServer) {
        super(username, accessKey, apiServer);
    }

    /**
     * Returns all real device in Sauce Labs. Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/rdc/#get-devices">here</a>
     *
     * @return {@link Devices}
     * @throws IOException API request failed
     */
    public Devices getDevices() throws IOException {
        String url = getBaseEndpoint() + "/devices";

        return new Devices(getResponseListClass(getResponseObject(url), Device.class));
    }

    /**
     * Returns a specific device based on its ID. Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/rdc/#get-a-specific-device">here</a>
     *
     * @return {@link Device}
     * @throws IOException API request failed
     */
    public Device getSpecificDevice(String deviceID) throws IOException {
        String url = getBaseEndpoint() + "/devices/" + deviceID;

        return getResponseClass(getResponseObject(url), Device.class);
    }

    /**
     * Returns all available devices in Sauce Labs. Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/rdc/#get-available-devices">here</a>
     *
     * @return {@link AvailableDevices}
     * @throws IOException API request failed
     */
    public AvailableDevices getAvailableDevices() throws IOException {
        String url = getBaseEndpoint() + "/devices/available";

        return new AvailableDevices(getResponseListClass(getResponseObject(url), String.class));
    }

    /**
     * Returns all device jobs/tests run on real devices. Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/rdc/#get-real-device-jobs">here</a>
     *
     * @return {@link DeviceJobs}
     * @throws IOException API request failed
     */
    public DeviceJobs getDeviceJobs() throws IOException {
        String url = getBaseEndpoint() + "/jobs";

        return getResponseClass(getResponseObject(url), DeviceJobs.class);
    }

    /**
     * Returns all device jobs/tests run on real devices. Result can be limited by providing optional parameters.
     * Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/rdc/#get-real-device-jobs">here</a>
     *
     * @param params Optional parameters
     * @return {@link DeviceJobs}
     * @throws IOException API request failed
     */
    public DeviceJobs getDeviceJobs(ImmutableMap<String, Object> params) throws IOException {
        String url = getBaseEndpoint() + "/jobs";

        return getResponseClass(getResponseObject(url, params), DeviceJobs.class);
    }

    /**
     * Returns a specific job/test based on its ID.
     * Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/rdc/#get-a-specific-real-device-job">here</a>
     *
     * @param jobID The ID of the job/test
     * @return {@link DeviceJob}
     * @throws IOException API request failed
     */
    public DeviceJob getSpecificDeviceJob(String jobID) throws IOException {
        String url = getBaseEndpoint() + "/jobs/" + jobID;

        return getResponseClass(getResponseObject(url), DeviceJob.class);
    }

    /**
     * TODO: This endpoint is currently not documented and also does not return any response whatsoever.
     *  Update this method and add integration tests including a model and so on when above is fixed.
     * Deletes a real device job/test by ID.
     * Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/rdc/">here</a>
     *
     * @param jobID The ID of the job/test to delete
     */
    public void deleteSpecificRealDeviceJob(String jobID) {
        String url = getBaseEndpoint() + "/jobs/" + jobID;

        try {
            deleteResponse(url);
        } catch (Exception e) {
            // do nothing
        }
    }

    /**
     * The base endpoint of the Platform endpoint APIs.
     */
    @Override
    protected String getBaseEndpoint() {
      return super.getBaseEndpoint() + "v1/rdc";
    }
}