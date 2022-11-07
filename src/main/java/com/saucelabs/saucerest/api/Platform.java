package com.saucelabs.saucerest.api;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.model.platform.EndOfLifeAppiumVersions;
import com.saucelabs.saucerest.model.platform.SupportedPlatforms;
import com.saucelabs.saucerest.model.platform.TestStatus;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Platform extends AbstractEndpoint {

    public Platform(DataCenter dataCenter) {
        super(dataCenter);
    }

    public Platform(String apiServer) {
        super(apiServer);
    }

    /**
     * Returns the status of Sauce Labs. Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/platform/#get-sauce-labs-teststatus">here</a>
     *
     * @return {@link TestStatus}
     * @throws IOException API request failed
     */
    public TestStatus getTestStatus() throws IOException {
        String url = getBaseEndpoint() + "/status";

        return getResponseClass(getResponseObject(url), TestStatus.class);
    }

    /**
     * Returns supported platforms. Valid values are 'all', 'appium' or 'webdriver'.
     * Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/platform/#get-supported-platforms">here</a>
     *
     * @param automationApi Specified automation framework: all, appium or webdriver.
     * @return {@link SupportedPlatforms}
     */
    public SupportedPlatforms getSupportedPlatforms(String automationApi) throws IOException {
        String url = getBaseEndpoint() + "/platforms/" + automationApi;

        return new SupportedPlatforms(getResponseListClass(getResponseObject(url), com.saucelabs.saucerest.model.platform.Platform.class));
    }

    /**
     * Returns all supported Appium versions on Sauce Labs and the expected end of life date of the version.
     * Documentation is
     * <a href="https://docs.saucelabs.com/dev/api/platform/#get-end-of-life-dates-for-appium-versions">here</a>
     *
     * @return {@link EndOfLifeAppiumVersions}
     */
    public EndOfLifeAppiumVersions getEndOfLifeAppiumVersions() throws IOException {
        String url = getBaseEndpoint() + "/platforms/appium/eol";

        return new EndOfLifeAppiumVersions(getResponseObject(url));
    }

    /**
     * The base endpoint of the Platform endpoint APIs.
     */
    private String getBaseEndpoint() {
        return baseURL + "rest/v1/info";
    }

    /**
     * Need to use this as the response is a JSON array instead of a JSON object.
     */
    protected <T> List<T> getResponseListClass(String jsonResponse, Class<T> clazz) throws IOException {
        Moshi moshi = new Moshi.Builder().build();

        Type listPlatform = Types.newParameterizedType(List.class, clazz);
        JsonAdapter<List<T>> adapter = moshi.adapter(listPlatform);
        return adapter.fromJson(jsonResponse);
    }
}
