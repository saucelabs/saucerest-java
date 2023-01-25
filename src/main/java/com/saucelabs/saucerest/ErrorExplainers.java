package com.saucelabs.saucerest;

/**
 * A nice clean place to collect error messages intended to guide users when throwing exceptions.
 */
public class ErrorExplainers {

    public static String errorMessageBuilder(String errorReason, String errorExplanation) {
        return (String.join(System.lineSeparator(), errorReason, errorExplanation));
    }

    public static String missingCreds() {
        return String.join(System.lineSeparator(),
            "If using System Properties/Environment Variables (ENVars), this can happen because:",
            " * You are using a toolchain which does not automatically propagate ENVars between tools",
            " * You are using a CI platform which does not automatically propagate ENVars between separate controller and processing hosts",
            " * You are running tests on an environment on which these properties are not set; A newly build CI server, a Docker instance, etc"
        );
    }

    public static String incorrectCreds(String username, String accessKey) {
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

    public static String JobNotYetDone() {
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

    public static String TunnelNotFound() {
        return String.join(System.lineSeparator(),
            " * Tunnel ID could not be found. Possible reasons:",
            " * The tunnel ID requested does not exist in this data center. Ensure the data center endpoint is correct.",
            " * A tunnel with this id never existed."
        );
    }

    public static String TunnelNotFound(String tunnelID) {
        return String.join(System.lineSeparator(),
            " * Tunnel ID " + tunnelID + " could not be found. Possible reasons:",
            " * The tunnel ID requested does not exist in this data center. Ensure the data center endpoint is correct.",
            " * A tunnel with this id never existed."
        );
    }

    public static String ResigningNotAllowed() {
        return String.join(System.lineSeparator(),
            " * Trying to set Resigning for this app failed. Possible reason:",
            " * You set the app platform to be Android. Resigning is only available and applied for iOS apps.",
            " * Either use Instrumentation which is for Android apps or change the platform to iOS."
        );
    }

    public static String InstrumentationNotAllowed() {
        return String.join(System.lineSeparator(),
            " * Trying to set Instrumentation for this app failed. Possible reason:",
            " * You set the app platform to be iOS. Instrumentation is only available and applied for Android apps.",
            " * Either use Resigning which is for iOS apps or change the platform to Android."
        );
    }

  public static String DeviceLockOnlyOnAndroid() {
    return String.join(System.lineSeparator(),
      " * Trying to setup a PIN code is only available for Android devices."
    );
  }

  public static String NoResult() {
    return String.join(System.lineSeparator(),
      " * API request was successful but nothing found."
    );
  }
}
