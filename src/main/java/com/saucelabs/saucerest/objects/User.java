package com.saucelabs.saucerest.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.saucelabs.saucerest.deserializers.UnixtimeDeserializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

// http://json2java.azurewebsites.net/
public class User implements Serializable {
    @JsonProperty("domain")
    private String domain;

    public String getDomain() { return this.domain; }

    public void setDomain(String domain) { this.domain = domain; }

    @JsonProperty("last_name")
    private String last_name;

    public String getLastName() { return this.last_name; }

    public void setLastName(String last_name) { this.last_name = last_name; }

    @JsonProperty("creation_time")
    @JsonDeserialize(using=UnixtimeDeserializer.class)
    private Date creation_time;

    public Date getCreationTime() { return this.creation_time; }

    public void setCreationTime(Date creation_time) { this.creation_time = creation_time; }

    @JsonProperty("user_type")
    private String user_type; // FIXME - enum

    public String getUserType() { return this.user_type; }

    public void setUserType(String user_type) { this.user_type = user_type; }

    @JsonProperty("concurrency_limit")
    private ConcurrencyLimit concurrency_limit;

    public ConcurrencyLimit getConcurrencyLimit() { return this.concurrency_limit; }

    public void setConcurrencyLimit(ConcurrencyLimit concurrency_limit) { this.concurrency_limit = concurrency_limit; }

    @JsonProperty("manual_minutes")
    private int manual_minutes;

    public int getManualMinutes() { return this.manual_minutes; }

    public void setManualMinutes(int manual_minutes) { this.manual_minutes = manual_minutes; }

    @JsonProperty("can_run_manual")
    private boolean can_run_manual;

    public boolean getCanRunManual() { return this.can_run_manual; }

    public void setCanRunManual(boolean can_run_manual) { this.can_run_manual = can_run_manual; }

    @JsonProperty("prevent_emails")
    private ArrayList<String> prevent_emails; // This is a list of keys that are prevented. Enum? FIXME

    public ArrayList<String> getPreventEmails() { return this.prevent_emails; }

    public void setPreventEmails(ArrayList<String> prevent_emails) { this.prevent_emails = prevent_emails; }

    @JsonProperty("id")
    private String id;

    public String getId() { return this.id; }

    public void setId(String id) { this.id = id; }

    @JsonProperty("first_name")
    private String first_name;

    public String getFirstName() { return this.first_name; }

    public void setFirstName(String first_name) { this.first_name = first_name; }

    @JsonProperty("verified")
    private boolean verified;

    public boolean getVerified() { return this.verified; }

    public void setVerified(boolean verified) { this.verified = verified; }

    @JsonProperty("subscribed")
    private boolean subscribed;

    public boolean getSubscribed() { return this.subscribed; }

    public void setSubscribed(boolean subscribed) { this.subscribed = subscribed; }

    @JsonProperty("title")
    private String title;

    public String getTitle() { return this.title; }

    public void setTitle(String title) { this.title = title; }

    @JsonProperty("ancestor_allows_subaccounts")
    private boolean ancestor_allows_subaccounts;

    public boolean getAncestorAllowsSubaccounts() { return this.ancestor_allows_subaccounts; }

    public void setAncestorAllowsSubaccounts(boolean ancestor_allows_subaccounts) { this.ancestor_allows_subaccounts = ancestor_allows_subaccounts; }

    @JsonProperty("email")
    private String email;

    public String getEmail() { return this.email; }

    public void setEmail(String email) { this.email = email; }

    @JsonProperty("username")
    private String username;

    public String getUsername() { return this.username; }

    public void setUsername(String username) { this.username = username; }

    @JsonProperty("vm_lockdown")
    private boolean vm_lockdown;

    public boolean getVmLockdown() { return this.vm_lockdown; }

    public void setVmLockdown(boolean vm_lockdown) { this.vm_lockdown = vm_lockdown; }

    @JsonProperty("parent")
    private String parent;

    public String getParent() { return this.parent; }

    public void setParent(String parent) { this.parent = parent; }

    @JsonProperty("is_admin")
    private String is_admin; // FIXME - pretty sure this should be a boolean

    public String getIsAdmin() { return this.is_admin; }

    public void setIsAdmin(String is_admin) { this.is_admin = is_admin; }

    @JsonProperty("access_key")
    private String access_key;

    public String getAccessKey() { return this.access_key; }

    public void setAccessKey(String access_key) { this.access_key = access_key; }

    @JsonProperty("name")
    private String name;

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    @JsonProperty("is_sso")
    private boolean is_sso;

    public boolean getIsSso() { return this.is_sso; }

    public void setIsSso(boolean is_sso) { this.is_sso = is_sso; }

    @JsonProperty("entity_type")
    private String entity_type;

    public String getEntityType() { return this.entity_type; }

    public void setEntityType(String entity_type) { this.entity_type = entity_type; }

    @JsonProperty("ancestor_concurrency_limit")
    private ConcurrencyLimit ancestor_concurrency_limit;

    public ConcurrencyLimit getAncestorConcurrencyLimit() { return this.ancestor_concurrency_limit; }

    public void setAncestorConcurrencyLimit(ConcurrencyLimit ancestor_concurrency_limit) { this.ancestor_concurrency_limit = ancestor_concurrency_limit; }

    @JsonProperty("minutes")
    private int minutes;

    public int getMinutes() { return this.minutes; }

    public void setMinutes(int minutes) { this.minutes = minutes; }
}
