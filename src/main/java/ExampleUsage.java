import com.saucelabs.saucerest.SauceREST;

import java.io.IOException;

/* to get your jobID:
 * Selenium 1: String jobID = browser.getEval("selenium.sessionId");
 * Selenium 2: String jobID = ((RemoteWebDriver) driver).getSessionId().toString();
 */

public class ExampleUsage {
    public static void main(String[] args) throws IOException {
        SauceREST client = new SauceREST("<your-username>", "<your-access-key>");
        /* Using a map of updates:
         * (http://saucelabs.com/docs/sauce-ondemand#alternative-annotation-methods)
         *
         * Map<String, Object> updates = new HashMap<String, Object>();
         * updates.put("name", "this job has a name");
         * updates.put("passed", true);
         * updates.put("build", "c234");
         * client.updateJobInfo("<your-job-id>", updates);
         */

        client.jobPassed("<your-job-id>");
        //client.jobFailed("<your-job-id>");
        //client.downloadVideo("<your-job-id>", "download-location");
    }
}
