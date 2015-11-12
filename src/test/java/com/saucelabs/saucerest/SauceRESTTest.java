package com.saucelabs.saucerest;

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
import java.util.HashMap;

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
        this.sauceREST.doJSONPOST(new URL("http://example.org/blah"), new JSONObject());
    }

    @Test(expected=SauceException.NotAuthorized.class)
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
        assertEquals(JSONValue.parse(output), JSONValue.parse("{\"platform_version\":\"1.1\",\"platform\":\"jenkins\"}"));
    }


    @Test
    public void testGetUser() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(getClass().getResource("/user_test.json").openStream());
        String userInfo = sauceREST.getUser();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/users/fakeuser");
    }

    @Test
    public void testGetStoredFiles() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream(
            "[]".getBytes("UTF-8")
        ));
        String userInfo = sauceREST.getStoredFiles();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/storage/fakeuser");
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
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/fakeuser/jobs/12345");

        String output = this.urlConnection.getOutputStream().toString();
        assertEquals(JSONValue.parse(output), JSONValue.parse("{\"public\":\"shared\"}"));
    }


    public void testGetTunnels() throws Exception {
        urlConnection.setResponseCode(200);
        String userInfo = sauceREST.getTunnels();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/fakeuser/tunnels");
    }

    public void testGetTunnelInformation() throws Exception {
        urlConnection.setResponseCode(200);
        String userInfo = sauceREST.getTunnelInformation("1234-1234-1231-123-123");
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/fakeuser/tunnels/1234-1234-1231-123-123");
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

    public void testGetConcurrency() throws Exception {

    }

    public void testGetActivity() throws Exception {

    }

    public void testGetJobsList() throws Exception {

    }
    */
}