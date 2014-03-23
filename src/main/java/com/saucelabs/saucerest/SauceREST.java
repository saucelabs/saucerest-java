package com.saucelabs.saucerest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONValue;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.UnexpectedException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple Java API that invokes the Sauce REST API.
 */
public class SauceREST {

    private static final Logger logger = Logger.getLogger(SauceREST.class.getName());
    private static final long HTTP_READ_TIMEOUT_SECONDS = TimeUnit.SECONDS.toMillis(10);
    private static final long HTTP_CONNECT_TIMEOUT_SECONDS = TimeUnit.SECONDS.toMillis(10);


    protected String username;
    protected String accessKey;

    public static final String RESTURL = "https://saucelabs.com/rest/v1/%1$s";
    private static final String USER_RESULT_FORMAT = RESTURL + "/%2$s";
    private static final String JOB_RESULT_FORMAT = RESTURL + "/jobs/%2$s";
    private static final String STOP_JOB_FORMAT = JOB_RESULT_FORMAT + "/stop";
    private static final String DOWNLOAD_VIDEO_FORMAT = JOB_RESULT_FORMAT + "/results/video.flv";
    private static final String DOWNLOAD_LOG_FORMAT = JOB_RESULT_FORMAT + "/results/video.flv";
    private static final String DATE_FORMAT = "yyyyMMdd_HHmmSS";

    public SauceREST(String username, String accessKey) {
        this.username = username;
        this.accessKey = accessKey;
    }

    /**
     * Marks a Sauce Job as 'passed'.
     *
     * @param jobId the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
     * @throws IOException thrown if an error occurs invoking the REST request
     */
    public void jobPassed(String jobId) {
        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("passed", true);
        updateJobInfo(jobId, updates);
    }

    /**
     * Marks a Sauce Job as 'failed'.
     *
     * @param jobId the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
     * @throws IOException thrown if an error occurs invoking the REST request
     */
    public void jobFailed(String jobId) {
        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("passed", false);
        updateJobInfo(jobId, updates);
    }

    /**
     * Downloads the video for a Sauce Job to the filesystem.  The file will be stored in
     * a directory specified by the <code>location</code> field.
     *
     * @param jobId    the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
     * @param location
     * @throws IOException thrown if an error occurs invoking the REST request
     */
    public void downloadVideo(String jobId, String location) {
        URL restEndpoint = null;
        try {
            restEndpoint = new URL(String.format(DOWNLOAD_VIDEO_FORMAT, username, jobId));
        } catch (MalformedURLException e) {
            logger.log(Level.WARNING, "Error constructing Sauce URL", e);
        }
        downloadFile(jobId, location, restEndpoint);
    }

    /**
     * Downloads the log file for a Sauce Job to the filesystem.  The file will be stored in
     * a directory specified by the <code>location</code> field.
     *
     * @param jobId    the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
     * @param location
     * @throws IOException thrown if an error occurs invoking the REST request
     */
    public void downloadLog(String jobId, String location) {
        URL restEndpoint = null;
        try {
            restEndpoint = new URL(String.format(DOWNLOAD_LOG_FORMAT, username, jobId));
        } catch (MalformedURLException e) {
            logger.log(Level.WARNING, "Error constructing Sauce URL", e);
        }
        downloadFile(jobId, location, restEndpoint);
    }

    public String retrieveResults(String path) {
        URL restEndpoint = null;
        try {
            restEndpoint = new URL(String.format(USER_RESULT_FORMAT, username, path));
        } catch (MalformedURLException e) {
            logger.log(Level.WARNING, "Error constructing Sauce URL", e);
        }
        return retrieveResults(restEndpoint);
    }

    public String getJobInfo(String jobId) {
        URL restEndpoint = null;
        try {
            restEndpoint = new URL(String.format(JOB_RESULT_FORMAT, username, jobId));
        } catch (MalformedURLException e) {
            logger.log(Level.WARNING, "Error constructing Sauce URL", e);
        }
        return retrieveResults(restEndpoint);
    }

    public String retrieveResults(URL restEndpoint) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            HttpURLConnection connection = openConnection(restEndpoint);

            connection.setDoOutput(true);

