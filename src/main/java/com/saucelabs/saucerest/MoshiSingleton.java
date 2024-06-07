package com.saucelabs.saucerest;

import java.io.IOException;
import java.math.BigInteger;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Moshi.Builder;

public class MoshiSingleton {

    private static Moshi moshi;

    private MoshiSingleton() {
    }

    public static synchronized Moshi getInstance() {
        if (moshi == null) {
            moshi = new Builder()
                .add(BigInteger.class, new JsonAdapter<BigInteger>()
                {
                    @Override
                    public BigInteger fromJson(JsonReader jsonReader) throws IOException
                    {
                        return new BigInteger(jsonReader.nextString());
                    }

                    @Override
                    public void toJson(JsonWriter jsonWriter, BigInteger bigInteger) throws IOException
                    {
                        jsonWriter.value(bigInteger.toString());
                    }
                })
                .build();
        }
        return moshi;
    }
}