package com.saucelabs.saucerest.model.accounts;

import java.util.HashMap;
import java.util.Map;

public class UpdateUser {
    private final String userID;
    private final String firstName;
    private final String lastName;
    private final String phone;

    private UpdateUser(Builder builder) {
        userID = builder.userID;
        firstName = builder.firstName;
        lastName = builder.lastName;
        phone = builder.phone;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> parameters = new HashMap<>();

        if (this.firstName != null) {
            parameters.put("first_name", this.firstName);
        }

        if (this.lastName != null) {
            parameters.put("last_name", this.lastName);
        }

        if (this.phone != null) {
            parameters.put("phone", this.phone);
        }

        return parameters;
    }

    public String getUserID() {
        return userID;
    }

    public static final class Builder {
        private String userID;
        private String firstName;
        private String lastName;
        private String phone;

        public Builder setUserID(String val) {
            userID = val;
            return this;
        }

        public Builder setFirstName(String val) {
            firstName = val;
            return this;
        }

        public Builder setLastName(String val) {
            lastName = val;
            return this;
        }

        public Builder setPhone(String val) {
            phone = val;
            return this;
        }

        public UpdateUser build() {
            if (phone != null && !phone.matches("^\\+?1?\\d{8,15}$")) {
                throw new IllegalArgumentException("Phone number must be in international format, e.g. +1 1234567890");
            }

            return new UpdateUser(this);
        }
    }
}