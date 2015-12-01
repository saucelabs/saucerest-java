package com.saucelabs.saucerest;

import com.saucelabs.saucerest.objects.*;
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
import java.util.*;

import static org.hamcrest.CoreMatchers.containsString;
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
        private String realMethod;

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

        public void setRealMethod(String realMethod) {
            this.realMethod = realMethod;
        }

        public String getRealMethod() {
            return realMethod;
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

            @Override
            public String doREST(String method, URL url, JSONObject body) throws SauceException {
                SauceRESTTest.this.urlConnection.setRealMethod(method);
                return super.doREST(method, url, body);
            }
        };
    }

    @Test
    public void testDoREST_GET() throws Exception {
        this.sauceREST = new SauceREST("fakeuser", "fakekey");
        String ret = this.sauceREST.doREST("GET", new URL("https://saucelabs.com/test/guinea-pig"), null);
        assertNotNull(ret);
        assertThat(ret, containsString("<title>I am a page title - Sauce Labs</title>"));
    }

    @Test
    public void testDoJSONPOST_Created() throws Exception {
        urlConnection.setInputStream(new ByteArrayInputStream(
            "{\"id\": \"29cee6f11f5e4ec6b8b62e98f79bba6f\"}".getBytes("UTF-8")
        ));
        urlConnection.setResponseCode(201);
        this.sauceREST.doREST("POST", new URL("http://example.org/blah"), new JSONObject());
    }

    @Test(expected = SauceException.NotAuthorized.class)
    public void testDoJSONPOST_NotAuthorized() throws Exception {
        urlConnection.setResponseCode(401);

        thrown.expect(SauceException.NotAuthorized.class);
        this.sauceREST.doREST("POST", new URL("http://example.org/blah"), new JSONObject());
    }

    @Test
    public void testGetSupportedPlatforms() throws Exception {
        urlConnection.setResponseCode(200);
        List<Platform> platforms;

        urlConnection.setInputStream(getClass().getResource("/appium.json").openStream());
        platforms = sauceREST.getSupportedPlatforms("appium");
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/info/platforms/appium");
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(platforms);
        assertEquals(86, platforms.size());
        assertEquals("iPad", platforms.get(0).longName);
        assertEquals(
            Arrays.asList("1.0.0", "1.1.0", "1.2.0", "1.2.4", "1.3.3", "1.3.4", "1.3.6", "1.4.3", "1.4.7", "1.4.10"),
            platforms.get(0).deprecatedBackendVersions
        );
        assertEquals("iPhone", platforms.get(3).longName);

        urlConnection.setInputStream(getClass().getResource("/webdriver.json").openStream());
        platforms = sauceREST.getSupportedPlatforms("webdriver");
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/info/platforms/webdriver");
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(platforms);
        assertEquals(686, platforms.size());
        assertEquals("iPad", platforms.get(0).longName);
        assertNull(platforms.get(0).deprecatedBackendVersions);
        assertEquals("Firefox", platforms.get(3).longName);
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
        User userInfo = sauceREST.getUser();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/users/" + this.sauceREST.getUsername() + "");
        assertNotNull(userInfo);

        assertEquals("this-is-a-fake-access-key", userInfo.getAccessKey());
        assertEquals(null, userInfo.getDomain());
        assertEquals("Last Test", userInfo.getLastName());
        assertEquals("First Test", userInfo.getFirstName());
        assertEquals("test", userInfo.getId());
        assertEquals(null, userInfo.getName());
        assertEquals(null, userInfo.getTitle());
        assertEquals("test@test.com", userInfo.getEmail());
        assertEquals("individual", userInfo.getEntityType());

        assertEquals(new Date((long) 1265155800 * 1000), userInfo.getCreationTime());
        assertEquals("free", userInfo.getUserType());

        assertEquals(2, userInfo.getConcurrencyLimit().getMac());
        assertEquals(2, userInfo.getConcurrencyLimit().getScout());
        assertEquals(2, userInfo.getConcurrencyLimit().getOverall());
        assertEquals(0, userInfo.getConcurrencyLimit().getRealDevice());

        assertEquals(2, userInfo.getAncestorConcurrencyLimit().getMac());
        assertEquals(2, userInfo.getAncestorConcurrencyLimit().getScout());
        assertEquals(2, userInfo.getAncestorConcurrencyLimit().getOverall());
        assertEquals(0, userInfo.getAncestorConcurrencyLimit().getRealDevice());

        assertEquals(45, userInfo.getManualMinutes());
        assertEquals(160, userInfo.getMinutes());

        assertEquals(Arrays.asList("marketing"), userInfo.getPreventEmails());

        assertEquals(false, userInfo.getVerified());
        assertEquals(false, userInfo.getSubscribed());
        assertEquals(true, userInfo.getAncestorAllowsSubaccounts());
        assertEquals(false, userInfo.getVmLockdown());
        assertEquals(null, userInfo.getParent());
        assertEquals(null, userInfo.getIsAdmin());
        assertEquals(true, userInfo.getCanRunManual());
        assertEquals(false, userInfo.getIsSso());

        urlConnection.setResponseCode(401);
        urlConnection.setInputStream(new ByteArrayInputStream(
            "{\"error\": \"user does not exist or access denied\"}".getBytes("UTF-8")
        ));
        assertNull(sauceREST.getUser());
    }

    @Test
    public void testGetStoredFiles() throws Exception {
        List<com.saucelabs.saucerest.objects.File> files;
        urlConnection.setResponseCode(200);

        urlConnection.setInputStream(getClass().getResource("/storage_empty.json").openStream());
        files = sauceREST.getStoredFiles();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/storage/" + this.sauceREST.getUsername() + "");
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(files);
        assertEquals(0, files.size());

        urlConnection.setInputStream(getClass().getResource("/storage_files.json").openStream());
        files = sauceREST.getStoredFiles();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/storage/" + this.sauceREST.getUsername() + "");
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(files);
        assertEquals(1, files.size());
        assertEquals("cd6d9784b26d865cd1ede9610197e279", files.get(0).getMd5());
        assertEquals("Garod-2.png", files.get(0).getName());
        assertEquals(1448843959891L, files.get(0).getMtime().getTime());
        assertEquals(80375, files.get(0).getSize());
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
        assertNull(this.urlConnection.getRealURL().getQuery());

        String output = this.urlConnection.getOutputStream().toString();
        assertEquals(JSONValue.parse(output), JSONValue.parse("{\"public\":\"shared\"}"));
    }

    public void testGetJobInfo() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(getClass().getResource("/job_info.json").openStream());
        Job job = this.sauceREST.getJobInfo("1234");
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/1234");
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(job);
        assertEquals("Sauce Sample Test", job.getName());
    }

    public void testGetTunnels() throws Exception {
        List<String> tunnels;
        urlConnection.setResponseCode(200);

        urlConnection.setInputStream(new ByteArrayInputStream("[]".getBytes("UTF-8")));
        tunnels = sauceREST.getTunnels();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/tunnels");
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(tunnels);
        assertEquals(0, tunnels.size());

        urlConnection.setInputStream(new ByteArrayInputStream("[\"0ec525b62b4e47a6a77e5185e9f40b2d\"]".getBytes("UTF-8")));
        tunnels = sauceREST.getTunnels();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/tunnels");
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(tunnels);
        assertEquals(1, tunnels.size());
        assertEquals("0ec525b62b4e47a6a77e5185e9f40b2d", tunnels.get(0));
    }

    public void testDeleteTunnel() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("".getBytes("UTF-8")));

        sauceREST.deleteTunnel("0ec525b62b4e47a6a77e5185e9f40b2d");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/tunnels/0ec525b62b4e47a6a77e5185e9f40b2d",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertEquals("DELETE", this.urlConnection.getRealMethod());
    }

    public void testGetTunnelInformation() throws Exception {
        Tunnel tunnel = null;

        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("{\"errors\": \"Tunnel not found\"}".getBytes("UTF-8")));
        tunnel = sauceREST.getTunnelInformation("0ec525b62b4e47a6a77e5185e9f40b2d");
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/tunnels/0ec525b62b4e47a6a77e5185e9f40b2d");
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNull(tunnel);

        urlConnection.setResponseCode(404);
        urlConnection.setInputStream(getClass().getResource("/single_tunnel.json").openStream());
        tunnel = sauceREST.getTunnelInformation("0ec525b62b4e47a6a77e5185e9f40b2d");
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/tunnels/0ec525b62b4e47a6a77e5185e9f40b2d");
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertNotNull(tunnel);
        assertEquals("running", tunnel.getStatus());
        assertEquals(Arrays.asList("google.com", "yahoo.com"), tunnel.getDirectDomains());
        assertEquals(null, tunnel.getVmVersion());
        assertEquals(1448859728000L, tunnel.getLastConnected().getTime());
        assertEquals(null, tunnel.getShutdownTime());
        assertEquals(443, tunnel.getSshPort());
        assertEquals(1448859722000L, tunnel.getLaunchTime().getTime());
        assertEquals(null, tunnel.getUserShutdown());
        assertEquals(null, tunnel.getUseCachingProxy());
        assertEquals(1448859714000L, tunnel.getCreationTime().getTime());
        assertEquals(Arrays.asList("notgoogle.com", "notyahoo.com"), tunnel.getDomainNames());
        assertEquals(false, tunnel.getSharedTunnel());
        assertEquals("gavin-tunnel-id", tunnel.getTunnelIdentifier());
        assertEquals("maki81017.miso.saucelabs.com", tunnel.getHost());
        assertEquals(false, tunnel.getNoProxyCaching());
        assertEquals("halkeye", tunnel.getOwner());
        assertEquals(true, tunnel.getUseKgp());
        assertEquals(null, tunnel.getNoSslBumpDomains());
        assertEquals("0ec525b62b4e47a6a77e5185e9f40b2d", tunnel.getId());

        assertEquals("odin", tunnel.getMetadata().getHostname());
        assertEquals("abd6986", tunnel.getMetadata().getGitVersion());
        assertEquals("Linux 3.10.0-229.14.1.el7.x86_64 #1 SMP Tue Sep 15 15:05:51 UTC 2015 x86_64", tunnel.getMetadata().getPlatform());
        assertEquals("./bin/sc -u halkeye -k **** -D google.com,yahoo.com -t notgoogle.com,notyahoo.com -i gavin-tunnel-id", tunnel.getMetadata().getCommand());
        assertEquals(1788, tunnel.getMetadata().getBuild());
        assertEquals("4.3.12", tunnel.getMetadata().getRelease());
        assertEquals(1024, tunnel.getMetadata().getNofileLimit());
    }

    public void testGetActivity() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(getClass().getResource("/activity.json").openStream());

        Activity activity = sauceREST.getActivity();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/" + this.sauceREST.getUsername() + "/activity");
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertEquals(0, activity.getTotals().getAll());
        assertEquals(0, activity.getTotals().getInProgress());
        assertEquals(0, activity.getTotals().getQueued());

        assertEquals(0, activity.getSubaccount("halkeye").getAll());
        assertEquals(0, activity.getSubaccount("halkeye").getInProgress());
        assertEquals(0, activity.getSubaccount("halkeye").getQueued());

        assertNull(activity.getSubaccount("no real account"));

        assertEquals(new HashSet<String>(Arrays.asList("halkeye", "gavin_sauce_1")), activity.getSubaccounts());

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

    public void testGetConcurrency() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(getClass().getResource("/users_halkeye_concurrency.json").openStream());

        Concurrency concurencyInfo = sauceREST.getConcurrency();
        assertEquals(this.urlConnection.getRealURL().getPath(), "/rest/v1/users/" + this.sauceREST.getUsername() + "/concurrency");
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertEquals(1448857070346L, concurencyInfo.getTimestamp().getTime());

        assertEquals(0, concurencyInfo.getSubaccount("halkeye").getCurrent().getMac());
        assertEquals(0, concurencyInfo.getSubaccount("halkeye").getCurrent().getManual());
        assertEquals(0, concurencyInfo.getSubaccount("halkeye").getCurrent().getOverall());

        assertEquals(100, concurencyInfo.getSubaccount("halkeye").getRemaining().getMac());
        assertEquals(5, concurencyInfo.getSubaccount("halkeye").getRemaining().getManual());
        assertEquals(100, concurencyInfo.getSubaccount("halkeye").getRemaining().getOverall());

        assertEquals(1, concurencyInfo.getSubaccount("gavin_sauce_1").getRemaining().getOverall());

        assertNull(concurencyInfo.getSubaccount("no real account"));

        assertEquals(new HashSet<String>(Arrays.asList("halkeye", "gavin_sauce_1")), concurencyInfo.getSubaccounts());
    }

    public void testStopJob() throws Exception {
        urlConnection.setResponseCode(200);
        urlConnection.setInputStream(new ByteArrayInputStream("".getBytes("UTF-8")));

        sauceREST.stopJob("0ec525b62b4e47a6a77e5185e9f40b2d");
        assertEquals(
            "/rest/v1/" + this.sauceREST.getUsername() + "/jobs/0ec525b62b4e47a6a77e5185e9f40b2d/stop",
            this.urlConnection.getRealURL().getPath()
        );
        assertNull(this.urlConnection.getRealURL().getQuery());
        assertEquals("PUT", this.urlConnection.getRealMethod());
    }
}