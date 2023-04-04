package com.saucelabs.saucerest;

import com.squareup.moshi.Moshi;

public class MoshiSingleton {

    private static Moshi moshi;

    private MoshiSingleton() {
    }

    public static synchronized Moshi getInstance() {
        if (moshi == null) {
            moshi = new Moshi.Builder().build();
        }
        return moshi;
    }
}