package com.saucelabs.saucerest.model;

import com.saucelabs.saucerest.MoshiSingleton;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

public abstract class AbstractModel {

    /**
     * Transform a model class into JSON.
     *
     * @return JSON string
     */
    public <T> String toJson() {
        Moshi moshi = MoshiSingleton.getInstance();
        JsonAdapter<T> jsonAdapter = (JsonAdapter<T>) moshi.adapter(this.getClass()).nonNull();
        return jsonAdapter.toJson((T) this);
    }
}