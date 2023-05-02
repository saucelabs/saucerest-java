package com.saucelabs.saucerest.unit;

import com.saucelabs.saucerest.model.storage.StorageParameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class StorageEndpointParameterTest {

    @Test
    public void pageNotSet() {
        StorageParameter storageParameter = new StorageParameter.Builder()
                .setQ("bla")
                .build();

        Assertions.assertNull(storageParameter.toMap().get("page"));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void pageDefaultToNull(int page) {
        StorageParameter storageParameter = new StorageParameter.Builder()
            .setQ("bla")
            .setPage(page)
            .build();

        Assertions.assertNull(storageParameter.toMap().get("page"));
    }

    @Test
    public void pageSet() {
        StorageParameter storageParameter = new StorageParameter.Builder()
            .setQ("bla")
            .setPage(5)
            .build();

        Assertions.assertEquals(5, storageParameter.toMap().get("page"));
    }
}