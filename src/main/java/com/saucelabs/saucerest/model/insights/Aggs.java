package com.saucelabs.saucerest.model.insights;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Aggs {
    public List<NameCount> browser;

    @SerializedName("browserError")
    public List<NameCount> browserError;

    @SerializedName("browserFail")
    public List<NameCount> browserFail;

    public List<NameCount> device;

    @SerializedName("deviceError")
    public List<NameCount> deviceError;

    @SerializedName("deviceFail")
    public List<NameCount> deviceFail;

    @SerializedName("errorMessage")
    public List<NameCount> errorMessage;

    public List<NameCount> framework;

    @SerializedName("frameworkError")
    public List<NameCount> frameworkError;

    @SerializedName("frameworkFail")
    public List<NameCount> frameworkFail;

    public List<NameCount> os;

    @SerializedName("osError")
    public List<NameCount> osError;

    @SerializedName("osFail")
    public List<NameCount> osFail;

    public List<NameCount> owner;
    public List<NameCount> status;
}
