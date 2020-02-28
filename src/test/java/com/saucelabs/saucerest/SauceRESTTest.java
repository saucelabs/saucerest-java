package com.saucelabs.saucerest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang.SerializationUtils;
import org.hamcrest.CoreMatchers;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SauceRESTTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private SauceREST sauceREST;
    private MockHttpURLConnection urlConnection;

    public class MockOutputStream extends OutputStream {
        public StringBuffer output = new StringBuffer();

        @Override
        public void write(int b) {
            output.append((char) b);
        }

        @Override
        public String toString() {
            return output.toString();
        }
    }

    private class ExceptionThrowingMockInputStream extends InputStream {

        @Override
        public int read() {
            return 1;
        }

        @Override
        public void close() throws IOException {
            throw new IOException();
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

        protected MockHttpURLConnection(ExceptionThrowingMockInputStream mockInputStream) throws MalformedURLException {
            this();
            this.mockInputStream = mockInputStream;
        }

        @Override
        public void disconnect() {

        }

        @Override
        public boolean usingProxy() {
            return false;
        }

        @Override
        public void connect() {

        }

        @Override
        public InputStream getInputStream() {
            return mockInputStream;
        }

        public void setInputStream(InputStream mockInputStream) {
            this.mockInputStream = mockInputStream;
        }

        @Override
        public OutputStream getOutputStream() {
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
        public int getResponseCode() {
            return this.responseCode;
        }
    }


    @Before
    public void setUp() throws Exception {
        urlConnection = new MockHttpURLConnection();
        this.sauceREST = new SauceREST("fakeuser", "fakekey") {
            @Override
            public HttpURLConnection openConnection(URL url) {
                SauceRESTTest.this.urlConnection.setRealURL(url);
                return SauceRESTTest.this.urlConnection;
            }
        };
    }

    private void setConnectionThrowIOExceptionOnClose() throws MalformedURLException {
        urlConnection = new MockHttpURLConnection(new ExceptionThrowingMockInputStream());
        this.sauceREST = new SauceREST("fakeuser", "fakekey") {
            @Override
            public HttpURLConnection openConnection(URL url) {
                SauceRESTTest.this.urlConnection.setRealURL(url);
                return SauceRESTTest.this.urlConnection;
            }
        };
    }

    @Test
    public void testUserAgent() {
        String agent = this.sauceREST.getUserAgent();
        assertNotNull(agent);
        assertThat(agent, not(CoreMatchers.containsString("/null")));
    }

    @Test
    public void testConfirmSerializable() {
        SauceREST original = new SauceREST(null, null);
        SauceREST copy = (SauceREST) SerializationUtils.clone(original);
        assertEquals(original, copy);
    }

    @Test
    public void testDoJSONPOST_Created() throws Exception {
        urlConnection.setInputStream(new ByteArrayInputStream(
            "{\"id\": \"29cee6f11f5e4ec6b8b62e98f79bba6f\"}".getBytes("UTF-8")
        ));
        urlConnection.setResponseCode(201);
        this.sauceREST.doJSONPOST(new URL("http://example.org/blah"), new JSONObject());
    }

    @Ignore("This test didn't run before - was implicitly ignored. Requires fixing.")
    @Test(expected = SauceException.NotAuthorized.class)
    public void testDoJSONPOST_NotAuthorized() throws Exception {
        urlConnection.setResponseCode(401);

        thrown.expect(SauceException.NotAuthorized.class);
        this.sauceREST.doJSONPOST(new URL("http://example.org/blah"), new JSONObject());
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
        assertEquals(new JSONObject(output).toString(),
            new JSONObject("{\"platform_version\":\"1.1\",\"platform\":\"jenkins\"}").toString());
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
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("public", "shared");
        sauceREST.updateJobInfo("12345", updates);
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/12345");

        String output = this.urlConnection.getOutputStream().toString();
        assertEquals(output, "{\"public\":\"shared\"}");
    }

    @Test
    public void testUpdateJobInfo_NotAuthorized() throws Exception {
        setConnectionThrowIOExceptionOnClose();
        urlConnection.setResponseCode(401);
        thrown.expect(SauceException.NotAuthorized.class);

        HashMap<String, Object> updates = new HashMap<>();
        updates.put("passed", true);
        sauceREST.updateJobInfo("12345", updates);
    }

    @Test
    public void testUpdateJobInfo_TooManyRequests() throws Exception {
        setConnectionThrowIOExceptionOnClose();
        urlConnection.setResponseCode(429);
        thrown.expect(SauceException.TooManyRequests.class);

        HashMap<String, Object> updates = new HashMap<>();
        updates.put("passed", true);
        sauceREST.updateJobInfo("12345", updates);
    }

    @Test
    public void testGetTunnels() {
        urlConnection.setResponseCode(200);
        String userInfo = sauceREST.getTunnels();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/tunnels");
    }

    @Test
    public void testGetTunnelInformation() {
        urlConnection.setResponseCode(200);
        String userInfo = sauceREST.getTunnelInformation("1234-1234-1231-123-123");
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/tunnels/1234-1234-1231-123-123");
    }

    @Test
    public void testGetActivity() {
        urlConnection.setResponseCode(200);
        String userInfo = sauceREST.getActivity();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/activity");
    }

    @Test
    public void testGetConcurrency() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(getClass().getResource("/users_halkeye_concurrency.json").openStream());

        String concurencyInfo = sauceREST.getConcurrency();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/users/" + this.sauceREST.getUsername() + "/concurrency");
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertEquals(concurencyInfo, "{\"timestamp\": 1447392030.111457, \"concurrency\": {\"halkeye\": {\"current\": {\"overall\": 0, \"mac\": 0, \"manual\": 0}, \"remaining\": {\"overall\": 100, \"mac\": 100, \"manual\": 5}}}}");
    }

    @Test
    public void testUploadFile() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ \"md5\": \"abc123445213242\" }".getBytes("UTF-8")));

        sauceREST.uploadFile(
            new ByteArrayInputStream("".getBytes("UTF-8")),
            "gavin.txt",
            true
        );
        assertEquals(
            "/rest/v1/storage/" + this.sauceREST.getUsername() + "/gavin.txt",
            this.urlConnection.getRealURL().getPath()
        );
        assertEquals(
            "overwrite=true",
            this.urlConnection.getRealURL().getQuery()
        );
    }

    @Test
    public void testStopJob() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes("UTF-8")));

        sauceREST.stopJob("123");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/123/stop",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
    }

    @Test
    public void testDeleteJob() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes("UTF-8")));

        sauceREST.deleteJob("123");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/123",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
    }

    @Test
    public void testGetJobInfo() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes("UTF-8")));

        sauceREST.getJobInfo("123");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/123",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
    }

    @Test
    public void testRetrieveResults() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes("UTF-8")));

        sauceREST.retrieveResults("fakePath");
        assertEquals(
            "/rest/v1/fakePath",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
    }

    @Test
    public void testAttemptDownload() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes("UTF-8")));

        sauceREST.attemptLogDownload("1234", folder.getRoot().getAbsolutePath());
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/selenium-server.log",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());

        sauceREST.attemptVideoDownload("1234", folder.getRoot().getAbsolutePath());
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/video.mp4",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
    }

    @Test
    public void testDownloadWithFileNotFoundThrowsException() throws Exception {
        urlConnection.setResponseCode(404);
        thrown.expect(java.io.FileNotFoundException.class);
        sauceREST.downloadLogOrThrow("1234", folder.getRoot().getAbsolutePath());
    }

    @Test
    public void testDownloadLogWithWrongCredentialsThrowsException() throws Exception {
        urlConnection.setResponseCode(401);
        thrown.expect(SauceException.NotAuthorized.class);
        sauceREST.downloadLogOrThrow("1234", folder.getRoot().getAbsolutePath());
    }

    @Test
    public void testDownloadVideoWithFileNotFoundThrowsException() throws Exception {
        urlConnection.setResponseCode(404);
        thrown.expect(java.io.FileNotFoundException.class);
        sauceREST.downloadVideoOrThrow("1234", folder.getRoot().getAbsolutePath());
    }

    @Test
    public void testDownloadVideoWithWrongCredentialsThrowsException() throws Exception {
        urlConnection.setResponseCode(401);
        thrown.expect(SauceException.NotAuthorized.class);
        sauceREST.downloadVideoOrThrow("1234", folder.getRoot().getAbsolutePath());
    }

    @Test
    public void testAttemptHARDownload() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes("UTF-8")));

        sauceREST.attemptHARDownload("1234", folder.getRoot().getAbsolutePath());
        assertEquals(
            "/v1/eds/1234/network.har",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
    }

    @Test
    public void testDownloadHARWithFileNotFoundThrowsException() throws Exception {
        urlConnection.setResponseCode(404);
        thrown.expect(java.io.FileNotFoundException.class);
        sauceREST.downloadHAROrThrow("1234", folder.getRoot().getAbsolutePath());
    }

    @Test
    public void testDownloadHARWithWrongCredentialsThrowsException() throws Exception {
        urlConnection.setResponseCode(401);
        thrown.expect(SauceException.NotAuthorized.class);
        sauceREST.downloadHAROrThrow("1234", folder.getRoot().getAbsolutePath());
    }

    @Test
    public void testJobFailed() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes("UTF-8")));

        sauceREST.jobFailed("1234");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
        String output = this.urlConnection.getOutputStream().toString();
        assertEquals(output, "{\"passed\":false}");
    }

    @Test
    public void testJobPassed() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes("UTF-8")));

        sauceREST.jobPassed("1234");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
        String output = this.urlConnection.getOutputStream().toString();
        assertEquals(output, "{\"passed\":true}");
    }

    @Test
    public void testAddTagsEmpty() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes("UTF-8")));

        sauceREST.addTags("1234", new ArrayList<String>());
        assertEquals(
                "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234",
                this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
        String output = this.urlConnection.getOutputStream().toString();
        assertEquals(output, "{\"tags\":[]}");
    }

    @Test
    public void testAddTags() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes("UTF-8")));

        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        sauceREST.addTags("1234", tags);
        assertEquals(
                "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234",
                this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
        String output = this.urlConnection.getOutputStream().toString();
        assertEquals(output, "{\"tags\":[\"tag1\",\"tag2\"]}");
    }

    @Test
    public void testGetFullJobs() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes("UTF-8")));

        sauceREST.getFullJobs();
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs",
            this.urlConnection.getRealURL().getPath()
        );
        assertEquals("full=true&limit=20", this.urlConnection.getRealURL().getQuery());

        sauceREST.getFullJobs(50);
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs",
            this.urlConnection.getRealURL().getPath()
        );
        assertEquals("full=true&limit=50", this.urlConnection.getRealURL().getQuery());
    }

    @Test
    public void testGetJobs() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes("UTF-8")));

        sauceREST.getJobs();
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs",
            this.urlConnection.getRealURL().getPath()
        );
        assertEquals(null, this.urlConnection.getRealURL().getQuery());

    }

    @Test
    public void testGetJobsLimit() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes("UTF-8")));

        sauceREST.getJobs(100);
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs",
            this.urlConnection.getRealURL().getPath()
        );
        assertEquals("limit=100", this.urlConnection.getRealURL().getQuery());

        sauceREST.getJobs(500);
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs",
            this.urlConnection.getRealURL().getPath()
        );
        assertEquals("limit=500", this.urlConnection.getRealURL().getQuery());
    }

    @Test
    public void testGetJobsSkipLimit() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes("UTF-8")));

        sauceREST.getJobs(100, 1470689339, 1470862161);
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs",
            this.urlConnection.getRealURL().getPath()
        );
        assertEquals("limit=100&from=1470689339&to=1470862161", this.urlConnection.getRealURL().getQuery());

        sauceREST.getJobs(500, 1470689339, 1470862161);
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs",
            this.urlConnection.getRealURL().getPath()
        );
        assertEquals("limit=500&from=1470689339&to=1470862161", this.urlConnection.getRealURL().getQuery());

    }

    @Test
    public void testBuildFullJobs() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes("UTF-8")));

        sauceREST.getBuildFullJobs("fakePath");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/build/fakePath/jobs",
            this.urlConnection.getRealURL().getPath()
        );
        assertEquals("full=1", this.urlConnection.getRealURL().getQuery());
    }

    @Test
    public void testGetBuild() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes("UTF-8")));

        sauceREST.getBuild("fakePath");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/builds/fakePath",
            this.urlConnection.getRealURL().getPath()
        );
        assertEquals(null, this.urlConnection.getRealURL().getQuery());
    }

    @Test
    public void should_get_public_job_from_eu() {
        // GIVEN
        this.sauceREST = new SauceREST("fakeuser", "fakekey", DataCenter.EU) {
            @Override
            public HttpURLConnection openConnection(URL url) {
                SauceRESTTest.this.urlConnection.setRealURL(url);
                return SauceRESTTest.this.urlConnection;
            }
        };
        // WHEN
        String publicJobLink = sauceREST.getPublicJobLink("fakeJobId");
        // THEN
        assertThat(publicJobLink, containsString("eu-central-1"));
    }

    @Test
    public void should_get_public_job_from_us() {
        // GIVEN
        this.sauceREST = new SauceREST("fakeuser", "fakekey", DataCenter.US) {
            @Override
            public HttpURLConnection openConnection(URL url) {
                SauceRESTTest.this.urlConnection.setRealURL(url);
                return SauceRESTTest.this.urlConnection;
            }
        };
        // WHEN
        String publicJobLink = sauceREST.getPublicJobLink("fakeJobId");
        // THEN
        assertThat(publicJobLink, not(containsString("eu-central-1")));
    }

    @Test
    public void should_get_public_job_from_us_by_default() {
        // GIVEN
        this.sauceREST = new SauceREST("fakeuser", "fakekey") {
            @Override
            public HttpURLConnection openConnection(URL url) {
                SauceRESTTest.this.urlConnection.setRealURL(url);
                return SauceRESTTest.this.urlConnection;
            }
        };
        // WHEN
        String publicJobLink = sauceREST.getPublicJobLink("fakeJobId");
        // THEN
        assertThat(publicJobLink, not(containsString("eu-central-1")));
    }

    @Test
    public void should_get_public_job_from_eu_with_string() {
        // GIVEN
        this.sauceREST = new SauceREST("fakeuser", "fakekey", "EU") {
            @Override
            public HttpURLConnection openConnection(URL url) {
                SauceRESTTest.this.urlConnection.setRealURL(url);
                return SauceRESTTest.this.urlConnection;
            }
        };
        // WHEN
        String publicJobLink = sauceREST.getPublicJobLink("fakeJobId");
        // THEN
        assertThat(publicJobLink, containsString("eu-central-1"));
    }

    @Test
    public void should_get_public_job_from_us_east_with_string() {
        // GIVEN
        this.sauceREST = new SauceREST("fakeuser", "fakekey", "US_EAST") {
            @Override
            public HttpURLConnection openConnection(URL url) {
                SauceRESTTest.this.urlConnection.setRealURL(url);
                return SauceRESTTest.this.urlConnection;
            }
        };
        // WHEN
        String publicJobLink = sauceREST.getPublicJobLink("fakeJobId");
        // THEN
        assertThat(publicJobLink, containsString("us-east-1"));
    }

    @Test
    public void should_get_public_job_from_us_with_invalid_string() {
        // GIVEN
        this.sauceREST = new SauceREST("fakeuser", "fakekey", "Antarctica") {
            @Override
            public HttpURLConnection openConnection(URL url) {
                SauceRESTTest.this.urlConnection.setRealURL(url);
                return SauceRESTTest.this.urlConnection;
            }
        };
        // WHEN
        String publicJobLink = sauceREST.getPublicJobLink("fakeJobId");
        // THEN
        assertThat(publicJobLink, not(containsString("eu-central-1")));
    }

    /*
    public void testAddAuthenticationProperty() throws Exception {

    }

    public void testOpenConnection() throws Exception {

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
