package com.saucelabs.saucerest.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.saucelabs.saucerest.deserializers.UnixtimeDeserializer;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class Concurrency implements Serializable {

    public static class SubAccountConcurrencyValues implements Serializable
    {
        private ConcurrencyValues current;

        public ConcurrencyValues getCurrent() { return this.current; }

        public void setCurrent(ConcurrencyValues current) { this.current = current; }

        private ConcurrencyValues remaining;

        public ConcurrencyValues getRemaining() { return this.remaining; }

        public void setRemaining(ConcurrencyValues remaining) { this.remaining = remaining; }
    }

    public static class ConcurrencyValues implements Serializable
    {
        @JsonProperty("overall")
        private int overall;

        public int getOverall() { return this.overall; }

        public void setOverall(int overall) { this.overall = overall; }

        @JsonProperty("mac")
        private int mac;

        public int getMac() { return this.mac; }

        public void setMac(int mac) { this.mac = mac; }

        @JsonProperty("manual")
        private int manual;

        public int getManual() { return this.manual; }

        public void setManual(int manual) { this.manual = manual; }
    }

    @JsonProperty("timestamp")
    @JsonDeserialize(using=UnixtimeDeserializer.class)
    private Date timestamp;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("concurrency")
    private HashMap<String, SubAccountConcurrencyValues> subaccounts;

    public void setSubaccounts(HashMap<String, SubAccountConcurrencyValues> subaccounts) {
        this.subaccounts = subaccounts;
    }

    public Set<String> getSubaccounts() {
        return subaccounts.keySet();
    }

    public SubAccountConcurrencyValues getSubaccount(String username) {
        return subaccounts.get(username);
    }
}
