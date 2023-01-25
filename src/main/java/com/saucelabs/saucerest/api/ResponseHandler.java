package com.saucelabs.saucerest.api;

import com.saucelabs.saucerest.ErrorExplainers;
import com.saucelabs.saucerest.HttpMethod;
import com.saucelabs.saucerest.SauceException;
import okhttp3.Response;

import static java.net.HttpURLConnection.*;

/**
 * Handle non-200 HTTP responses differently if needed per endpoint.
 * For example, provide endpoint specific context and error message.
 */
public class ResponseHandler {

    public static void responseHandler(AbstractEndpoint endpoint, Response response) {
        // TODO: refactor this to use Java 17 pattern matching in the future
        switch (response.code()) {
            case HTTP_NOT_FOUND:
                if (endpoint instanceof SauceConnect) {
                    if (response.request().method().equals(HttpMethod.DELETE.label)) {
                        String tunnelID = response.request().url().pathSegments().get(response.request().url().pathSegments().size() - 1);
                        throw new SauceException.NotFound(String.join(System.lineSeparator(), ErrorExplainers.TunnelNotFound(tunnelID)));
                    }
                } else {
                    throw new SauceException.NotFound();
                }
            case HTTP_UNAUTHORIZED:
                throw new SauceException.NotAuthorized(checkCredentials(endpoint));
            case HTTP_BAD_REQUEST:
                if (endpoint instanceof Job) {
                    if (response.message().equalsIgnoreCase("Job hasn't finished running")) {
                        throw new SauceException.NotYetDone(ErrorExplainers.JobNotYetDone());
                    } else {
                        throw new RuntimeException("Unexpected code " + response);
                    }
                }
            default:
                throw new RuntimeException("Unexpected code " + response);
        }
    }

    private static String checkCredentials(AbstractEndpoint endpoint) {
        String username = endpoint.username;
        String accessKey = endpoint.accessKey;

        if ((username == null || username.isEmpty()) && (accessKey == null || accessKey.isEmpty())) {
            return String.join(System.lineSeparator(), "Your username and access key are empty or blank.", ErrorExplainers.missingCreds());
        }

        if (username == null || username.isEmpty()) {
            return String.join(System.lineSeparator(), "Your username is empty or blank.", ErrorExplainers.missingCreds());
        }

        if (accessKey == null || accessKey.isEmpty()) {
            return String.join(System.lineSeparator(), "Your access key is empty or blank.", ErrorExplainers.missingCreds());
        }

        return ErrorExplainers.incorrectCreds(username, accessKey);
    }
}
