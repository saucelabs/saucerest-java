package com.saucelabs.saucerest.api;

import com.google.gson.reflect.TypeToken;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.HttpMethod;
import com.saucelabs.saucerest.model.platform.AppiumVersion;
import com.saucelabs.saucerest.model.platform.EndOfLifeAppiumVersions;
import com.saucelabs.saucerest.model.platform.Platform;
import com.saucelabs.saucerest.model.platform.SupportedPlatforms;
import com.saucelabs.saucerest.model.platform.TestStatus;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;

public class PlatformEndpoint extends AbstractEndpoint {

    public PlatformEndpoint(DataCenter dataCenter) {
        super(dataCenter, false);
    }

    public PlatformEndpoint(String apiServer) {
        super(apiServer, false);
    }

    public PlatformEndpoint(String username, String accessKey, DataCenter dataCenter) {
        super(username, accessKey, dataCenter);
    }

    public PlatformEndpoint(String username, String accessKey, String apiServer) {
        super(username, accessKey, apiServer);
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

        return deserializeJSONObject(request(url, HttpMethod.GET), TestStatus.class);
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

        return new SupportedPlatforms(deserializeJSONArray(request(url, HttpMethod.GET), Platform.class));
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

        Type type = TypeToken.getParameterized(Map.class, String.class, Integer.class).getType();
        Map<String, Integer> mapOfVersions = deserializeJSON(request(url, HttpMethod.GET), type);
        return new EndOfLifeAppiumVersions(mapOfVersions.entrySet().stream()
            .map(entry -> new AppiumVersion(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList())
        );
    }

    /**
     * The base endpoint of the Platform endpoint APIs.
     */
    @Override
    protected String getBaseEndpoint() {
        return super.getBaseEndpoint() + "rest/v1/info";
    }
}
