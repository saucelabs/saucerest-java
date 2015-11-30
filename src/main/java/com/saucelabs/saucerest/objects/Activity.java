package com.saucelabs.saucerest.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by halkeye on 2015-11-29.
 */
public class Activity implements Serializable {
    public static class ActivityStats implements Serializable {
        @JsonProperty("in progress")
        private int in_progress;

        public int getInProgress() { return this.in_progress; }

        public void setInProgress(int in_progress) { this.in_progress = in_progress; }

        @JsonProperty("all")
        private int all;

        public int getAll() { return this.all; }

        public void setAll(int all) { this.all = all; }

        @JsonProperty("queued")
        private int queued;

        public int getQueued() { return this.queued; }

        public void setQueued(int queued) { this.queued = queued; }
    }

    @JsonProperty("subaccounts")
    private HashMap<String, ActivityStats> subaccounts;

    public void setSubaccounts(HashMap<String, ActivityStats> subaccounts) {
        this.subaccounts = subaccounts;
    }

    public Set<String> getSubaccounts() {
        return subaccounts.keySet();
    }

    public ActivityStats getSubaccount(String username) {
        return subaccounts.get(username);
    }


    @JsonProperty("totals")
    private ActivityStats totals;

    public ActivityStats getTotals() { return this.totals; }

    public void setTotals(ActivityStats totals) { this.totals = totals; }

}
