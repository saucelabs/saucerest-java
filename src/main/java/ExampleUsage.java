import com.saucelabs.saucerest.SauceREST;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/* to get your jobID:
 * Selenium 1: String jobID = browser.getEval("selenium.sessionId");
 * Selenium 2: String jobID = ((RemoteWebDriver) driver).getSessionId().toString();
 */

public class ExampleUsage {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Usage: java -cp saucerest-java.jar ExampleUsage <userid> <accessKey> <jobid>");
            System.exit(1);
        }

        SauceREST client = new SauceREST(args[0], args[1]);

        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("name", "this job has a name");
        updates.put("passed", true);
        JSONArray tags = new JSONArray();
        tags.add("testingblah");
        updates.put("tags", tags);
        client.updateJobInfo(args[2], updates);
        System.out.println(client.getJobInfo(args[2]));
    }
}
