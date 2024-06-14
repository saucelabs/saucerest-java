package com.saucelabs.saucerest;

import com.google.gson.annotations.SerializedName;

public enum JobVisibility {
    @SerializedName("public")
    PUBLIC,
    @SerializedName("public restricted")
    PUBLIC_RESTRICTED,
    @SerializedName("share")
    SHARE,
    @SerializedName("team")
    TEAM,
    @SerializedName("private")
    PRIVATE
}
