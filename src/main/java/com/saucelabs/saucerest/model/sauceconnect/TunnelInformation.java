package com.saucelabs.saucerest.model.sauceconnect;

import java.util.List;

public class TunnelInformation {

    public String allocationType;
    public String backend;
    public Integer build;
    public Integer creationTime;
    public Object directDomains;
    public Object domainNames;
    public String extraInfo;
    public String host;
    public String id;
    public String instance;
    public List<Instance> instances = null;
    public String internalAddress;
    public Object ipAddress;
    public Boolean isReady;
    public Integer lastConnected;
    public Integer launchTime;
    public Metadata metadata;
    public Boolean noProxyCaching;
    public Object noSslBumpDomains;
    public String orgId;
    public String owner;
    public Boolean sharedTunnel;
    public Object shutdownReason;
    public Object shutdownTime;
    public Integer sshPort;
    public String status;
    public Tags tags;
    public List<String> teamIds = null;
    public String tunnelIdentifier;
    public Object useCachingProxy;
    public Boolean useKgp;
    public Object userShutdown;
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