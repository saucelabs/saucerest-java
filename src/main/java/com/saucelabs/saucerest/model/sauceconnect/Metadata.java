package com.saucelabs.saucerest.model.sauceconnect;

import com.squareup.moshi.Json;

public class Metadata {

    @Json(name = "hostname")
    public String hostname;
    @Json(name = "host_memory")
    public Long hostMemory;
    @Json(name = "command_args")
    public String commandArgs;
    @Json(name = "git_version")
    public String gitVersion;
    @Json(name = "platform")
    public String platform;
    @Json(name = "command")
    public String command;
    @Json(name = "build")
    public String build;
    @Json(name = "external_proxy")
    public String externalProxy;
    @Json(name = "release")
    public String release;
    @Json(name = "host_cpu")
    public String hostCpu;
    @Json(name = "nofile_limit")
    public Integer nofileLimit;

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
    public Metadata(String hostname, Long hostMemory, String commandArgs, String gitVersion, String platform, String command, String build, String externalProxy, String release, String hostCpu, Integer nofileLimit) {
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