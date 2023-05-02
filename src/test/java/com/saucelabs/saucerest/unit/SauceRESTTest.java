package com.saucelabs.saucerest.unit;

import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.SauceRESTBuilder;
import com.saucelabs.saucerest.api.RealDevicesEndpoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Execution(ExecutionMode.SAME_THREAD)
@ExtendWith(MockitoExtension.class)
class SauceRESTTest {
    @Mock
    private RealDevicesEndpoint mockRealDevicesEndpoint;

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
        public InputStream getInputStream() {
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

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }


    @Test
    void testBuilder() {
        assertThrows(IllegalStateException.class, () ->
                new SauceRESTBuilder().setUsername(null).setAccessKey(null).build());
    }

//    @Test
//    void testDownloadServerLog(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(200);
//        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));
//
//        String absolutePath = tempDir.toAbsolutePath().toString();
//        sauceREST.downloadServerLog("1234", absolutePath);
//        assertEquals(
//            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/selenium-server.log",
//            this.urlConnection.getRealURL().getPath()
//        );
//        assertNull(this.urlConnection.getRealURL().getQuery());
//
//        boolean downloaded = sauceREST.downloadVideo("1234", absolutePath);
//        assertEquals(
//            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/video.mp4",
//            this.urlConnection.getRealURL().getPath()
//        );
//        assertNull(this.urlConnection.getRealURL().getQuery());
//        assertTrue(downloaded);
//    }
//
//    @Test
//    void testDownloadServerLogWithCustomFileName(@TempDir Path tempDir) throws Exception {
//        urlConnection.setResponseCode(200);
//        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));
//
//        String absolutePath = tempDir.toAbsolutePath().toString();
//        boolean downloaded = sauceREST.downloadServerLog("1234", absolutePath, "foobar.log");
//        assertEquals(
//            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/selenium-server.log",
//            this.urlConnection.getRealURL().getPath()
//        );
//        assertNull(this.urlConnection.getRealURL().getQuery());
//        assertNotNull(tempDir.toFile().listFiles());
//        assertEquals(1, tempDir.toFile().listFiles().length);
//        assertEquals(tempDir.toFile().listFiles()[0].getName(), "foobar.log");
//        assertTrue(downloaded);
//    }
//
//    @Test
//    void testDownloadServerLogWithCustomFileNameEmptyDefaultFallback(@TempDir Path tempDir) throws Exception {
//        urlConnection.setResponseCode(200);
//        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));
//
//        String absolutePath = tempDir.toAbsolutePath().toString();
//        boolean downloaded = sauceREST.downloadServerLog("1234", absolutePath, "");
//        assertEquals(
//            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/selenium-server.log",
//            this.urlConnection.getRealURL().getPath()
//        );
//        assertNull(this.urlConnection.getRealURL().getQuery());
//        assertNotNull(tempDir.toFile().listFiles());
//        assertEquals(1, tempDir.toFile().listFiles().length);
//        assertTrue(tempDir.toFile().listFiles()[0].getName().endsWith(".log"));
//        assertTrue(downloaded);
//    }
//
//    @Test
//    void testDownloadServerLogWithCustomFileNameSlashed(@TempDir Path tempDir) throws Exception {
//        urlConnection.setResponseCode(200);
//        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));
//
//        String absolutePath = tempDir.toAbsolutePath().toString();
//        boolean downloaded = sauceREST.downloadServerLog("1234", absolutePath, "foo/bar.log");
//        assertEquals(
//            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/selenium-server.log",
//            this.urlConnection.getRealURL().getPath()
//        );
//        assertNull(this.urlConnection.getRealURL().getQuery());
//        assertNotNull(tempDir.toFile().listFiles());
//        assertEquals(1, tempDir.toFile().listFiles().length);
//        assertTrue(tempDir.toFile().listFiles()[0].getName().endsWith("foo_bar.log"));
//        assertTrue(downloaded);
//    }
//
//    @Test
//    void testDownloadServerLogWithFileNotFoundThrowsException(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(404);
//        String location = tempDir.toAbsolutePath().toString();
//        assertThrows(java.io.FileNotFoundException.class, () -> sauceREST.downloadServerLogOrThrow("1234", location));
//    }
//
//    @Test
//    void testDownloadServerLogWithWrongCredentialsThrowsException(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(401);
//        String location = tempDir.toAbsolutePath().toString();
//        assertThrows(SauceException.NotAuthorized.class, () -> sauceREST.downloadServerLogOrThrow("1234", location));
//    }
//
//    @Test
//    void testDownloadAutomatorLog(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(200);
//        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));
//
//        String absolutePath = tempDir.toAbsolutePath().toString();
//        sauceREST.downloadAutomatorLog("1234", absolutePath);
//        assertEquals("/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/automator.log", this.urlConnection.getRealURL().getPath());
//        assertNull(this.urlConnection.getRealURL().getQuery());
//    }
//
//    @Test
//    void testDownloadAutomatorLogWithCustomFileName(@TempDir Path tempDir) throws Exception {
//        urlConnection.setResponseCode(200);
//        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));
//
//        String absolutePath = tempDir.toAbsolutePath().toString();
//        boolean downloaded = sauceREST.downloadAutomatorLog("1234", absolutePath, "foobar.log");
//        assertEquals("/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/automator.log", this.urlConnection.getRealURL().getPath());
//        assertNull(this.urlConnection.getRealURL().getQuery());
//        assertNotNull(tempDir.toFile().listFiles());
//        assertEquals(1, tempDir.toFile().listFiles().length);
//        assertEquals(tempDir.toFile().listFiles()[0].getName(), "foobar.log");
//        assertTrue(downloaded);
//    }
//
//    @Test
//    void testGetAvailableAssets() throws Exception {
//        urlConnection.setResponseCode(200);
//        urlConnection.setInputStream(getClass().getResource("/assets.json").openStream());
//
//        String results;
//        try (BufferedInputStream stream = sauceREST.getAvailableAssets("1234")) {
//            assertEquals("/rest/v1/" + sauceREST.getUsername() + "/jobs/1234/assets",
//                urlConnection.getRealURL().getPath());
//
//            results = IOUtils.toString(stream, StandardCharsets.UTF_8);
//        }
//        JSONObject jsonObject = new JSONObject(results);
//
//        assertEquals("selenium-server.log", jsonObject.getString("selenium-log"));
//        assertFalse(jsonObject.isEmpty());
//        assertTrue(jsonObject.has("video"));
//    }
//
//    @Test
//    void testGetAvailableAssets_NotFound() {
//        urlConnection.setResponseCode(404);
//        urlConnection.setInputStream(new ByteArrayInputStream("Not found".getBytes(StandardCharsets.UTF_8)));
//
//        assertThrows(FileNotFoundException.class, () -> sauceREST.getAvailableAssets("1234"));
//    }
//
//    /**
//     * This test is not thread/parallel safe. When run fully parallelized will fail.
//     */
//    @Test
//    void testDownloadAllAssets(@TempDir Path tempDir) throws IOException {
//        List<InputStream> inputStreamList = Arrays.asList(
//            new ByteArrayInputStream("{\"automation_backend\": \"appium\"}".getBytes(StandardCharsets.UTF_8)),
//            getClass().getResource("/assets.json").openStream()
//        );
//
//        urlConnection.setResponseCode(200);
//        urlConnection.setMultipleInputStreams(inputStreamList);
//
//        String absolutePath = tempDir.toAbsolutePath().toString();
//        sauceREST.downloadAllAssets("1234", absolutePath);
//
//        assertNull(urlConnection.getRealURL().getQuery());
//        assertNotNull(urlConnection.getRealURL().getPath());
//        assertTrue(outContent.toString().contains("selenium-server.log"));
//        assertTrue(outContent.toString().contains("logcat.log"));
//        assertTrue(outContent.toString().contains("log.json"));
//        assertTrue(outContent.toString().contains("video.mp4"));
//        assertTrue(outContent.toString().contains("screenshots.zip"));
//    }
//
//    @Test
//    void testDownloadScreenshots(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(200);
//        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));
//
//        boolean downloaded = sauceREST.downloadScreenshots("1234", tempDir.toAbsolutePath().toString());
//        assertEquals("/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/screenshots.zip", this.urlConnection.getRealURL().getPath());
//        assertNull(this.urlConnection.getRealURL().getQuery());
//        assertTrue(downloaded);
//    }
//
//    @Test
//    void testDownloadScreenshotsWithCustomFileName(@TempDir Path tempDir) throws Exception {
//        urlConnection.setResponseCode(200);
//        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));
//
//        String absolutePath = tempDir.toAbsolutePath().toString();
//        boolean downloaded = sauceREST.downloadScreenshots("1234", absolutePath, "foobar.zip");
//        assertEquals("/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/screenshots.zip", this.urlConnection.getRealURL().getPath());
//        assertNull(this.urlConnection.getRealURL().getQuery());
//        assertNotNull(tempDir.toFile().listFiles());
//        assertEquals(1, tempDir.toFile().listFiles().length);
//        assertEquals(tempDir.toFile().listFiles()[0].getName(), "foobar.zip");
//        assertTrue(downloaded);
//    }
//
//    @Test
//    void testDownloadScreenshotsWithWrongCredentialsThrowsException(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(401);
//        String location = tempDir.toAbsolutePath().toString();
//        assertThrows(SauceException.NotAuthorized.class, () -> sauceREST.downloadScreenshotsOrThrow("1234", location));
//    }
//
//    @Test
//    void testDownloadScreenshotsWithFileNotFoundThrowsException(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(404);
//        String location = tempDir.toAbsolutePath().toString();
//        assertThrows(java.io.FileNotFoundException.class, () -> sauceREST.downloadScreenshotsOrThrow("1234", location));
//    }
//
//    @Test
//    void testVideoDownload(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(200);
//        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));
//
//        boolean downloaded = sauceREST.downloadVideo("1234", tempDir.toAbsolutePath().toString());
//        assertEquals(
//            "/rest/v1/fakeuser/jobs/1234/assets/video.mp4",
//            this.urlConnection.getRealURL().getPath()
//        );
//        assertNull(this.urlConnection.getRealURL().getQuery());
//        assertTrue(downloaded);
//    }
//
//    @Test
//    void testDownloadVideoWithFileNotFoundThrowsException(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(404);
//        String location = tempDir.toAbsolutePath().toString();
//        assertThrows(java.io.FileNotFoundException.class, () -> sauceREST.downloadVideoOrThrow("1234", location));
//    }
//
//    @Test
//    void testDownloadVideoWithWrongCredentialsThrowsException(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(401);
//        String location = tempDir.toAbsolutePath().toString();
//        assertThrows(SauceException.NotAuthorized.class, () -> sauceREST.downloadVideoOrThrow("1234", location));
//    }
//
//    @Test
//    void testHARDownload(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(200);
//        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));
//
//        boolean downloaded = sauceREST.downloadHAR("1234", tempDir.toAbsolutePath().toString());
//        assertEquals(
//            "/v1/eds/1234/network.har",
//            this.urlConnection.getRealURL().getPath()
//        );
//        assertNull(this.urlConnection.getRealURL().getQuery());
//        assertTrue(downloaded);
//    }
//
//    @Test
//    void testDownloadHARWithFileNotFoundThrowsException(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(404);
//        String location = tempDir.toAbsolutePath().toString();
//        assertThrows(java.io.FileNotFoundException.class, () -> sauceREST.downloadHAROrThrow("1234", location));
//    }
//
//    @Test
//    void testDownloadHARWithWrongCredentialsThrowsException(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(401);
//        String location = tempDir.toAbsolutePath().toString();
//        assertThrows(SauceException.NotAuthorized.class, () -> sauceREST.downloadHAROrThrow("1234", location));
//    }
//
//    @Test
//    void testDownloadSauceLabsLog(@TempDir Path tempDir) throws Exception {
//        urlConnection.setResponseCode(200);
//        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));
//
//        boolean downloaded = sauceREST.downloadSauceLabsLog("1234", tempDir.toAbsolutePath().toString());
//        assertEquals(
//            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/log.json",
//            this.urlConnection.getRealURL().getPath()
//        );
//        assertNull(this.urlConnection.getRealURL().getQuery());
//        assertNotNull(tempDir.toFile().listFiles());
//        assertEquals(1, tempDir.toFile().listFiles().length);
//
//        assertTrue(tempDir.toFile().listFiles()[0].getName().endsWith(".json"));
//        assertTrue(downloaded);
//    }
//
//    @Test
//    void testDownloadSauceLabsLogStream() throws Exception {
//        urlConnection.setResponseCode(200);
//        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));
//
//        byte[] targetArray;
//        try (BufferedInputStream stream = sauceREST.downloadSauceLabsLog("1234")) {
//            assertEquals("/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/log.json",
//                this.urlConnection.getRealURL().getPath());
//
//            targetArray = IOUtils.toByteArray(stream);
//        }
//        assertTrue(targetArray.length > 0);
//        assertNull(this.urlConnection.getRealURL().getQuery());
//    }
//
//    @Test
//    void testDownloadDeviceLogOfEmulator(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(200);
//        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));
//
//        boolean downloaded = sauceREST.downloadDeviceLog("1234", tempDir.toAbsolutePath().toString(), true);
//        assertEquals("/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/logcat.log", this.urlConnection.getRealURL().getPath());
//        assertNull(this.urlConnection.getRealURL().getQuery());
//        assertTrue(downloaded);
//    }
//
//    @Test
//    void testDownloadDeviceLogOfEmulatorWithCustomFilename(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(200);
//        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));
//
//        boolean downloaded = sauceREST.downloadDeviceLog("1234", tempDir.toAbsolutePath().toString(), "device.log", true);
//        assertEquals("/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/logcat.log", this.urlConnection.getRealURL().getPath());
//        assertNull(this.urlConnection.getRealURL().getQuery());
//        assertNotNull(tempDir.toFile().listFiles());
//        assertEquals(1, tempDir.toFile().listFiles().length);
//        assertEquals(tempDir.toFile().listFiles()[0].getName(), "device.log");
//        assertTrue(downloaded);
//    }
//
//    @Test
//    void testDownloadDeviceLogOfEmulatorWithWrongCredentialsThrowsException(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(401);
//        String location = tempDir.toAbsolutePath().toString();
//        assertThrows(SauceException.NotAuthorized.class, () -> sauceREST.downloadDeviceLogOrThrow("1234", location, true));
//    }
//
//    @Test
//    void testDownloadDeviceLogOfEmulatorWithFileNotFoundThrowsException(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(404);
//        String location = tempDir.toAbsolutePath().toString();
//        assertThrows(java.io.FileNotFoundException.class, () -> sauceREST.downloadDeviceLogOrThrow("1234", location, true));
//    }
//
//    @Test
//    void testDownloadDeviceLogOfSimulator(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(200);
//        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));
//
//        boolean downloaded = sauceREST.downloadDeviceLog("1234", tempDir.toAbsolutePath().toString(), false);
//        assertEquals("/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/ios-syslog.log", this.urlConnection.getRealURL().getPath());
//        assertNull(this.urlConnection.getRealURL().getQuery());
//        assertTrue(downloaded);
//    }
//
//    @Test
//    void testDownloadDeviceLogOfSimulatorWithCustomFilename(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(200);
//        urlConnection.setInputStream(new ByteArrayInputStream("{ }".getBytes(StandardCharsets.UTF_8)));
//
//        boolean downloaded = sauceREST.downloadDeviceLog("1234", tempDir.toAbsolutePath().toString(), "device.log", false);
//        assertEquals("/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234/assets/ios-syslog.log", this.urlConnection.getRealURL().getPath());
//        assertNull(this.urlConnection.getRealURL().getQuery());
//        assertNotNull(tempDir.toFile().listFiles());
//        assertEquals(1, tempDir.toFile().listFiles().length);
//        assertEquals(tempDir.toFile().listFiles()[0].getName(), "device.log");
//        assertTrue(downloaded);
//    }
//
//    @Test
//    void testDownloadDeviceLogOfSimulatorWithWrongCredentialsThrowsException(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(401);
//        String location = tempDir.toAbsolutePath().toString();
//        assertThrows(SauceException.NotAuthorized.class, () -> sauceREST.downloadDeviceLogOrThrow("1234", location, false));
//    }
//
//    @Test
//    void testDownloadDeviceLogOfSimulatorWithFileNotFoundThrowsException(@TempDir Path tempDir) {
//        urlConnection.setResponseCode(404);
//        String location = tempDir.toAbsolutePath().toString();
//        assertThrows(java.io.FileNotFoundException.class, () -> sauceREST.downloadDeviceLogOrThrow("1234", location, false));
//    }
//
//    @Test
//    void testDownloadServerLogWithRenaming(@TempDir Path tempDir) throws IOException {
//        urlConnection.setResponseCode(200);
//        urlConnection.setInputStream(getClass().getResource("/appium-server.log").openStream());
//
//        String absolutePath = tempDir.toAbsolutePath().toString();
//        boolean downloaded = sauceREST.downloadServerLog("1234", absolutePath);
//        assertEquals("/rest/v1/" + sauceREST.getUsername() + "/jobs/1234/assets/selenium-server.log", urlConnection.getRealURL().getPath());
//        assertTrue(downloaded);
//        assertNull(this.urlConnection.getRealURL().getQuery());
//        assertNotNull(tempDir.toFile().listFiles());
//        assertEquals(1, tempDir.toFile().listFiles().length);
//        assertTrue(tempDir.toFile().listFiles()[0].length() > 0);
//        assertTrue(tempDir.toFile().listFiles()[0].getName().contains("appium-server.log"), "Actual:" + tempDir.toFile().list()[0]);
//    }
//
//    @Test
//    void testDownloadServerLogWithoutRenaming(@TempDir Path tempDir) throws IOException {
//        urlConnection.setResponseCode(200);
//        urlConnection.setInputStream(getClass().getResource("/selenium-server.log").openStream());
//
//        String absolutePath = tempDir.toAbsolutePath().toString();
//        boolean downloaded = sauceREST.downloadServerLog("1234", absolutePath);
//        assertEquals("/rest/v1/" + sauceREST.getUsername() + "/jobs/1234/assets/selenium-server.log", urlConnection.getRealURL().getPath());
//        assertTrue(downloaded);
//        assertNull(this.urlConnection.getRealURL().getQuery());
//        assertNotNull(tempDir.toFile().listFiles());
//        assertEquals(1, tempDir.toFile().listFiles().length);
//        assertTrue(tempDir.toFile().listFiles()[0].length() > 0);
//        assertTrue(tempDir.toFile().listFiles()[0].getName().contains("selenium-server.log"), "Actual:" + tempDir.toFile().list()[0]);
//    }
}