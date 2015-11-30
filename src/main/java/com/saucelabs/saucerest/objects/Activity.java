package com.saucelabs.saucerest.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class Activity implements Serializable {
    public static class ActivityStats implements Serializable {
        @JsonProperty("in progress")
        private int in_progress;

        public int getInProgress() { return this.in_progress; }

        @JsonProperty("all")
        private int all;

        public int getAll() { return this.all; }

        @JsonProperty("queued")
        private int queued;

        public int getQueued() { return this.queued; }
    }

    @JsonProperty("subaccounts")
    private HashMap<String, ActivityStats> subaccounts;

    public Set<String> getSubaccounts() {
        return subaccounts.keySet();
    }

    public ActivityStats getSubaccount(String username) {
        return subaccounts.get(username);
    }


    @JsonProperty("totals")
    private ActivityStats totals;

    public ActivityStats getTotals() { return this.totals; }
}
