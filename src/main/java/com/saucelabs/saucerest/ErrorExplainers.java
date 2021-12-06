package com.saucelabs.saucerest;

/**
 * A nice clean place to collect error messages intended to guide users when throwing exceptions.
 */
class ErrorExplainers {

    static String missingCreds() {
        return String.join(System.lineSeparator(),
            "If using System Properties/Environment Variables (ENVars), this can happen because:",
            " * You are using a toolchain which does not automatically propagate ENVars between tools",
            " * You are using a CI platform which does not automatically propagate ENVars between separate controller and processing hosts",
            " * You are running tests on an environment on which these properties are not set; A newly build CI server, a Docker instance, etc"
        );
    }

    static String incorrectCreds(String username, String accessKey) {
        String endOfKey = accessKey.substring(accessKey.length() - 3);

        return String.join(System.lineSeparator(),
            "Not Authorized.  Possible Reasons:",
            " * The provided Username (" + username + ") is incorrect",
            " * This account does not have permissions to access this job",
            " * The provided Access Key ending with '" + endOfKey + "' is incorrect"
        );
    }

    static String resourceMissing() {
        return String.join(System.lineSeparator(),
            "Resource Not Found.   Possible reasons:",
            " * This job does not exist",
            " * Job assets have expired"
        );
    }

    static String videoMissing() {
        return String.join(System.lineSeparator(),
            " * You disabled video recording by setting the `recordVideo` capability to false",
            " * This test was not able to complete video encoding due to an error or early termination"
        );
    }

    static String HARMissing() {
        return String.join(System.lineSeparator(),
            " * This test was run without Extended Debugging. See https://wiki.saucelabs.com/pages/viewpage.action?pageId=70072943",
            " * This test was not able to complete HAR file recording due to an error or early termination"
        );
    }

    static String JobNotYetDone() {
        return String.join(System.lineSeparator(),
            " * This job hasn't finished processing yet.",
            " * After driver.quit() is called it will take some seconds to process and make available all job assets"
        );
    }

    static String LogNotFound() {
        return String.join(System.lineSeparator(),
            " * Log file could not be found. Possible reasons:",
            " * The requested log does not exist for the used framework. For example asking for the Selenium log when using Appium",
            " * A error occurred where the job was created on Sauce Labs but no test were executed."
        );
    }

    static String TunnelNotFound() {
        return String.join(System.lineSeparator(),
            " * Tunnel id could not be found. Possible reasons:",
            " * The tunnel id requested does not exist in this data center. Ensure the data center endpoint is correct.",
            " * A tunnel with this id never existed."
        );
    }
}
