package com.saucelabs.saucerest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.hamcrest.CoreMatchers;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SauceRESTTest {

    private SauceREST sauceREST;
    private MockHttpURLConnection urlConnection;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

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
        private List<InputStream> multipleMockInputStream;
        private int multipleMockInputStreamCounter = 0;

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

        protected MockHttpURLConnection(ExceptionThrowingMockOutputStream mockOutputStream) throws MalformedURLException {
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
        public InputStream getInputStream() throws IOException {
            if (multipleMockInputStream == null) {
                return mockInputStream;
            } else {
                // this allows us to specify multiple responses when we test requests or methods with multiple calls
                InputStream inputStream = multipleMockInputStream.get(multipleMockInputStreamCounter);
                multipleMockInputStreamCounter++;

                // reset counter back to 0 otherwise there won't be any other InputStream to return
                // this means we also loop back to return the first InputStream from the list
                if (multipleMockInputStreamCounter >= multipleMockInputStream.size()) {
                    multipleMockInputStreamCounter = multipleMockInputStream.size() - 1;
                }
                return inputStream;
            }
        }

        public void setInputStream(InputStream mockInputStream) {
            this.mockInputStream = mockInputStream;
        }

        public void setMultipleInputStreams(List<InputStream> multipleMockInputStream) {
            this.multipleMockInputStream = multipleMockInputStream;
        }

        public List<InputStream> getMultipleInputStreams() {
            return multipleMockInputStream;
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
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
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
    void testBuilder() {
        assertThrows(IllegalStateException.class, () ->
            new SauceRESTBuilder().setUsername(null).setAccessKey(null).build());
    }

    @Test
    void testUserAgent() {
        String agent = this.sauceREST.getUserAgent();
        assertNotNull(agent);
        assertThat(agent, not(CoreMatchers.containsString("/null")));
    }

    @Test
    void testConfirmSerializable() {
        SauceREST original = new SauceREST(null, null);
        SauceREST copy = (SauceREST) SerializationUtils.clone(original);
        assertEquals(original, copy);
    }

    @Test
    void testDoJSONPOST_Created() throws Exception {
        urlConnection.setInputStream(new ByteArrayInputStream(
            "{\"id\": \"29cee6f11f5e4ec6b8b62e98f79bba6f\"}".getBytes(StandardCharsets.UTF_8)
        ));
        urlConnection.setResponseCode(201);
        this.sauceREST.doJSONPOST(new URL("http://example.org/blah"), new JSONObject());
    }

    @Test
    void testDoJSONPOST_NotAuthorized() throws Exception {
        setConnectionThrowIOExceptionOnWrite();
        urlConnection.setResponseCode(401);
        URL url = new URL("http://example.org/blah");
        JSONObject body = new JSONObject();
        assertThrows(SauceException.NotAuthorized.class, () -> sauceREST.doJSONPOST(url, body));
    }

    @Test
    void testGetSupportedPlatforms_appium() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(getClass().getResource("/appium.json").openStream());

        String results = sauceREST.getSupportedPlatforms("appium");
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/info/platforms/appium");
    }

    @Test
    void testRecordCI() {
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
    void testGetUser() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(getClass().getResource("/user_test.json").openStream());
        String userInfo = sauceREST.getUser();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/users/" + this.sauceREST.getUsername() + "");
    }

    @Test
    void testGetStoredFiles() {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream(
            "[]".getBytes(StandardCharsets.UTF_8)
        ));
        String userInfo = sauceREST.getStoredFiles();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/storage/" + this.sauceREST.getUsername() + "");
    }

    @Test
    void testUpdateJobInfo() {
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
    void testUpdateJobInfo_NotAuthorized() throws Exception {
        setConnectionThrowIOExceptionOnClose();
        urlConnection.setResponseCode(401);

        HashMap<String, Object> updates = new HashMap<>();
        updates.put("passed", true);
        assertThrows(SauceException.NotAuthorized.class, () -> sauceREST.updateJobInfo("12345", updates));
    }

    @Test
    void testUpdateJobInfo_TooManyRequests() throws Exception {
        setConnectionThrowIOExceptionOnClose();
        urlConnection.setResponseCode(429);

        HashMap<String, Object> updates = new HashMap<>();
        updates.put("passed", true);
        assertThrows(SauceException.TooManyRequests.class, () -> sauceREST.updateJobInfo("12345", updates));
    }

    @Test
    void testGetTunnels() {
        urlConnection.setResponseCode(200);
        String userInfo = sauceREST.getTunnels();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/tunnels");
    }

    @Test
    void testGetTunnelInformation() {
        urlConnection.setResponseCode(200);
        String userInfo = sauceREST.getTunnelInformation("1234-1234-1231-123-123");
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/tunnels/1234-1234-1231-123-123");
    }

    @Test
    void testDeleteTunnel() throws Exception {
        urlConnection.setResponseCode(401);
        urlConnection.setInputStream(new ByteArrayInputStream("Not authorized".getBytes(StandardCharsets.UTF_8)));
        assertThrows(SauceException.NotAuthorized.class, () -> sauceREST.deleteTunnel("1234"));

        urlConnection.setResponseCode(404);
        urlConnection.setInputStream(new ByteArrayInputStream("Not found".getBytes(StandardCharsets.UTF_8)));
        assertThrows(SauceException.NotFound.class, () -> sauceREST.deleteTunnel("1234"));

        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{\"result\": true, \"id\": \"1234\", \"jobs_running\": 0}".getBytes(StandardCharsets.UTF_8)));

        String results;
        try (BufferedInputStream stream = sauceREST.deleteTunnel("1234")) {
            assertEquals("/rest/v1/" + sauceREST.getUsername() + "/tunnels/1234",
                urlConnection.getRealURL().getPath());

            results = IOUtils.toString(stream, StandardCharsets.UTF_8);
        }
        JSONObject jsonObject = new JSONObject(results);

        assertTrue(jsonObject.getBoolean("result"));
        assertEquals("1234", jsonObject.getString("id"));
        assertEquals(0, jsonObject.getInt("jobs_running"));
        assertFalse(jsonObject.isEmpty());
    }

    @Test
    void testGetActivity() {
        urlConnection.setResponseCode(200);
        String userInfo = sauceREST.getActivity();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/activity");
    }

    @Test
    void testGetConcurrency() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(getClass().getResource("/users_halkeye_concurrency.json").openStream());

        String concurencyInfo = sauceREST.getConcurrency();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/users/" + this.sauceREST.getUsername() + "/concurrency");
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertEquals(concurencyInfo, "{\"timestamp\": 1447392030.111457, \"concurrency\": {\"halkeye\": {\"current\": {\"overall\": 0, \"mac\": 0, \"manual\": 0}, \"remaining\": {\"overall\": 100, \"mac\": 100, \"manual\": 5}}}}");
    }

    @Test
    void testUploadFile() throws Exception {
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
    void testStopJob() {
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
    void testDeleteJob() {
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
    void testGetJobInfo() {
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
    void testRetrieveResults() {
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
    void testdownloadServerLog(@TempDir Path tempDir) {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

        String absolutePath = tempDir.toAbsolutePath().toString();
        sauceREST.downloadServerLog("1234", absolutePath);
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/selenium-server.log",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());

        boolean downloaded = sauceREST.downloadVideo("1234", absolutePath);
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/video.mp4",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertTrue(downloaded);
    }

    @Test
    void testdownloadServerLogWithCustomFileName(@TempDir Path tempDir) throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8.name())));

        String absolutePath = tempDir.toAbsolutePath().toString();
        boolean downloaded = sauceREST.downloadServerLog("1234", absolutePath, "foobar.log");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/selenium-server.log",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(tempDir.toFile().listFiles());
        assertEquals(1, tempDir.toFile().listFiles().length);
        assertEquals(tempDir.toFile().listFiles()[0].getName(), "foobar.log");
        assertTrue(downloaded);
    }

    @Test
    void testdownloadServerLogWithCustomFileNameEmptyDefaultFallback(@TempDir Path tempDir) throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8.name())));

        String absolutePath = tempDir.toAbsolutePath().toString();
        boolean downloaded = sauceREST.downloadServerLog("1234", absolutePath, "");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/selenium-server.log",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(tempDir.toFile().listFiles());
        assertEquals(1, tempDir.toFile().listFiles().length);
        assertTrue(tempDir.toFile().listFiles()[0].getName().endsWith(".log"));
        assertTrue(downloaded);
    }

    @Test
    void testdownloadServerLogWithCustomFileNameSlashed(@TempDir Path tempDir) throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8.name())));

        String absolutePath = tempDir.toAbsolutePath().toString();
        boolean downloaded = sauceREST.downloadServerLog("1234", absolutePath, "foo/bar.log");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/selenium-server.log",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(tempDir.toFile().listFiles());
        assertEquals(1, tempDir.toFile().listFiles().length);
        assertTrue(tempDir.toFile().listFiles()[0].getName().endsWith("foo_bar.log"));
        assertTrue(downloaded);
    }

    @Test
    void testDownloadServerLogWithFileNotFoundThrowsException(@TempDir Path tempDir) {
        urlConnection.setResponseCode(404);
        String location = tempDir.toAbsolutePath().toString();
        assertThrows(java.io.FileNotFoundException.class, () -> sauceREST.downloadServerLogOrThrow("1234", location));
    }

    @Test
    void testDownloadServerLogWithWrongCredentialsThrowsException(@TempDir Path tempDir) {
        urlConnection.setResponseCode(401);
        String location = tempDir.toAbsolutePath().toString();
        assertThrows(SauceException.NotAuthorized.class, () -> sauceREST.downloadServerLogOrThrow("1234", location));
    }

    @Test
    void testdownloadAutomatorLog(@TempDir Path tempDir) {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

        String absolutePath = tempDir.toAbsolutePath().toString();
        sauceREST.downloadAutomatorLog("1234", absolutePath);
        assertEquals("/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/automator.log", this.urlConnection.getRealURL().getPath());
        assertNull(this.urlConnection.getRealURL().getQuery());
    }

    @Test
    void testdownloadAutomatorLogWithCustomFileName(@TempDir Path tempDir) throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8.name())));

        String absolutePath = tempDir.toAbsolutePath().toString();
        boolean downloaded = sauceREST.downloadAutomatorLog("1234", absolutePath, "foobar.log");
        assertEquals("/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/automator.log", this.urlConnection.getRealURL().getPath());
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(tempDir.toFile().listFiles());
        assertEquals(1, tempDir.toFile().listFiles().length);
        assertEquals(tempDir.toFile().listFiles()[0].getName(), "foobar.log");
        assertTrue(downloaded);
    }

    @Test
    void testGetAvailableAssets() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(getClass().getResource("/assets.json").openStream());

        String results;
        try (BufferedInputStream stream = sauceREST.getAvailableAssets("1234")) {
            assertEquals("/rest/v1/" + sauceREST.getUsername() + "/jobs/1234/assets",
                urlConnection.getRealURL().getPath());

            results = IOUtils.toString(stream, StandardCharsets.UTF_8);
        }
        JSONObject jsonObject = new JSONObject(results);

        assertEquals("selenium-server.log", jsonObject.getString("selenium-log"));
        assertFalse(jsonObject.isEmpty());
        assertTrue(jsonObject.has("video"));
    }

    @Test
    void testGetAvailableAssets_NotFound() {
        urlConnection.setResponseCode(404);
        urlConnection.setInputStream(new ByteArrayInputStream("Not found".getBytes(StandardCharsets.UTF_8)));

        assertThrows(FileNotFoundException.class, () -> sauceREST.getAvailableAssets("1234"));
    }

    @Test
    void testDownloadAllAssets(@TempDir Path tempDir) throws IOException {
        List<InputStream> inputStreamList = Arrays.asList(
            new ByteArrayInputStream("{\"automation_backend\": \"appium\"}".getBytes(StandardCharsets.UTF_8)),
            getClass().getResource("/assets.json").openStream()
        );

        urlConnection.setResponseCode(200);
        urlConnection.setMultipleInputStreams(inputStreamList);

        String absolutePath = tempDir.toAbsolutePath().toString();
        sauceREST.downloadAllAssets("1234", absolutePath);

        assertNull(urlConnection.getRealURL().getQuery());
        assertNotNull(urlConnection.getRealURL().getPath());
        assertTrue(outContent.toString().contains("selenium-server.log"));
        assertTrue(outContent.toString().contains("logcat.log"));
        assertTrue(outContent.toString().contains("log.json"));
        assertTrue(outContent.toString().contains("video.mp4"));
        assertTrue(outContent.toString().contains("screenshots.zip"));
    }

    @Test
    void testDownloadScreenshots(@TempDir Path tempDir) {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

        boolean downloaded = sauceREST.downloadScreenshots("1234", tempDir.toAbsolutePath().toString());
        assertEquals("/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/screenshots.zip", this.urlConnection.getRealURL().getPath());
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertTrue(downloaded);
    }

    @Test
    void testDownloadScreenshotsWithCustomFileName(@TempDir Path tempDir) throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8.name())));

        String absolutePath = tempDir.toAbsolutePath().toString();
        boolean downloaded = sauceREST.downloadScreenshots("1234", absolutePath, "foobar.zip");
        assertEquals("/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/screenshots.zip", this.urlConnection.getRealURL().getPath());
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(tempDir.toFile().listFiles());
        assertEquals(1, tempDir.toFile().listFiles().length);
        assertEquals(tempDir.toFile().listFiles()[0].getName(), "foobar.zip");
        assertTrue(downloaded);
    }

    @Test
    void testDownloadScreenshotsWithWrongCredentialsThrowsException(@TempDir Path tempDir) {
        urlConnection.setResponseCode(401);
        String location = tempDir.toAbsolutePath().toString();
        assertThrows(SauceException.NotAuthorized.class, () -> sauceREST.downloadScreenshotsOrThrow("1234", location));
    }

    @Test
    void testDownloadScreenshotsWithFileNotFoundThrowsException(@TempDir Path tempDir) {
        urlConnection.setResponseCode(404);
        String location = tempDir.toAbsolutePath().toString();
        assertThrows(java.io.FileNotFoundException.class, () -> sauceREST.downloadScreenshotsOrThrow("1234", location));
    }

    @Test
    void testVideoDownload(@TempDir Path tempDir) {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

        boolean downloaded = sauceREST.downloadVideo("1234", tempDir.toAbsolutePath().toString());
        assertEquals(
            "/rest/v1/fakeuser/jobs/1234/assets/video.mp4",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertTrue(downloaded);
    }

    @Test
    void testDownloadVideoWithFileNotFoundThrowsException(@TempDir Path tempDir) {
        urlConnection.setResponseCode(404);
        String location = tempDir.toAbsolutePath().toString();
        assertThrows(java.io.FileNotFoundException.class, () -> sauceREST.downloadVideoOrThrow("1234", location));
    }

    @Test
    void testDownloadVideoWithWrongCredentialsThrowsException(@TempDir Path tempDir) {
        urlConnection.setResponseCode(401);
        String location = tempDir.toAbsolutePath().toString();
        assertThrows(SauceException.NotAuthorized.class, () -> sauceREST.downloadVideoOrThrow("1234", location));
    }

    @Test
    void testHARDownload(@TempDir Path tempDir) {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

        boolean downloaded = sauceREST.downloadHAR("1234", tempDir.toAbsolutePath().toString());
        assertEquals(
            "/v1/eds/1234/network.har",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertTrue(downloaded);
    }

    @Test
    void testDownloadHARWithFileNotFoundThrowsException(@TempDir Path tempDir) {
        urlConnection.setResponseCode(404);
        String location = tempDir.toAbsolutePath().toString();
        assertThrows(java.io.FileNotFoundException.class, () -> sauceREST.downloadHAROrThrow("1234", location));
    }

    @Test
    void testDownloadHARWithWrongCredentialsThrowsException(@TempDir Path tempDir) {
        urlConnection.setResponseCode(401);
        String location = tempDir.toAbsolutePath().toString();
        assertThrows(SauceException.NotAuthorized.class, () -> sauceREST.downloadHAROrThrow("1234", location));
    }

    @Test
    void downloadSauceLabsLog(@TempDir Path tempDir) throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8.name())));

        boolean downloaded = sauceREST.downloadSauceLabsLog("1234", tempDir.toAbsolutePath().toString());
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/log.json",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(tempDir.toFile().listFiles());
        assertEquals(1, tempDir.toFile().listFiles().length);

        assertTrue(tempDir.toFile().listFiles()[0].getName().endsWith(".json"));
        assertTrue(downloaded);
    }

    @Test
    void downloadSauceLabsLogStream() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8.name())));

        byte[] targetArray;
        try (BufferedInputStream stream = sauceREST.downloadSauceLabsLog("1234")) {
            assertEquals("/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/log.json",
                this.urlConnection.getRealURL().getPath());

            targetArray = IOUtils.toByteArray(stream);
        }
        assertTrue(targetArray.length > 0);
        assertNull(this.urlConnection.getRealURL().getQuery());
    }

    @Test
    void testDownloadDeviceLogOfEmulator(@TempDir Path tempDir) {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

        boolean downloaded = sauceREST.downloadDeviceLog("1234", tempDir.toAbsolutePath().toString(), true);
        assertEquals("/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/logcat.log", this.urlConnection.getRealURL().getPath());
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertTrue(downloaded);
    }

    @Test
    void testDownloadDeviceLogOfEmulatorWithCustomFilename(@TempDir Path tempDir) {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

        boolean downloaded = sauceREST.downloadDeviceLog("1234", tempDir.toAbsolutePath().toString(), "device.log", true);
        assertEquals("/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/logcat.log", this.urlConnection.getRealURL().getPath());
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(tempDir.toFile().listFiles());
        assertEquals(1, tempDir.toFile().listFiles().length);
        assertEquals(tempDir.toFile().listFiles()[0].getName(), "device.log");
        assertTrue(downloaded);
    }

    @Test
    void testDownloadDeviceLogOfEmulatorWithWrongCredentialsThrowsException(@TempDir Path tempDir) {
        urlConnection.setResponseCode(401);
        String location = tempDir.toAbsolutePath().toString();
        assertThrows(SauceException.NotAuthorized.class, () -> sauceREST.downloadDeviceLogOrThrow("1234", location, true));
    }

    @Test
    void testDownloadDeviceLogOfEmulatorWithFileNotFoundThrowsException(@TempDir Path tempDir) {
        urlConnection.setResponseCode(404);
        String location = tempDir.toAbsolutePath().toString();
        assertThrows(java.io.FileNotFoundException.class, () -> sauceREST.downloadDeviceLogOrThrow("1234", location, true));
    }

    @Test
    void testDownloadDeviceLogOfSimulator(@TempDir Path tempDir) {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

        boolean downloaded = sauceREST.downloadDeviceLog("1234", tempDir.toAbsolutePath().toString(), false);
        assertEquals("/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/ios-syslog.log", this.urlConnection.getRealURL().getPath());
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertTrue(downloaded);
    }

    @Test
    void testDownloadDeviceLogOfSimulatorWithCustomFilename(@TempDir Path tempDir) {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

        boolean downloaded = sauceREST.downloadDeviceLog("1234", tempDir.toAbsolutePath().toString(), "device.log", false);
        assertEquals("/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/ios-syslog.log", this.urlConnection.getRealURL().getPath());
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(tempDir.toFile().listFiles());
        assertEquals(1, tempDir.toFile().listFiles().length);
        assertEquals(tempDir.toFile().listFiles()[0].getName(), "device.log");
        assertTrue(downloaded);
    }

    @Test
    void testDownloadDeviceLogOfSimulatorWithWrongCredentialsThrowsException(@TempDir Path tempDir) {
        urlConnection.setResponseCode(401);
        String location = tempDir.toAbsolutePath().toString();
        assertThrows(SauceException.NotAuthorized.class, () -> sauceREST.downloadDeviceLogOrThrow("1234", location, false));
    }

    @Test
    void testDownloadDeviceLogOfSimulatorWithFileNotFoundThrowsException(@TempDir Path tempDir) {
        urlConnection.setResponseCode(404);
        String location = tempDir.toAbsolutePath().toString();
        assertThrows(java.io.FileNotFoundException.class, () -> sauceREST.downloadDeviceLogOrThrow("1234", location, false));
    }

    @Test
    void testDownloadServerLogWithRenaming(@TempDir Path tempDir) throws IOException {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(getClass().getResource("/appium-server.log").openStream());

        String absolutePath = tempDir.toAbsolutePath().toString();
        boolean downloaded = sauceREST.downloadServerLog("1234", absolutePath);
        assertEquals("/rest/v1/" + sauceREST.getUsername() + "/jobs/1234/assets/selenium-server.log", urlConnection.getRealURL().getPath());
        assertTrue(downloaded);
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(tempDir.toFile().listFiles());
        assertEquals(1, tempDir.toFile().listFiles().length);
        assertTrue(tempDir.toFile().listFiles()[0].length() > 0);
        assertTrue(tempDir.toFile().listFiles()[0].getName().contains("appium-server.log"), "Actual:" + tempDir.toFile().list()[0]);
    }

    @Test
    void testDownloadServerLogWithoutRenaming(@TempDir Path tempDir) throws IOException {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(getClass().getResource("/selenium-server.log").openStream());

        String absolutePath = tempDir.toAbsolutePath().toString();
        boolean downloaded = sauceREST.downloadServerLog("1234", absolutePath);
        assertEquals("/rest/v1/" + sauceREST.getUsername() + "/jobs/1234/assets/selenium-server.log", urlConnection.getRealURL().getPath());
        assertTrue(downloaded);
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(tempDir.toFile().listFiles());
        assertEquals(1, tempDir.toFile().listFiles().length);
        assertTrue(tempDir.toFile().listFiles()[0].length() > 0);
        assertTrue(tempDir.toFile().listFiles()[0].getName().contains("selenium-server.log"), "Actual:" + tempDir.toFile().list()[0]);
    }

    @Test
    void testDownload(@TempDir Path tempDir) {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));

        String absolutePath = tempDir.toAbsolutePath().toString();
        sauceREST.downloadLog("1234", absolutePath);
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/selenium-server.log",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());

        boolean downloaded = sauceREST.downloadVideo("1234", absolutePath);
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/video.mp4",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertTrue(downloaded);
    }

    @Test
    void testDownloadWithCustomFileName(@TempDir Path tempDir) throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8.name())));

        String absolutePath = tempDir.toAbsolutePath().toString();
        boolean downloaded = sauceREST.downloadLog("1234", absolutePath, "foobar.log");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/selenium-server.log",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(tempDir.toFile().listFiles());
        assertEquals(1, tempDir.toFile().listFiles().length);
        assertEquals(tempDir.toFile().listFiles()[0].getName(), "foobar.log");
        assertTrue(downloaded);
    }

    @Test
    void testDownloadWithCustomFileNameEmptyDefaultFallback(@TempDir Path tempDir) throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8.name())));

        String absolutePath = tempDir.toAbsolutePath().toString();
        boolean downloaded = sauceREST.downloadLog("1234", absolutePath, "");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/selenium-server.log",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(tempDir.toFile().listFiles());
        assertEquals(1, tempDir.toFile().listFiles().length);
        assertTrue(tempDir.toFile().listFiles()[0].getName().endsWith(".log"));
        assertTrue(downloaded);
    }

    @Test
    void testDownloadWithCustomFileNameSlashed(@TempDir Path tempDir) throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8.name())));

        String absolutePath = tempDir.toAbsolutePath().toString();
        boolean downloaded = sauceREST.downloadLog("1234", absolutePath, "foo/bar.log");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/selenium-server.log",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(tempDir.toFile().listFiles());
        assertEquals(1, tempDir.toFile().listFiles().length);
        assertTrue(tempDir.toFile().listFiles()[0].getName().endsWith("foo_bar.log"));
        assertTrue(downloaded);
    }

    @Test
    void testDownloadWithFileNotFoundThrowsException(@TempDir Path tempDir) {
        urlConnection.setResponseCode(404);
        String location = tempDir.toAbsolutePath().toString();
        assertThrows(java.io.FileNotFoundException.class, () -> sauceREST.downloadLogOrThrow("1234", location));
    }

    @Test
    void testDownloadLogWithWrongCredentialsThrowsException(@TempDir Path tempDir) {
        urlConnection.setResponseCode(401);
        String location = tempDir.toAbsolutePath().toString();
        assertThrows(SauceException.NotAuthorized.class, () -> sauceREST.downloadLogOrThrow("1234", location));
    }

    @Test
    void testJobFailed() {
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
    void testJobPassed() {
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
    void testAddTagsEmpty() {
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
    void testAddTags() {
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
    void testGetFullJobs() {
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
    void testGetJobs() {
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
    void testAddAuthenticationProperty() {

    }

    void testOpenConnection() {

    }

    void testGetPublicJobLink() {

    }

    void testEncodeAuthentication() {

    }

    void testDeleteTunnel() {

    }

    void testGetTunnels() {

    }

    void testGetTunnelInformation() {

    }
    */
}
