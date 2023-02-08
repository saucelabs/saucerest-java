package com.saucelabs.saucerest.model.accounts;

import com.squareup.moshi.Json;

public class Settings {

    @Json(name = "allow_integrations_page")
    public Boolean allowIntegrationsPage;
    @Json(name = "can_use_tunnels_with_public_real_devices")
    public Boolean canUseTunnelsWithPublicRealDevices;
    @Json(name = "country")
    public String country;
    @Json(name = "disable_email_verification")
    public Boolean disableEmailVerification;
    @Json(name = "groups_enabled")
    public Boolean groupsEnabled;
    @Json(name = "jit_default_team")
    public Object jitDefaultTeam;
    @Json(name = "jit_username_prefix")
    public String jitUsernamePrefix;
    @Json(name = "jobs_cross_team_sharing")
    public Boolean jobsCrossTeamSharing;
    @Json(name = "live_only")
    public Boolean liveOnly;
    @Json(name = "logout_url")
    public String logoutUrl;
    @Json(name = "mac_virtual_machines")
    public Integer macVirtualMachines;
    @Json(name = "performance_enabled")
    public Boolean performanceEnabled;
    @Json(name = "rdc_enabled")
    public Boolean rdcEnabled;
    @Json(name = "real_devices")
    public Integer realDevices;
    @Json(name = "sso_enabled")
    public Boolean ssoEnabled;
    @Json(name = "sso_only")
    public Boolean ssoOnly;
    @Json(name = "team_limit")
    public Integer teamLimit;
    @Json(name = "team_limit_reached")
    public Boolean teamLimitReached;
    @Json(name = "to_plan")
    public Object toPlan;
    @Json(name = "trial_period")
    public Object trialPeriod;
    @Json(name = "tunnels_lockdown")
    public Boolean tunnelsLockdown;
    @Json(name = "user_type")
    public String userType;
    @Json(name = "virtual_machines")
    public Integer virtualMachines;
    @Json(name = "vm_lockdown")
    public Boolean vmLockdown;

    /**
     * No args constructor for use in serialization
     */
    public Settings() {
    }

    public Settings(Boolean allowIntegrationsPage, Boolean canUseTunnelsWithPublicRealDevices, String country, Boolean disableEmailVerification, Boolean groupsEnabled, Object jitDefaultTeam, String jitUsernamePrefix, Boolean jobsCrossTeamSharing, Boolean liveOnly, String logoutUrl, Integer macVirtualMachines, Boolean performanceEnabled, Boolean rdcEnabled, Integer realDevices, Boolean ssoEnabled, Boolean ssoOnly, Integer teamLimit, Boolean teamLimitReached, Object toPlan, Object trialPeriod, Boolean tunnelsLockdown, String userType, Integer virtualMachines, Boolean vmLockdown) {
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
    }

    private Settings(Builder builder) {
        virtualMachines = builder.virtualMachines;
    }

    public static final class Builder {
        private Integer virtualMachines;

        public Builder() {
        }

        public Builder setVirtualMachines(Integer val) {
            virtualMachines = val;
            return this;
        }

        public Settings build() {
            return new Settings(this);
        }
    }
}