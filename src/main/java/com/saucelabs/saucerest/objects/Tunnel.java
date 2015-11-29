package com.saucelabs.saucerest.objects;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tunnel {
    @JsonProperty("status")
    private String status;
    @JsonProperty("direct_domains")
    private String directDomains;
    @JsonProperty("vm_version")
    private String vmVersion;
    @JsonProperty("shutdown_time")
    private String shutdownTime;
    @JsonProperty("ssh_port")
    private String sshPort;
    @JsonProperty("user_shutdown")
    private String userShutdown;
    @JsonProperty("use_caching_proxy")
    private String useCachingProxy;
    @JsonProperty("creation_time")
    private String creationTime;
    @JsonProperty("domain_names")
    private List<String> domainNames;
    @JsonProperty("shared_tunnel")
    private String sharedTunnel;
    @JsonProperty("tunnel_identifier")
    private String tunnelIdentifier;
    @JsonProperty("host")
    private String host;
    @JsonProperty("owner")
    private String owner;
    @JsonProperty("use_kgp")
    private String useKgp;
    @JsonProperty("no_ssl_bump_domains")
    private String noSslBumpDomains;
    @JsonProperty("id")
    private String id;
    @JsonProperty("metadata")
    private Map<String, String> metadata;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDirectDomains() {
        return directDomains;
    }

    public void setDirectDomains(String directDomains) {
        this.directDomains = directDomains;
    }

    public String getVmVersion() {
        return vmVersion;
    }

    public void setVmVersion(String vm_version) {
        this.vmVersion = vm_version;
    }

    public String getShutdownTime() {
        return shutdownTime;
    }

    public void setShutdownTime(String shutdownTime) {
        this.shutdownTime = shutdownTime;
    }

    public String getSshPort() {
        return sshPort;
    }

    public void setSshPort(String sshPort) {
        this.sshPort = sshPort;
    }

    public String getUserShutdown() {
        return userShutdown;
    }

    public void setUserShutdown(String userShutdown) {
        this.userShutdown = userShutdown;
    }

    public String getUseCachingProxy() {
        return useCachingProxy;
    }

    public void setUseCachingProxy(String useCachingProxy) {
        this.useCachingProxy = useCachingProxy;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public List<String> getDomainNames() {
        return domainNames;
    }

    public void setDomainNames(List<String> domainNames) {
        this.domainNames = domainNames;
    }

    public String getSharedTunnel() {
        return sharedTunnel;
    }

    public void setSharedTunnel(String sharedTunnel) {
        this.sharedTunnel = sharedTunnel;
    }

    public String getTunnelIdentifier() {
        return tunnelIdentifier;
    }

    public void setTunnelIdentifier(String tunnelIdentifier) {
        this.tunnelIdentifier = tunnelIdentifier;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getUseKgp() {
        return useKgp;
    }

    public void setUseKgp(String useKgp) {
        this.useKgp = useKgp;
    }

    public String getNoSslBumpDomains() {
        return noSslBumpDomains;
    }

    public void setNoSslBumpDomains(String noSslBumpDomains) {
        this.noSslBumpDomains = noSslBumpDomains;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}