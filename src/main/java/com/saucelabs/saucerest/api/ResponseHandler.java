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
        // TODO: add more specific error messages for each endpoint
        switch (response.code()) {
            case HTTP_NOT_FOUND:
                if (endpoint instanceof SauceConnectEndpoint) {
                    if (response.request().method().equals(HttpMethod.DELETE.label)) {
                        String tunnelID = getID(response);
                        throw new SauceException.NotFound(String.join(System.lineSeparator(), ErrorExplainers.TunnelNotFound(tunnelID)));
                    }
                } else if (endpoint instanceof StorageEndpoint) {
                    String appFileID = getID(response);
                    throw new SauceException.NotFound(String.join(System.lineSeparator(), ErrorExplainers.AppNotFound(appFileID)));
                } else if (endpoint instanceof AccountsEndpoint) {
                    String accountID = getID(response);
                    throw new SauceException.NotFound(String.join(System.lineSeparator(), ErrorExplainers.AccountNotFound(accountID)));
                }
                throw new SauceException.NotFound();
            case HTTP_UNAUTHORIZED:
                throw new SauceException.NotAuthorized(checkCredentials(endpoint));
            case HTTP_BAD_REQUEST:
                if (endpoint instanceof JobsEndpoint) {
                    if (response.message().equalsIgnoreCase("Job hasn't finished running")) {
                        throw new SauceException.NotYetDone(ErrorExplainers.JobNotYetDone());
                    }
                }
                throw new RuntimeException("Unexpected code " + response);
            default:
                throw new RuntimeException("Unexpected code " + response);
        }
    }

    /**
     * Returns the ID of the resource from the URL.
     */
    private static String getID(Response response) {
        String ID = getLastPathSegment(response, 1);

        // if ID is not a UUID
        if (!ID.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}") || !ID.matches("\\d+")) {
            return getLastPathSegment(response, 2);
        }

        return ID;
    }

    private static String getLastPathSegment(Response response, int offset) {
        if (offset == 0) {
            offset = 1;
        }

        return response.request().url().pathSegments().get(response.request().url().pathSegments().size() - offset);
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