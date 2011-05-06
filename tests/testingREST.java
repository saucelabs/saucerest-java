import java.io.*;
import java.util.*;
import com.saucelabs.saucerest.SauceREST;

/* to get your jobID:
 * Selenium 1: browser.getEval("selenium.sessionId");
 * Selenium 2: ((RemoteWebDriver) driver).getSessionId().toString();
 */

public class TestingREST {
    public static void main(String[] args) throws IOException {
        SauceREST client = new SauceREST("<your-username>", "<your-access-key>");
        /* Using a map of udpates:
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
    }
}
