package com.saucelabs.saucerest.unit;

import com.saucelabs.saucerest.SauceException;
import com.saucelabs.saucerest.model.storage.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EditAppGroupSettingsTest {

    @Test
    void toJson() {
    }

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

        Assertions.assertNotNull(editAppGroupSettings);
        System.out.println(editAppGroupSettings.toJson());
    }

    @Test
    public void testBuilderWithProxy() {
        Settings settings = new Settings.Builder()
            .setProxy(new Proxy.Builder().setHost("local").setPort(1234).build())
            .build();

        EditAppGroupSettings editAppGroupSettings = new EditAppGroupSettings.Builder(EditAppGroupSettings.Builder.Platform.ANDROID)
            .setSettings(settings)
            .build();

        Assertions.assertEquals("local", editAppGroupSettings.settings.proxy.host);
        Assertions.assertEquals(1234, editAppGroupSettings.settings.proxy.port);
        Assertions.assertTrue(editAppGroupSettings.settings.proxyEnabled);
    }

    @Test
    public void testBuilderResigningException() {
        Settings settings = new Settings.Builder()
            .setResigning(new Resigning.Builder().setBiometrics(true).build())
            .build();

        Assertions.assertThrows(SauceException.ResigningNotAllowed.class, () -> new EditAppGroupSettings.Builder(EditAppGroupSettings.Builder.Platform.ANDROID)
            .setSettings(settings)
            .build());
    }

    @Test
    public void testBuilderDeviceLockException() {
        Settings settings = new Settings.Builder()
            .setSetupDeviceLock(true)
            .build();

        Assertions.assertThrows(SauceException.DeviceLockOnlyOnAndroid.class, () -> new EditAppGroupSettings.Builder(EditAppGroupSettings.Builder.Platform.IOS)
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

        Assertions.assertEquals("PORTRAIT", editAppGroupSettings.settings.orientation);
    }

    @Test
    public void testBuilderOrientation_Landscape() {
        Settings settings = new Settings.Builder()
            .setOrientation("LANDSCAPE")
            .build();

        EditAppGroupSettings editAppGroupSettings = new EditAppGroupSettings.Builder(EditAppGroupSettings.Builder.Platform.ANDROID)
            .setSettings(settings)
            .build();

        Assertions.assertEquals("LANDSCAPE", editAppGroupSettings.settings.orientation);
    }

    @Test
    public void testBuilderInstrumentationException() {
        Settings settings = new Settings.Builder()
            .setInstrumentation(new Instrumentation.Builder().setBiometrics(true).build())
            .build();

        Assertions.assertThrows(SauceException.InstrumentationNotAllowed.class, () -> new EditAppGroupSettings.Builder(EditAppGroupSettings.Builder.Platform.IOS)
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

        Assertions.assertTrue(editAppGroupSettings.settings.instrumentationEnabled);
        Assertions.assertTrue(editAppGroupSettings.settings.instrumentation.imageInjection);
        Assertions.assertNull(editAppGroupSettings.settings.instrumentation.biometrics);
        System.out.println(editAppGroupSettings.toJson());
    }

    @Test
    public void testBuilderToJson() {
        Settings settings = new Settings.Builder()
            .setAudioCapture(true)
            .build();

        EditAppGroupSettings editAppGroupSettings = new EditAppGroupSettings.Builder(EditAppGroupSettings.Builder.Platform.ANDROID)
            .setSettings(settings)
            .build();


        System.out.println(editAppGroupSettings.toJson());
    }
}