            addAuthenticationProperty(connection);

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                builder.append(inputLine);
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error retrieving Sauce Results", e);
        }
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error closing Sauce input stream", e);
        }
        return builder.toString();
    }

    private void downloadFile(String jobId, String location, URL restEndpoint) {
        try {
            HttpURLConnection connection = openConnection(restEndpoint);

            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            addAuthenticationProperty(connection);

            InputStream stream = connection.getInputStream();
            BufferedInputStream in = new BufferedInputStream(stream);
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            String saveName = jobId + format.format(new Date());
            if (restEndpoint.getPath().endsWith(".flv")) {
                saveName = saveName + ".flv";
            } else {
                saveName = saveName + ".log";
            }
            FileOutputStream file = new FileOutputStream(new File(location, saveName));
            BufferedOutputStream out = new BufferedOutputStream(file);
            int i;
            while ((i = in.read()) != -1) {
                out.write(i);
            }
            out.flush();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error downloading Sauce Results");
        }
    }

    private void addAuthenticationProperty(HttpURLConnection connection) {
        if (username != null && accessKey != null) {
            String auth = encodeAuthentication();
            connection.setRequestProperty("Authorization", auth);
        }
    }

    public void updateJobInfo(String jobId, Map<String, Object> updates) {
        HttpURLConnection postBack = null;
        try {
            URL restEndpoint = new URL(String.format(JOB_RESULT_FORMAT, username, jobId));
            postBack = openConnection(restEndpoint);
            postBack.setDoOutput(true);
            postBack.setRequestMethod("PUT");
            addAuthenticationProperty(postBack);
            String jsonText = JSONValue.toJSONString(updates);
            postBack.getOutputStream().write(jsonText.getBytes());
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error updating Sauce Results", e);
        }

        try {
            if (postBack != null) {
                postBack.getInputStream().close();
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error closing result stream", e);
        }

    }

    public void stopJob(String jobId) {
        HttpURLConnection postBack = null;

        try {
            URL restEndpoint = new URL(String.format(STOP_JOB_FORMAT, username, jobId));
            postBack = openConnection(restEndpoint);
            postBack.setDoOutput(true);
            postBack.setRequestMethod("PUT");
            addAuthenticationProperty(postBack);
            postBack.getOutputStream().write("".getBytes());
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error stopping Sauce Job", e);
        }

        try {
            if (postBack != null) {
                postBack.getInputStream().close();
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error closing result stream", e);
        }

    }

    public HttpURLConnection openConnection(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setReadTimeout((int) HTTP_READ_TIMEOUT_SECONDS);
        con.setConnectTimeout((int) HTTP_CONNECT_TIMEOUT_SECONDS);
        return con;
    }

    /**
     * Uploads a file to Sauce storage.
     *
     * @param file the file to upload
     *             -param fileName uses file.getName() to store in sauce
     *             -param overwrite set to true
     * @return the md5 hash returned by sauce of the file
     * @throws IOException
     */
    public String uploadFile(File file) throws IOException {
        return uploadFile(file, file.getName());
    }

    /**
     * Uploads a file to Sauce storage.
     *
     * @param file     the file to upload
     * @param fileName name of the file in sauce storage
     *                 -param overwrite set to true
     * @return the md5 hash returned by sauce of the file
     * @throws IOException
     */
    public String uploadFile(File file, String fileName) throws IOException {
        return uploadFile(file, fileName, true);
    }

    /**
     * Uploads a file to Sauce storage.
     *
     * @param file      the file to upload
     * @param fileName  name of the file in sauce storage
     * @param overwrite boolean flag to overwrite file in sauce storage if it exists
     * @return the md5 hash returned by sauce of the file
     * @throws IOException
     */
    public String uploadFile(File file, String fileName, Boolean overwrite) throws IOException {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://saucelabs.com/rest/v1/storage/" +
                username + "/" + fileName + "?overwrite=" + overwrite.toString());
        FileEntity entity = new FileEntity(file);
        entity.setContentType(new BasicHeader("Content-Type",
                "application/octet-stream"));
        post.setEntity(entity);

        post.setHeader("Content-Type", "application/octet-stream");
        post.setHeader("Authorization", encodeAuthentication());
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            builder.append(line);
        }

        try {
            JSONObject sauceUploadResponse = new JSONObject(builder.toString());
            if (sauceUploadResponse.has("error")) {
                throw new UnexpectedException("Failed to upload to sauce-storage: "
                        + sauceUploadResponse.getString("error"));
            }
            return sauceUploadResponse.getString("md5");
        } catch (JSONException j) {
            throw new UnexpectedException("Failed to parse json response.", j);
        }

    }

    /**
     * Generates a link to the job page on Saucelabs.com, which can be accessed
     * without the user's credentials. Auth token is HMAC/MD5 of the job ID
     * with the key <username>:<api key>
     * (see {@link http://saucelabs.com/docs/integration#public-job-links}).
     *
     * @param jobId the Sauce Job Id, typically equal to the Selenium/WebDriver sessionId
     * @return link to the job page with authorization token
     * @throws ;
     */
    public String getPublicJobLink(String jobId) {
        try {
            String key = username + ":" + accessKey;
            String auth_token = SecurityUtils.hmacEncode("HmacMD5", jobId, key);
            String link = "https://saucelabs.com/jobs/" + jobId + "?auth=" + auth_token;

            return link;
        } catch (IllegalArgumentException ex) {
            // someone messed up on the algorithm to hmacEncode
            // For available algorithms see {@link http://docs.oracle.com/javase/7/docs/api/javax/crypto/Mac.html}
            // we only want to use 'HmacMD5'
            System.err.println("Unable to create an authenticated public link to job:");
            System.err.println(ex);
            return "";
        }
    }

    private String encodeAuthentication() {
        String auth = username + ":" + accessKey;
        //Handle long strings encoded using BASE64Encoder - see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6947917
        BASE64Encoder encoder = new BASE64Encoder() {
            @Override
            protected int bytesPerLine() {
                return 9999;
            }
        };
        auth = "Basic " + new String(encoder.encode(auth.getBytes()));
        return auth;
    }
}
