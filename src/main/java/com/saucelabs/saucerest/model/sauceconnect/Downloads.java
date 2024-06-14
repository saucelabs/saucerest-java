package com.saucelabs.saucerest.model.sauceconnect;

import com.google.gson.annotations.SerializedName;

public class Downloads {

    public Linux linux;
    @SerializedName("linux-arm64")
    public LinuxArm64 linuxArm64;
    public Osx osx;
    public Win32 win32;

    /**
     * No args constructor for use in serialization
     */
    public Downloads() {
    }

    /**
     * @param linuxArm64
     * @param osx
     * @param win32
     * @param linux
     */
    public Downloads(Linux linux, LinuxArm64 linuxArm64, Osx osx, Win32 win32) {
        super();
        this.linux = linux;
        this.linuxArm64 = linuxArm64;
        this.osx = osx;
        this.win32 = win32;
    }
}
