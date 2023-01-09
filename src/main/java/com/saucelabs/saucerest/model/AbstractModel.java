package com.saucelabs.saucerest.model;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

public abstract class AbstractModel {

    /**
     * Transform a model class into JSON.
     *
     * @return
     */
    public <T> String toJson() {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<T> jsonAdapter = (JsonAdapter<T>) moshi.adapter(this.getClass()).nonNull();
        return jsonAdapter.toJson((T) this);
    }
}
