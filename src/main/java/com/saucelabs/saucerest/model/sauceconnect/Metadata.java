package com.saucelabs.saucerest.model.sauceconnect;

import java.math.BigInteger;

public class Metadata {

    public String hostname;
    public Long hostMemory;
    public String commandArgs;
    public String gitVersion;
    public String platform;
    public String command;
    public String build;
    public String externalProxy;
    public String release;
    public String hostCpu;
    public BigInteger nofileLimit;

    /**
     * No args constructor for use in serialization
     */
    public Metadata() {
    }

    /**
     * @param hostname
     * @param gitVersion
     * @param externalProxy
     * @param build
     * @param commandArgs
     * @param release
     * @param nofileLimit
     * @param hostMemory
     * @param hostCpu
     * @param platform
     * @param command
     */
    public Metadata(String hostname, Long hostMemory, String commandArgs, String gitVersion, String platform, String command, String build, String externalProxy, String release, String hostCpu, BigInteger nofileLimit) {
        super();
        this.hostname = hostname;
        this.hostMemory = hostMemory;
        this.commandArgs = commandArgs;
        this.gitVersion = gitVersion;
        this.platform = platform;
        this.command = command;
        this.build = build;
        this.externalProxy = externalProxy;
        this.release = release;
        this.hostCpu = hostCpu;
        this.nofileLimit = nofileLimit;
    }
}