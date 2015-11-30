package com.saucelabs.saucerest.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.saucelabs.saucerest.deserializers.UnixtimeDeserializer;

public class Tunnel implements Serializable {
    public static class Metadata implements Serializable {
        @JsonProperty("hostname")
        private String hostname;

        public String getHostname() { return this.hostname; }

        @JsonProperty("git_version")
        private String git_version;

        public String getGitVersion() { return this.git_version; }

        @JsonProperty("platform")
        private String platform;

        public String getPlatform() { return this.platform; }

        @JsonProperty("command")
        private String command;

        public String getCommand() { return this.command; }

        @JsonProperty("build")
        private int build;

        public int getBuild() { return this.build; }

        @JsonProperty("release")
        private String release;

        public String getRelease() { return this.release; }

        @JsonProperty("nofile_limit")
        private int nofile_limit;

        public int getNofileLimit() { return this.nofile_limit; }
    }
    @JsonProperty("status")
    private String status;

    public String getStatus() { return this.status; }

    @JsonProperty("direct_domains")
    private ArrayList<String> direct_domains;

    public ArrayList<String> getDirectDomains() { return this.direct_domains; }

    @JsonProperty("vm_version")
    private String vm_version;

    public String getVmVersion() { return this.vm_version; }

    @JsonProperty("last_connected")
    @JsonDeserialize(using=UnixtimeDeserializer.class)
    private Date last_connected;

    public Date getLastConnected() { return this.last_connected; }

    @JsonProperty("shutdown_time")
    @JsonDeserialize(using=UnixtimeDeserializer.class)
    private Date shutdown_time;

    public Date getShutdownTime() { return this.shutdown_time; }

    @JsonProperty("ssh_port")
    private int ssh_port;

    public int getSshPort() { return this.ssh_port; }

    @JsonProperty("launch_time")
    @JsonDeserialize(using=UnixtimeDeserializer.class)
    private Date launch_time;

    public Date getLaunchTime() { return this.launch_time; }

    @JsonProperty("user_shutdown")
    private Boolean user_shutdown;

    public Boolean getUserShutdown() { return this.user_shutdown; }

    @JsonProperty("use_caching_proxy")

    private Boolean use_caching_proxy;

    public Boolean getUseCachingProxy() { return this.use_caching_proxy; }

    @JsonProperty("creation_time")
    @JsonDeserialize(using=UnixtimeDeserializer.class)
    private Date creation_time;

    public Date getCreationTime() { return this.creation_time; }

    @JsonProperty("domain_names")
    private ArrayList<String> domain_names;

    public ArrayList<String> getDomainNames() { return this.domain_names; }

    @JsonProperty("shared_tunnel")
    private boolean shared_tunnel;

    public boolean getSharedTunnel() { return this.shared_tunnel; }

    @JsonProperty("tunnel_identifier")
    private String tunnel_identifier;

    public String getTunnelIdentifier() { return this.tunnel_identifier; }

    @JsonProperty("host")
    private String host;

    public String getHost() { return this.host; }

    @JsonProperty("no_proxy_caching")
    private boolean no_proxy_caching;

    public boolean getNoProxyCaching() { return this.no_proxy_caching; }

    @JsonProperty("owner")
    private String owner;

    public String getOwner() { return this.owner; }

    @JsonProperty("use_kgp")
    private boolean use_kgp;

    public boolean getUseKgp() { return this.use_kgp; }

    @JsonProperty("no_ssl_bump_domains")
    private ArrayList<String> no_ssl_bump_domains; // FIXME - this can be null or a list

    public ArrayList<String> getNoSslBumpDomains() { return this.no_ssl_bump_domains; }

    @JsonProperty("id")
    private String id;

    public String getId() { return this.id; }

    @JsonProperty("metadata")
    private Metadata metadata;

    public Metadata getMetadata() { return this.metadata; }
}