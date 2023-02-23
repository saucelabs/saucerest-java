package com.saucelabs.saucerest.integration;

import java.io.File;
import java.util.Objects;

public class StorageTestHelper {
    public File getAppFile(AppFile appFile) {
        return new File(Objects.requireNonNull(getClass().getResource("/AppFiles/" + appFile.fileName)).getFile());
    }

    enum AppFile {
        IPA("iOS-Real-Device-MyRNDemoApp.ipa"),
        ZIP("iOS-Simulator-MyRNDemoApp.zip"),
        APK("Android-MyDemoAppRN.apk"),
        IPA_NATIVE("iOS-Real-Device-MyNativeDemoApp.ipa"),
        APK_NATIVE("Android-MyDemoAppNative.apk");

        public final String fileName;

        AppFile(String fileName) {
            this.fileName = fileName;
        }
    }
}