package com.saucelabs.saucerest.model.platform;

import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;

public class EndOfLifeAppiumVersions {

    public List<AppiumVersion> appiumVersionList;

    public EndOfLifeAppiumVersions(String jsonResponseBody) {
        super();
        JSONObject eolAppiumVersions = new JSONObject(jsonResponseBody);

        appiumVersionList = eolAppiumVersions.toMap().entrySet().stream().map(entry -> new AppiumVersion(entry.getKey(), (Integer) entry.getValue())).collect(Collectors.toList());
    }
}
