package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.*;

import com.saucelabs.saucerest.TestAsset;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class TestAssetTest {
    @Test
    void testAllEnumValues() {
        Arrays.stream(TestAsset.values()).forEach(asset -> {
            String label = asset.label;
            Optional<TestAsset> optionalAsset = TestAsset.get(label);
            assertAll(
                () -> assertTrue(optionalAsset.isPresent(), String.format("Asset %s not found", label)),
                () -> assertEquals(asset, optionalAsset.get(), String.format("Expected asset %s but found %s", asset, optionalAsset.get()))
            );
        });
    }

    @Test
    public void testGetExistingTestAsset() {
        Optional<TestAsset> asset = TestAsset.get("log.json");
        assertTrue(asset.isPresent());
        assertEquals(TestAsset.SAUCE_LOG, asset.get());
    }

    @Test
    public void testGetNonExistingTestAsset() {
        Optional<TestAsset> asset = TestAsset.get("non-existing-asset");
        assertFalse(asset.isPresent());
    }
}