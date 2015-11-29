package com.saucelabs.saucerest;

import com.saucelabs.saucerest.objects.Job;
import junit.framework.TestCase;
import org.json.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class SauceRESTTest extends TestCase {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private SauceREST sauceREST;
    private MockHttpURLConnection urlConnection;

    public class MockOutputStream extends OutputStream {
        public StringBuffer output = new StringBuffer();

        @Override
        public void write(int b) throws IOException {
            output.append((char) b);
        }

        @Override
        public String toString() {
            return output.toString();
        }
    }

    private class MockHttpURLConnection extends HttpURLConnection {
        private URL realURL;
        private InputStream mockInputStream;
        private OutputStream mockOutputStream;

        /**
         * Constructor for the HttpURLConnection.
         */
        protected MockHttpURLConnection() throws MalformedURLException {
            super(new URL("http://fake.site/"));
            try {
                this.mockInputStream = new ByteArrayInputStream("".getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            this.mockOutputStream = new MockOutputStream();
        }

        @Override
        public void disconnect() {

        }

        @Override
        public boolean usingProxy() {
            return false;
        }

        @Override
        public void connect() throws IOException {

        }

        @Override
        public InputStream getInputStream() throws IOException {
            return mockInputStream;
        }

        public void setInputStream(InputStream mockInputStream) {
            this.mockInputStream = mockInputStream;
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return mockOutputStream;
        }

        public void setOutputStream(OutputStream mockOutputStream) {
            this.mockOutputStream = mockOutputStream;
        }

        public URL getRealURL() {
            return realURL;
        }

        public void setRealURL(URL realURL) {
            this.realURL = realURL;
        }

        public void setResponseCode(int responseCode) {
            this.responseCode = responseCode;
        }

        @Override
        public int getResponseCode() throws IOException {
            return this.responseCode;
        }
    }


    @Before
    public void setUp() throws Exception {
        urlConnection = new MockHttpURLConnection();
        this.sauceREST = new SauceREST("fakeuser", "fakekey") {
            @Override
            public HttpURLConnection openConnection(URL url) throws IOException {
                SauceRESTTest.this.urlConnection.setRealURL(url);
                return SauceRESTTest.this.urlConnection;
            }
        };
    }

    @Test
    public void testDoJSONPOST_Created() throws Exception {
        urlConnection.setInputStream(new ByteArrayInputStream(
            "{\"id\": \"29cee6f11f5e4ec6b8b62e98f79bba6f\"}".getBytes("UTF-8")
        ));
        urlConnection.setResponseCode(201);
        this.sauceREST.doRESTPost(new URL("http://example.org/blah"), new JSONObject());
    }

    @Test(expected=SauceException.NotAuthorized.class)
    public void testDoJSONPOST_NotAuthorized() throws Exception {
        urlConnection.setResponseCode(401);

        thrown.expect(SauceException.NotAuthorized.class);
        this.sauceREST.doRESTPost(new URL("http://example.org/blah"), new JSONObject());
    }

    @Test
    public void testGetSupportedPlatforms_appium() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(getClass().getResource("/appium.json").openStream());

        String results = sauceREST.getSupportedPlatforms("appium");
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/info/platforms/appium");
    }

    @Test
    public void testRecordCI() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream(
            "{\"id\": \"29cee6f11f5e4ec6b8b62e98f79bba6f\"}".getBytes("UTF-8")
        ));
        sauceREST.recordCI("jenkins", "1.1");
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/stats/ci");
        String output = this.urlConnection.getOutputStream().toString();
        assertEquals(JSONValue.parse(output), JSONValue.parse("{\"platform_version\":\"1.1\",\"platform\":\"jenkins\"}"));
    }


    @Test
    public void testGetUser() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(getClass().getResource("/user_test.json").openStream());
        String userInfo = sauceREST.getUser();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/users/" + this.sauceREST.getUsername() + "");
    }

    @Test
    public void testGetStoredFiles() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream(
            "[]".getBytes("UTF-8")
        ));
        String userInfo = sauceREST.getStoredFiles();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/storage/" + this.sauceREST.getUsername() + "");
    }

    @Test
    public void testUpdateJobInfo() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream(
            "[]".getBytes("UTF-8")
        ));
        HashMap<String, Object> updates = new HashMap<String, Object>();
        updates.put("public", "shared");
        sauceREST.updateJobInfo("12345", updates);
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/12345");

        String output = this.urlConnection.getOutputStream().toString();
        assertEquals(JSONValue.parse(output), JSONValue.parse("{\"public\":\"shared\"}"));
    }


    public void testGetTunnels() throws Exception {
        urlConnection.setResponseCode(200);
        String userInfo = sauceREST.getTunnels();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/tunnels");
    }

    public void testGetTunnelInformation() throws Exception {
        urlConnection.setResponseCode(200);
        String userInfo = sauceREST.getTunnelInformation("1234-1234-1231-123-123");
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/tunnels/1234-1234-1231-123-123");
    }

    public void testGetActivity() throws Exception {
        urlConnection.setResponseCode(200);
        String userInfo = sauceREST.getActivity();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/activity");
    }

    public void testGetBuildJobs() throws Exception {
        urlConnection.setResponseCode(200);
        List<Job> jobs;

        urlConnection.setInputStream(getClass().getResource("/build_jobs_full.json").openStream());
        jobs = sauceREST.getBuildJobs("test_sauce__22", true);
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/build/test_sauce__22/jobs");
        assertEquals(this.urlConnection.getRealURL().getQuery(), "full=1");
        assertNotNull(jobs);
        assertEquals(6, jobs.size());
        assertEquals("Sauce Sample Test", jobs.get(0).getName());
        assertEquals("5f119101b8b14db89b25250bf33341d7", jobs.get(0).getId());
        assertEquals(new Date(1000 * (long) 1445299850), jobs.get(0).getCreationTime());
        assertEquals("Sauce Sample Test", jobs.get(1).getName());
        assertEquals("6c0b4b2076c44c4791f14c747eab7545", jobs.get(1).getId());

        urlConnection.setInputStream(getClass().getResource("/build_jobs.json").openStream());
        jobs = sauceREST.getBuildJobs("test_sauce__22", false);
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/build/test_sauce__22/jobs");
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertEquals(6, jobs.size());
        assertNull(jobs.get(0).getName());
        assertEquals("5f119101b8b14db89b25250bf33341d7", jobs.get(0).getId());
        assertEquals(new Date(1000 * (long) 1445299850), jobs.get(0).getCreationTime());

        urlConnection.setInputStream(getClass().getResource("/build_jobs_empty.json").openStream());
        jobs = sauceREST.getBuildJobs("test_sauce__8000000", false);
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/build/test_sauce__8000000/jobs");
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertEquals(0, jobs.size());
    }

    //@Ignore("No idea what this done")
    public void testGetJobsList() throws Exception {
        urlConnection.setResponseCode(200);

        sauceREST.getJobsList(new String[] {});
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/jobs");
        assertNull(this.urlConnection.getRealURL().getQuery());

        sauceREST.getJobsList(new String[] {"hey=1"});
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/jobs");
        assertEquals(this.urlConnection.getRealURL().getQuery(), "hey=1");
    }


    public void testGetConcurrency() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(getClass().getResource("/users_halkeye_concurrency.json").openStream());

        String concurencyInfo = sauceREST.getConcurrency();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/users/" + this.sauceREST.getUsername() + "/concurrency");
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertEquals(concurencyInfo, "{\"timestamp\": 1447392030.111457, \"concurrency\": {\"halkeye\": {\"current\": {\"overall\": 0, \"mac\": 0, \"manual\": 0}, \"remaining\": {\"overall\": 100, \"mac\": 100, \"manual\": 5}}}}");
    }

    /*
    public void testJobPassed() throws Exception {

    }

    public void testJobFailed() throws Exception {

    }

    public void testDownloadVideo() throws Exception {

    }

    public void testDownloadLog() throws Exception {

    }

    public void testRetrieveResults() throws Exception {

    }

    public void testGetJobInfo() throws Exception {

    }

    public void testRetrieveResults1() throws Exception {

    }

    public void testAddAuthenticationProperty() throws Exception {

    }

    public void testStopJob() throws Exception {

    }

    public void testOpenConnection() throws Exception {

    }

    public void testUploadFile() throws Exception {

    }

    public void testUploadFile1() throws Exception {

    }

    public void testUploadFile2() throws Exception {

    }

    public void testGetPublicJobLink() throws Exception {

    }

    public void testEncodeAuthentication() throws Exception {

    }

    public void testDeleteTunnel() throws Exception {

    }

    public void testGetTunnels() throws Exception {

    }

    public void testGetTunnelInformation() throws Exception {

    }
    */
}