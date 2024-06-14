package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.*;

import com.saucelabs.saucerest.SauceException;
import com.saucelabs.saucerest.model.storage.*;
import org.junit.jupiter.api.Test;

class EditAppGroupSettingsTest {

    @Test
    public void testBuilder() {
        Settings settings = new Settings.Builder()
            .setAudioCapture(true)
            .setSetupDeviceLock(true)
            .setInstrumentation(new Instrumentation.Builder().setImageInjection(true).build())
            .build();

        EditAppGroupSettings editAppGroupSettings = new EditAppGroupSettings.Builder(EditAppGroupSettings.Builder.Platform.ANDROID)
            .setSettings(settings)
            .build();

        assertNotNull(editAppGroupSettings);
        assertNotNull(editAppGroupSettings.settings);
        assertAll(
            () -> assertTrue(editAppGroupSettings.settings.audioCapture),
            () -> assertTrue(editAppGroupSettings.settings.setupDeviceLock),
            () -> assertTrue(editAppGroupSettings.settings.instrumentation.imageInjection)
        );
    }

    @Test
    public void testBuilderWithProxy() {
        Settings settings = new Settings.Builder()
                .setProxy(new Proxy.Builder().setHost("local").setPort(1234).build())
                .build();

        EditAppGroupSettings editAppGroupSettings = new EditAppGroupSettings.Builder(EditAppGroupSettings.Builder.Platform.ANDROID)
                .setSettings(settings)
                .build();

        assertEquals("local", editAppGroupSettings.settings.proxy.host);
        assertEquals(1234, editAppGroupSettings.settings.proxy.port);
        assertTrue(editAppGroupSettings.settings.proxyEnabled);
    }

    @Test
    public void testBuilderResigningException() {
        Settings settings = new Settings.Builder()
            .setResigning(new Resigning.Builder().setBiometrics(true).build())
            .build();

        assertThrows(SauceException.ResigningNotAllowed.class, () -> new EditAppGroupSettings.Builder(EditAppGroupSettings.Builder.Platform.ANDROID)
                .setSettings(settings)
                .build());
    }

    @Test
    public void testBuilderDeviceLockException() {
        Settings settings = new Settings.Builder()
            .setSetupDeviceLock(true)
            .build();

        assertThrows(SauceException.DeviceLockOnlyOnAndroid.class, () -> new EditAppGroupSettings.Builder(EditAppGroupSettings.Builder.Platform.IOS)
                .setSettings(settings)
                .build());
    }

    @Test
    public void testBuilderOrientation_Portrait() {
        Settings settings = new Settings.Builder()
            .setOrientation("PORTRAIT")
            .build();

        EditAppGroupSettings editAppGroupSettings = new EditAppGroupSettings.Builder(EditAppGroupSettings.Builder.Platform.ANDROID)
            .setSettings(settings)
            .build();

        assertEquals("PORTRAIT", editAppGroupSettings.settings.orientation);
    }

    @Test
    public void testBuilderOrientation_Landscape() {
        Settings settings = new Settings.Builder()
            .setOrientation("LANDSCAPE")
            .build();

        EditAppGroupSettings editAppGroupSettings = new EditAppGroupSettings.Builder(EditAppGroupSettings.Builder.Platform.ANDROID)
            .setSettings(settings)
            .build();

        assertEquals("LANDSCAPE", editAppGroupSettings.settings.orientation);
    }

    @Test
    public void testBuilderInstrumentationException() {
        Settings settings = new Settings.Builder()
            .setInstrumentation(new Instrumentation.Builder().setBiometrics(true).build())
            .build();

        assertThrows(SauceException.InstrumentationNotAllowed.class, () -> new EditAppGroupSettings.Builder(EditAppGroupSettings.Builder.Platform.IOS)
                .setSettings(settings)
                .build());
    }

    @Test
    public void testBuilderAddingRequiredFlag() {
        Settings settings = new Settings.Builder()
                .setInstrumentation(new Instrumentation.Builder().setImageInjection(true).build())
                .build();

        EditAppGroupSettings editAppGroupSettings = new EditAppGroupSettings.Builder(EditAppGroupSettings.Builder.Platform.ANDROID)
                .setSettings(settings)
                .build();

        assertTrue(editAppGroupSettings.settings.instrumentationEnabled);
        assertTrue(editAppGroupSettings.settings.instrumentation.imageInjection);
        assertNull(editAppGroupSettings.settings.instrumentation.biometrics);
    }
}