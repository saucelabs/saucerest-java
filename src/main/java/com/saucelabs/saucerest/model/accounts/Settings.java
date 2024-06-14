package com.saucelabs.saucerest.model.accounts;

public class Settings {

    public Boolean allowIntegrationsPage;
    public Boolean canUseTunnelsWithPublicRealDevices;
    public String country;
    public Boolean disableEmailVerification;
    public Boolean groupsEnabled;
    public Object jitDefaultTeam;
    public String jitUsernamePrefix;
    public Boolean jobsCrossTeamSharing;
    public Boolean liveOnly;
    public String logoutUrl;
    public Integer macVirtualMachines;
    public Boolean performanceEnabled;
    public Boolean rdcEnabled;
    public Integer realDevices;
    public Boolean ssoEnabled;
    public Boolean ssoOnly;
    public Integer teamLimit;
    public Boolean teamLimitReached;
    public Object toPlan;
    public Object trialPeriod;
    public Boolean tunnelsLockdown;
    public String userType;
    public Integer virtualMachines;
    public Boolean vmLockdown;
    public Boolean ssoLegacyEnabled;

    /**
     * No args constructor for use in serialization
     */
    public Settings() {
    }

    public Settings(Boolean allowIntegrationsPage, Boolean canUseTunnelsWithPublicRealDevices, String country, Boolean disableEmailVerification, Boolean groupsEnabled, Object jitDefaultTeam, String jitUsernamePrefix, Boolean jobsCrossTeamSharing, Boolean liveOnly, String logoutUrl, Integer macVirtualMachines, Boolean performanceEnabled, Boolean rdcEnabled, Integer realDevices, Boolean ssoEnabled, Boolean ssoOnly, Integer teamLimit, Boolean teamLimitReached, Object toPlan, Object trialPeriod, Boolean tunnelsLockdown, String userType, Integer virtualMachines, Boolean vmLockdown, Boolean ssoLegacyEnabled) {
        super();
        this.allowIntegrationsPage = allowIntegrationsPage;
        this.canUseTunnelsWithPublicRealDevices = canUseTunnelsWithPublicRealDevices;
        this.country = country;
        this.disableEmailVerification = disableEmailVerification;
        this.groupsEnabled = groupsEnabled;
        this.jitDefaultTeam = jitDefaultTeam;
        this.jitUsernamePrefix = jitUsernamePrefix;
        this.jobsCrossTeamSharing = jobsCrossTeamSharing;
        this.liveOnly = liveOnly;
        this.logoutUrl = logoutUrl;
        this.macVirtualMachines = macVirtualMachines;
        this.performanceEnabled = performanceEnabled;
        this.rdcEnabled = rdcEnabled;
        this.realDevices = realDevices;
        this.ssoEnabled = ssoEnabled;
        this.ssoOnly = ssoOnly;
        this.teamLimit = teamLimit;
        this.teamLimitReached = teamLimitReached;
        this.toPlan = toPlan;
        this.trialPeriod = trialPeriod;
        this.tunnelsLockdown = tunnelsLockdown;
        this.userType = userType;
        this.virtualMachines = virtualMachines;
        this.vmLockdown = vmLockdown;
        this.ssoLegacyEnabled = ssoLegacyEnabled;
    }

    private Settings(Builder builder) {
        virtualMachines = builder.virtualMachines;
    }

    public static final class Builder {
        private Integer virtualMachines;

        public Builder setVirtualMachines(Integer val) {
            virtualMachines = val;
            return this;
        }

        public Settings build() {
            return new Settings(this);
        }
    }
}