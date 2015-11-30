package com.saucelabs.saucerest.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.saucelabs.saucerest.deserializers.UnixtimeDeserializer;

import java.io.Serializable;
import java.util.Date;

// http://json2java.azurewebsites.net/
public class File implements Serializable {
    @JsonProperty("size")
    private int size;

    public int getSize() { return this.size; }

    public void setSize(int size) { this.size = size; }

    @JsonProperty("mtime")
    @JsonDeserialize(using=UnixtimeDeserializer.class)
    private Date mtime;

    public Date getMtime() { return this.mtime; }

    public void setMtime(Date mtime) { this.mtime = mtime; }

    @JsonProperty("name")
    private String name;

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    @JsonProperty("md5")
    private String md5;

    public String getMd5() { return this.md5; }

    public void setMd5(String md5) { this.md5 = md5; }
}
