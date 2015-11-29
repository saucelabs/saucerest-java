package com.saucelabs.saucerest.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

// http://json2java.azurewebsites.net/
public class ConcurrencyLimit implements Serializable {
    @JsonProperty("mac")
    private int mac;

    public int getMac() { return this.mac; }

    public void setMac(int mac) { this.mac = mac; }

    @JsonProperty("scout")
    private int scout;

    public int getScout() { return this.scout; }

    public void setScout(int scout) { this.scout = scout; }

    @JsonProperty("overall")
    private int overall;

    public int getOverall() { return this.overall; }

    public void setOverall(int overall) { this.overall = overall; }

    @JsonProperty("real_device")
    private int real_device;

    public int getRealDevice() { return this.real_device; }

    public void setRealDevice(int real_device) { this.real_device = real_device; }
}
