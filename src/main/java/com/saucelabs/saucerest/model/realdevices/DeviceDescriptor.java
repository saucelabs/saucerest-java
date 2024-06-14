package com.saucelabs.saucerest.model.realdevices;

import java.util.List;

public class DeviceDescriptor {

    public String abiType;
    public Integer apiLevel;
    public Integer cpuCores;
    public Integer cpuFrequency;
    public String defaultOrientation;
    public Integer dpi;
    public Boolean hasOnScreenButtons;
    public String id;
    public String internalOrientation;
    public Integer internalStorageSize;
    public Boolean isArm;
    public Boolean isKeyGuardDisabled;
    public Boolean isPrivate;
    public Boolean isRooted;
    public Boolean isTablet;
    public List<String> manufacturer = null;
    public String modelNumber;
    public String name;
    public String os;
    public String osVersion;
    public Integer pixelsPerPoint;
    public Integer ramSize;
    public Integer resolutionHeight;
    public Integer resolutionWidth;
    public Float screenSize;
    public Integer sdCardSize;
    public Boolean supportsAppiumWebAppTesting;
    public Boolean supportsGlobalProxy;
    public Boolean supportsMinicapSocketConnection;
    public Boolean supportsMockLocations;
    public String cpuType;
    public String deviceFamily;
    public String dpiName;
    public Boolean isAlternativeIoEnabled;
    public Boolean supportsManualWebTesting;
    public Boolean supportsMultiTouch;
    public Boolean supportsXcuiTest;

    public DeviceDescriptor() {
    }

    public DeviceDescriptor(String abiType, Integer apiLevel, Integer cpuCores, Integer cpuFrequency, String defaultOrientation, Integer dpi, Boolean hasOnScreenButtons, String id, String internalOrientation, Integer internalStorageSize, Boolean isArm, Boolean isKeyGuardDisabled, Boolean isPrivate, Boolean isRooted, Boolean isTablet, List<String> manufacturer, String modelNumber, String name, String os, String osVersion, Integer pixelsPerPoint, Integer ramSize, Integer resolutionHeight, Integer resolutionWidth, Float screenSize, Integer sdCardSize, Boolean supportsAppiumWebAppTesting, Boolean supportsGlobalProxy, Boolean supportsMinicapSocketConnection, Boolean supportsMockLocations, String cpuType, String deviceFamily, String dpiName, Boolean isAlternativeIoEnabled, Boolean supportsManualWebTesting, Boolean supportsMultiTouch, Boolean supportsXcuiTest) {
        super();
        this.abiType = abiType;
        this.apiLevel = apiLevel;
        this.cpuCores = cpuCores;
        this.cpuFrequency = cpuFrequency;
        this.defaultOrientation = defaultOrientation;
        this.dpi = dpi;
        this.hasOnScreenButtons = hasOnScreenButtons;
        this.id = id;
        this.internalOrientation = internalOrientation;
        this.internalStorageSize = internalStorageSize;
        this.isArm = isArm;
        this.isKeyGuardDisabled = isKeyGuardDisabled;
        this.isPrivate = isPrivate;
        this.isRooted = isRooted;
        this.isTablet = isTablet;
        this.manufacturer = manufacturer;
        this.modelNumber = modelNumber;
        this.name = name;
        this.os = os;
        this.osVersion = osVersion;
        this.pixelsPerPoint = pixelsPerPoint;
        this.ramSize = ramSize;
        this.resolutionHeight = resolutionHeight;
        this.resolutionWidth = resolutionWidth;
        this.screenSize = screenSize;
        this.sdCardSize = sdCardSize;
        this.supportsAppiumWebAppTesting = supportsAppiumWebAppTesting;
        this.supportsGlobalProxy = supportsGlobalProxy;
        this.supportsMinicapSocketConnection = supportsMinicapSocketConnection;
        this.supportsMockLocations = supportsMockLocations;
        this.cpuType = cpuType;
        this.deviceFamily = deviceFamily;
        this.dpiName = dpiName;
        this.isAlternativeIoEnabled = isAlternativeIoEnabled;
        this.supportsManualWebTesting = supportsManualWebTesting;
        this.supportsMultiTouch = supportsMultiTouch;
        this.supportsXcuiTest = supportsXcuiTest;
    }
}