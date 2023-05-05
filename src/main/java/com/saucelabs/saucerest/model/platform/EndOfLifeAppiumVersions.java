package com.saucelabs.saucerest.model.platform;

import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class EndOfLifeAppiumVersions {

    public List<AppiumVersion> getAppiumVersionList() {
        return appiumVersionList;
    }

    private final List<AppiumVersion> appiumVersionList;

    public EndOfLifeAppiumVersions(Response response) throws IOException {
        super();
        JSONObject eolAppiumVersions = new JSONObject(response.body().string());

        appiumVersionList = eolAppiumVersions.toMap().entrySet().stream().map(entry -> new AppiumVersion(entry.getKey(), (Integer) entry.getValue())).collect(Collectors.toList());
    }
}