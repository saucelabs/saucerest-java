package com.saucelabs.saucerest.model.realdevices;

import com.squareup.moshi.Json;

import java.util.List;

public class DeviceDescriptor {

    @Json(name = "abiType")
    public String abiType;
    @Json(name = "apiLevel")
    public Integer apiLevel;
    @Json(name = "cpuCores")
    public Integer cpuCores;
    @Json(name = "cpuFrequency")
    public Integer cpuFrequency;
    @Json(name = "defaultOrientation")
    public String defaultOrientation;
    @Json(name = "dpi")
    public Integer dpi;
    @Json(name = "hasOnScreenButtons")
    public Boolean hasOnScreenButtons;
    @Json(name = "id")
    public String id;
    @Json(name = "internalOrientation")
    public String internalOrientation;
    @Json(name = "internalStorageSize")
    public Integer internalStorageSize;
    @Json(name = "isArm")
    public Boolean isArm;
    @Json(name = "isKeyGuardDisabled")
    public Boolean isKeyGuardDisabled;
    @Json(name = "isPrivate")
    public Boolean isPrivate;
    @Json(name = "isRooted")
    public Boolean isRooted;
    @Json(name = "isTablet")
    public Boolean isTablet;
    @Json(name = "manufacturer")
    public List<String> manufacturer = null;
    @Json(name = "modelNumber")
    public String modelNumber;
    @Json(name = "name")
    public String name;
    @Json(name = "os")
    public String os;
    @Json(name = "osVersion")
    public String osVersion;
    @Json(name = "pixelsPerPoint")
    public Integer pixelsPerPoint;
    @Json(name = "ramSize")
    public Integer ramSize;
    @Json(name = "resolutionHeight")
    public Integer resolutionHeight;
    @Json(name = "resolutionWidth")
    public Integer resolutionWidth;
    @Json(name = "screenSize")
    public Float screenSize;
    @Json(name = "sdCardSize")
    public Integer sdCardSize;
    @Json(name = "supportsAppiumWebAppTesting")
    public Boolean supportsAppiumWebAppTesting;
    @Json(name = "supportsGlobalProxy")
    public Boolean supportsGlobalProxy;
    @Json(name = "supportsMinicapSocketConnection")
    public Boolean supportsMinicapSocketConnection;
    @Json(name = "supportsMockLocations")
    public Boolean supportsMockLocations;
    @Json(name = "cpuType")
    public String cpuType;
    @Json(name = "deviceFamily")
    public String deviceFamily;
    @Json(name = "dpiName")
    public String dpiName;
    @Json(name = "isAlternativeIoEnabled")
    public Boolean isAlternativeIoEnabled;
    @Json(name = "supportsManualWebTesting")
    public Boolean supportsManualWebTesting;
    @Json(name = "supportsMultiTouch")
    public Boolean supportsMultiTouch;
    @Json(name = "supportsXcuiTest")
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