package com.saucelabs.saucerest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang.SerializationUtils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SauceRESTTest {

    private SauceREST sauceREST;
    private MockHttpURLConnection urlConnection;

    public static class MockOutputStream extends OutputStream {
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

    private static class ExceptionThrowingMockInputStream extends InputStream {

        @Override
        public int read() {
            return 1;
        }

        @Override
        public void close() throws IOException {
            throw new IOException("Expected IO Exception");
        }
    }

    private static class ExceptionThrowingMockOutputStream extends OutputStream {

        @Override
        public void write(int b) throws IOException {
            throw new IOException("Expected IO Exception");
        }
    }

    private static class MockHttpURLConnection extends HttpURLConnection {
        private URL realURL;
        private InputStream mockInputStream;
        private OutputStream mockOutputStream;

        /**
         * Constructor for the HttpURLConnection.
         */
        protected MockHttpURLConnection() throws MalformedURLException {
            super(new URL("http://fake.site/"));
            this.mockInputStream = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));
            this.mockOutputStream = new MockOutputStream();
        }

        protected MockHttpURLConnection(ExceptionThrowingMockInputStream mockInputStream) throws MalformedURLException {
            this();
            this.mockInputStream = mockInputStream;
        }

        protected MockHttpURLConnection(ExceptionThrowingMockOutputStream mockOutputStream) throws  MalformedURLException {
            this();
            this.mockOutputStream = mockOutputStream;
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

    @BeforeAll
    static void setUpLogger() {
        System.setProperty("java.util.logging.config.file", ClassLoader.getSystemResource("logging.properties").getPath());
    }

    @BeforeEach
    void setUp() throws Exception {
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

    private void setConnectionThrowIOExceptionOnWrite() throws MalformedURLException {
        urlConnection = new MockHttpURLConnection(new ExceptionThrowingMockOutputStream());
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
            "{\"id\": \"29cee6f11f5e4ec6b8b62e98f79bba6f\"}".getBytes(StandardCharsets.UTF_8)
        ));
        urlConnection.setResponseCode(201);
        this.sauceREST.doJSONPOST(new URL("http://example.org/blah"), new JSONObject());
    }

    @Test
    public void testDoJSONPOST_NotAuthorized() throws Exception {
        setConnectionThrowIOExceptionOnWrite();
        urlConnection.setResponseCode(401);
        URL url = new URL("http://example.org/blah");
        JSONObject body = new JSONObject();
        assertThrows(SauceException.NotAuthorized.class, () -> sauceREST.doJSONPOST(url, body));
    }

    @Test
    public void testGetSupportedPlatforms_appium() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(getClass().getResource("/appium.json").openStream());

        String results = sauceREST.getSupportedPlatforms("appium");
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/info/platforms/appium");
    }

    @Test
    public void testRecordCI() {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream(
            "{\"id\": \"29cee6f11f5e4ec6b8b62e98f79bba6f\"}".getBytes(StandardCharsets.UTF_8)
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
    public void testGetStoredFiles() {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream(
            "[]".getBytes(StandardCharsets.UTF_8)
        ));
        String userInfo = sauceREST.getStoredFiles();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/storage/" + this.sauceREST.getUsername() + "");
    }

    @Test
    public void testUpdateJobInfo() {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream(
            "[]".getBytes(StandardCharsets.UTF_8)
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

        HashMap<String, Object> updates = new HashMap<>();
        updates.put("passed", true);
        assertThrows(SauceException.NotAuthorized.class, () -> sauceREST.updateJobInfo("12345", updates));
    }

    @Test
    public void testUpdateJobInfo_TooManyRequests() throws Exception {
        setConnectionThrowIOExceptionOnClose();
        urlConnection.setResponseCode(429);

        HashMap<String, Object> updates = new HashMap<>();
        updates.put("passed", true);
        assertThrows(SauceException.TooManyRequests.class, () -> sauceREST.updateJobInfo("12345", updates));
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
        urlConnection.setInputStream(new ByteArrayInputStream("{ \"md5\": \"abc123445213242\" }".getBytes(
            StandardCharsets.UTF_8)));

        sauceREST.uploadFile(
            new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)),
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
    public void testStopJob() {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

        sauceREST.stopJob("123");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/123/stop",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
    }

    @Test
    public void testDeleteJob() {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

        sauceREST.deleteJob("123");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/123",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
    }

    @Test
    public void testGetJobInfo() {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

        sauceREST.getJobInfo("123");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/123",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
    }

    @Test
    public void testRetrieveResults() {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

        sauceREST.retrieveResults("fakePath");
        assertEquals(
            "/rest/v1/fakePath",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
    }

    @Test
    public void testDownloadLog(@TempDir Path tempDir) {
        testDownload(tempDir,
            sauceREST::downloadLog,
            jobId -> "/rest/v1/" + sauceREST.getUsername() + "/jobs/" + jobId + "/assets/selenium-server.log",
            jobId -> matchesPattern(jobId + "[\\d_]+\\.log")
        );
    }

    @Test
    public void testDownloadVideo(@TempDir Path tempDir) {
        testDownload(tempDir,
            sauceREST::downloadVideo,
            jobId -> "/rest/v1/" + sauceREST.getUsername() + "/jobs/" + jobId + "/assets/video.mp4",
            jobId -> matchesPattern(jobId + "[\\d_]+\\.mp4")
        );
    }

    @Test
    public void testDownloadJsonLog(@TempDir Path tempDir) {
        testDownload(tempDir,
            sauceREST::downloadJsonLog,
            jobId -> "/rest/v1/" + sauceREST.getUsername() + "/jobs/" + jobId + "/assets/log.json",
            jobId -> matchesPattern(jobId + "[\\d_]+\\.json")
        );
    }

    @Test
    public void testDownloadHAR(@TempDir Path tempDir) {
        testDownload(tempDir,
            sauceREST::downloadHAR,
            jobId -> "/v1/eds/" + jobId + "/network.har",
            jobId -> matchesPattern(jobId + "[\\d_]+\\.har")
        );
    }

    @Test
    public void testDownloadWithCustomFileName(@TempDir Path tempDir) {
        testDownload(tempDir,
            (jobId, absolutePath) -> sauceREST.downloadLog(jobId, absolutePath, "foobar.log"),
            jobId -> "/rest/v1/" + sauceREST.getUsername() + "/jobs/" + jobId + "/assets/selenium-server.log",
            jobId -> equalTo("foobar.log")
        );
    }

    @Test
    public void testDownloadWithCustomFileNameEmptyDefaultFallback(@TempDir Path tempDir) {
        testDownload(tempDir,
            (jobId, absolutePath) -> sauceREST.downloadLog(jobId, absolutePath, ""),
            jobId -> "/rest/v1/" + sauceREST.getUsername() + "/jobs/" + jobId + "/assets/selenium-server.log",
            jobId -> matchesPattern(jobId + "[\\d_]+\\.log")
        );
    }

    @Test
    public void testDownloadWithCustomFileNameSlashed(@TempDir Path tempDir) {
        testDownload(tempDir,
            (jobId, absolutePath) -> sauceREST.downloadLog(jobId, absolutePath, "foo/bar.log"),
            jobId -> "/rest/v1/" + sauceREST.getUsername() + "/jobs/" + jobId + "/assets/selenium-server.log",
            jobId -> equalTo("foo_bar.log")
        );
    }

    private void testDownload(Path tempDir, BiPredicate<String, String> methodUnderTest,
        Function<String, String> expectedEndpointFactory, Function<String, Matcher<String>> fileNameMatcherFactory) {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

        String absolutePath = tempDir.toAbsolutePath().toString();
        String jobId = "1234";

        boolean downloaded = methodUnderTest.test(jobId, absolutePath);
        assertTrue(downloaded);
        assertNull(urlConnection.getRealURL().getQuery());
        assertEquals(expectedEndpointFactory.apply(jobId), urlConnection.getRealURL().getPath());
        File[] files = tempDir.toFile().listFiles();
        assertNotNull(files);
        assertEquals(1, files.length);
        assertThat(files[0].getName(), fileNameMatcherFactory.apply(jobId));
    }

    static Stream<Arguments> downloadWithExceptionArguments(){
        return Stream.of(
            arguments(404, FileNotFoundException.class),
            arguments(401, SauceException.NotAuthorized.class)
        );
    }

    @ParameterizedTest
    @MethodSource("downloadWithExceptionArguments")
    public void testDownloadLogThrowsException(int responseCode, Class<? extends Throwable> expectedExceptionClass) {
        urlConnection.setResponseCode(responseCode);
        assertThrows(expectedExceptionClass, () -> sauceREST.downloadLogOrThrow("1234", null));
    }

    @ParameterizedTest
    @MethodSource("downloadWithExceptionArguments")
    public void testDownloadVideoThrowsException(int responseCode, Class<? extends Throwable> expectedExceptionClass) {
        urlConnection.setResponseCode(responseCode);
        assertThrows(expectedExceptionClass, () -> sauceREST.downloadVideoOrThrow("1234", null));
    }

    @ParameterizedTest
    @MethodSource("downloadWithExceptionArguments")
    public void testDownloadHarThrowsException(int responseCode, Class<? extends Throwable> expectedExceptionClass) {
        urlConnection.setResponseCode(responseCode);
        assertThrows(expectedExceptionClass, () -> sauceREST.downloadHAROrThrow("1234", null));
    }

    @Test
    public void testDownloadJsonLogStream() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8.name())));

        BufferedInputStream stream = sauceREST.downloadJsonLog("1234");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/log.json",
            this.urlConnection.getRealURL().getPath()
        );

        byte[] targetArray = new byte[stream.available()];
        stream.read(targetArray);
        assertTrue(new String(targetArray).length() > 0);
        assertNull(this.urlConnection.getRealURL().getQuery());
    }

    @Test
    public void testJobFailed() {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

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
    public void testJobPassed() {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

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
    public void testAddTagsEmpty() {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

        sauceREST.addTags("1234", new ArrayList<>());
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
        String output = this.urlConnection.getOutputStream().toString();
        assertEquals(output, "{\"tags\":[]}");
    }

    @Test
    public void testAddTags() {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

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
    public void testGetFullJobs() {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

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
    public void testGetJobs() {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

        sauceREST.getJobs();
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());

    }

    @Test
    void testGetJobsLimit() {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

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
    void testGetJobsSkipLimit() {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

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
    void testBuildFullJobs() {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

        sauceREST.getBuildFullJobs("fakePath");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/build/fakePath/jobs",
            this.urlConnection.getRealURL().getPath()
        );
        assertEquals("full=1", this.urlConnection.getRealURL().getQuery());
    }

    @Test
    void testGetBuild() {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

        sauceREST.getBuild("fakePath");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/builds/fakePath",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
    }

    @Test
    void should_get_public_job_from_eu() {
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
    void should_get_public_job_from_us() {
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
    void should_get_public_job_from_us_by_default() {
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
    void should_get_public_job_from_eu_with_string() {
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
    void should_get_public_job_from_us_east_with_string() {
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
    void should_get_public_job_from_us_with_invalid_string() {
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
    public void testAddAuthenticationProperty() {

    }

    public void testOpenConnection() {

    }

    public void testGetPublicJobLink() {

    }

    public void testEncodeAuthentication() {

    }

    public void testDeleteTunnel() {

    }

    public void testGetTunnels() {

    }

    public void testGetTunnelInformation() {

    }
    */
}
