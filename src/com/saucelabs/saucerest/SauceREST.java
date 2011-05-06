package com.saucelabs.saucerest;

import java.io.*;
import java.net.*;
import java.util.*;
import org.json.simple.*;
import sun.misc.BASE64Encoder;



public class SauceREST {
    protected String username;
    protected String accessKey;

    public static final String RESTURL = "http://saucelabs.com/rest";

    public SauceREST(String username, String accessKey) {
        this.username = username;
        this.accessKey = accessKey;
    }

    public void jobPassed(String jobId) throws IOException {
        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("passed", true);
        updateJobInfo(jobId, updates);
    }

    public void jobFailed(String jobId) throws IOException {
        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("passed", false);
        updateJobInfo(jobId, updates);
    }

    public void updateJobInfo(String jobId, Map<String,Object> updates) throws IOException {
        URL restEndpoint = new URL(RESTURL+"/v1/"+username+"/jobs/"+jobId);
        String auth = username + ":" + accessKey;
        BASE64Encoder encoder = new BASE64Encoder();
        auth = "Basic " + new String(encoder.encode(auth.getBytes()));
        
        HttpURLConnection postBack = (HttpURLConnection) restEndpoint.openConnection();
        postBack.setDoOutput(true);
        postBack.setRequestMethod("PUT");
        postBack.setRequestProperty("Authorization", auth);
        String jsonText = JSONValue.toJSONString(updates);
        postBack.getOutputStream().write(jsonText.getBytes());
        postBack.getInputStream().close();
    }
}
