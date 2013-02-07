import java.io.*;
import java.lang.String;
import java.lang.System;
import java.util.*;
import com.saucelabs.saucerest.SauceREST;

/* to get your jobID:
 * Selenium 1: String jobID = browser.getEval("selenium.sessionId");
 * Selenium 2: String jobID = ((RemoteWebDriver) driver).getSessionId().toString();
 */

public class ExampleUsage {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Usage: java -cp saucerest-java.jar ExampleUsage <userid> <accessKey> <jobid>");
        }

        SauceREST client = new SauceREST(args[0], args[1]);
        /* Using a map of udpates:
         * (http://saucelabs.com/docs/sauce-ondemand#alternative-annotation-methods)
         *
         * Map<String, Object> updates = new HashMap<String, Object>();
         * updates.put("name", "this job has a name");
         * updates.put("passed", true);
         * updates.put("build", "c234");
         * client.updateJobInfo("<your-job-id>", updates);
         */
        String jobInfo = client.getJobInfo(args[2]);
        System.out.println("Job info: " + jobInfo);
        client.jobPassed(args[2]);
        //client.jobFailed("<your-job-id>");
    }
}
