package com.saucelabs.saucerest.model.sauceconnect;

import com.squareup.moshi.Json;
import java.util.List;

public class TunnelInformation {

    @Json(name = "allocation_type")
    public String allocationType;
    @Json(name = "backend")
    public String backend;
    @Json(name = "build")
    public Integer build;
    @Json(name = "creation_time")
    public Integer creationTime;
    @Json(name = "direct_domains")
    public Object directDomains;
    @Json(name = "domain_names")
    public Object domainNames;
    @Json(name = "extra_info")
    public String extraInfo;
    @Json(name = "host")
    public String host;
    @Json(name = "id")
    public String id;
    @Json(name = "instance")
    public String instance;
    @Json(name = "instances")
    public List<Instance> instances = null;
    @Json(name = "internal_address")
    public String internalAddress;
    @Json(name = "ip_address")
    public Object ipAddress;
    @Json(name = "is_ready")
    public Boolean isReady;
    @Json(name = "last_connected")
    public Integer lastConnected;
    @Json(name = "launch_time")
    public Integer launchTime;
    @Json(name = "metadata")
    public Metadata metadata;
    @Json(name = "no_proxy_caching")
    public Boolean noProxyCaching;
    @Json(name = "no_ssl_bump_domains")
    public Object noSslBumpDomains;
    @Json(name = "org_id")
    public String orgId;
    @Json(name = "owner")
    public String owner;
    @Json(name = "shared_tunnel")
    public Boolean sharedTunnel;
    @Json(name = "shutdown_reason")
    public Object shutdownReason;
    @Json(name = "shutdown_time")
    public Object shutdownTime;
    @Json(name = "ssh_port")
    public Integer sshPort;
    @Json(name = "status")
    public String status;
    @Json(name = "tags")
    public Tags tags;
    @Json(name = "team_ids")
    public List<String> teamIds = null;
    @Json(name = "tunnel_identifier")
    public String tunnelIdentifier;
    @Json(name = "use_caching_proxy")
    public Object useCachingProxy;
    @Json(name = "use_kgp")
    public Boolean useKgp;
    @Json(name = "user_shutdown")
    public Object userShutdown;
    @Json(name = "vm_version")
    public Object vmVersion;

    /**
     * No args constructor for use in serialization
     */
    public TunnelInformation() {
    }

    /**
     * @param metadata
     * @param instance
     * @param creationTime
     * @param instances
     * @param isReady
     * @param useCachingProxy
     * @param orgId
     * @param internalAddress
     * @param tunnelIdentifier
     * @param lastConnected
     * @param host
     * @param backend
     * @param domainNames
     * @param id
     * @param shutdownTime
     * @param directDomains
     * @param owner
     * @param sshPort
     * @param userShutdown
     * @param useKgp
     * @param ipAddress
     * @param sharedTunnel
     * @param allocationType
     * @param teamIds
     * @param noProxyCaching
     * @param tags
     * @param launchTime
     * @param build
     * @param shutdownReason
     * @param vmVersion
     * @param noSslBumpDomains
     * @param extraInfo
     * @param status
     */
    public TunnelInformation(String allocationType, String backend, Integer build, Integer creationTime, Object directDomains, Object domainNames, String extraInfo, String host, String id, String instance, List<Instance> instances, String internalAddress, Object ipAddress, Boolean isReady, Integer lastConnected, Integer launchTime, Metadata metadata, Boolean noProxyCaching, Object noSslBumpDomains, String orgId, String owner, Boolean sharedTunnel, Object shutdownReason, Object shutdownTime, Integer sshPort, String status, Tags tags, List<String> teamIds, String tunnelIdentifier, Object useCachingProxy, Boolean useKgp, Object userShutdown, Object vmVersion) {
        super();
        this.allocationType = allocationType;
        this.backend = backend;
        this.build = build;
        this.creationTime = creationTime;
        this.directDomains = directDomains;
        this.domainNames = domainNames;
        this.extraInfo = extraInfo;
        this.host = host;
        this.id = id;
        this.instance = instance;
        this.instances = instances;
        this.internalAddress = internalAddress;
        this.ipAddress = ipAddress;
        this.isReady = isReady;
        this.lastConnected = lastConnected;
        this.launchTime = launchTime;
        this.metadata = metadata;
        this.noProxyCaching = noProxyCaching;
        this.noSslBumpDomains = noSslBumpDomains;
        this.orgId = orgId;
        this.owner = owner;
        this.sharedTunnel = sharedTunnel;
        this.shutdownReason = shutdownReason;
        this.shutdownTime = shutdownTime;
        this.sshPort = sshPort;
        this.status = status;
        this.tags = tags;
        this.teamIds = teamIds;
        this.tunnelIdentifier = tunnelIdentifier;
        this.useCachingProxy = useCachingProxy;
        this.useKgp = useKgp;
        this.userShutdown = userShutdown;
        this.vmVersion = vmVersion;
    }
}