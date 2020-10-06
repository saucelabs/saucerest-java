package com.saucelabs.saucerest;

/**
 * A nice clean place to collect error messages intended to guide users when throwing exceptions.
 */
class ErrorExplainers {

    static String missingCreds() {
        return String.join(System.getProperty("line.separator"),
            "If using System Properties/Environment Variables (ENVars), this can happen because:",
            " * You are using a toolchain which does not automatically propagate ENVars between tools",
            " * You are using a CI platform which does not automatically propagate ENVars between separate controller and processing hosts",
            " * You are running tests on an environment on which these properties are not set; A newly build CI server, a Docker instance, etc"
        );
    }

    static String incorrectCreds(String username, String accessKey) {
        String endOfKey = accessKey.substring(accessKey.length() - 3);

        return String.join(System.getProperty("line.separator"),
            "Not Authorized.  Possible Reasons:",
            " * The provided Username (" + username + ") is incorrect",
            " * This account does not have permissions to access this job",
            " * The provided Access Key ending with '" + endOfKey + "' is incorrect"
        );
    }

    static String resourceMissing() {
        return String.join(System.getProperty("line.separator"),
            "Resource Not Found.   Possible reasons:",
            " * This job does not exist",
            " * Job assets have expired"
        );
    }

    static String videoMissing() {
        return String.join(System.getProperty("line.separator"),
            " * You disabled video recording by setting the `recordVideo` capability to false",
            " * This test was not able to complete video encoding due to an error or early termination"
        );
    }

    public static String HARMissing() {
        return String.join(System.getProperty("line.separator"),
            " * This test was run without Extended Debugging. See https://wiki.saucelabs.com/pages/viewpage.action?pageId=70072943",
            " * This test was not able to complete HAR file recording due to an error or early termination"
        );
    }
}
