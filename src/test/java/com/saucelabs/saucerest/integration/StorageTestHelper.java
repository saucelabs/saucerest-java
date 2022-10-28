package com.saucelabs.saucerest.integration;

import java.io.File;
import java.util.Objects;

public class StorageTestHelper {
    enum AppFile {
        IPA("iOS-Real-Device-MyRNDemoApp.ipa"),
        ZIP("iOS-Simulator-MyRNDemoApp.zip"),
        APK("Android-MyDemoAppRN.apk");

        public final String fileName;

        AppFile(String fileName) {
            this.fileName = fileName;
        }
    }

    public File getAppFile(AppFile appFile) {
        return new File(Objects.requireNonNull(getClass().getResource("/AppFiles/" + appFile.fileName)).getFile());
    }
}
