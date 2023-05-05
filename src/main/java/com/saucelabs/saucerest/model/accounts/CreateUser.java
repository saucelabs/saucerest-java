package com.saucelabs.saucerest.model.accounts;

import java.util.HashMap;
import java.util.Map;

public class CreateUser {
    public String firstName;
    public String lastName;
    public String email;
    public String userName;
    public String password;
    public String organization;
    public Integer role;
    public String team;

    private CreateUser(Builder builder) {
        firstName = builder.firstName;
        lastName = builder.lastName;
        email = builder.email;
        userName = builder.userName;
        password = builder.password;
        organization = builder.organization;
        role = builder.role;
        team = builder.team;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> parameters = new HashMap<>();

        if (this.firstName != null) {
            parameters.put("first_name", this.firstName);
        }

        if (this.lastName != null) {
            parameters.put("last_name", this.lastName);
        }

        if (this.email != null) {
            parameters.put("email", this.email);
        }

        if (this.userName != null) {
            parameters.put("username", this.userName);
        }

        if (this.password != null) {
            parameters.put("password", this.password);
        }

        if (this.organization != null) {
            parameters.put("organization", this.organization);
        }

        if (this.role != null) {
            parameters.put("role", this.role);
        }

        if (this.team != null) {
            parameters.put("team", this.team);
        }

        return parameters;
    }

    public static final class Builder {
        private String firstName;
        private String lastName;
        private String email;
        private String userName;
        private String password;
        private String organization;
        private Integer role;
        private String team;

        public Builder setFirstName(String val) {
            firstName = val;
            return this;
        }

        public Builder setLastName(String val) {
            lastName = val;
            return this;
        }

        public Builder setEmail(String val) {
            email = val;
            return this;
        }

        public Builder setUserName(String val) {
            userName = val;
            return this;
        }

        public Builder setPassword(String val) {
            password = val;
            return this;
        }

        public Builder setOrganization(String val) {
            organization = val;
            return this;
        }

        public Builder setRole(Roles val) {
            role = val.getValue();
            return this;
        }

        public Builder setTeam(String val) {
            team = val;
            return this;
        }

        public CreateUser build() {
            checkParameter();

            return new CreateUser(this);
        }

        /**
         * Based on <a href="https://docs.saucelabs.com/dev/api/accounts/#create-a-new-user">here</a>
         */
        private void checkParameter() {
            if (firstName == null || firstName.isEmpty())
                throw new IllegalArgumentException("First name is required");

            if (lastName == null || lastName.isEmpty())
                throw new IllegalArgumentException("Last name is required");

            if (email == null || email.isEmpty())
                throw new IllegalArgumentException("Email is required");

            if (userName == null || userName.isEmpty())
                throw new IllegalArgumentException("Username is required");

            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Password is required");
            } else if (password.length() <= 8) {
                throw new IllegalArgumentException("Password must be at least 8 characters");
            } else if (!password.matches(".*[0-9].*")) {
                throw new IllegalArgumentException("Password must contain at least one number");
            } else if (!password.matches(".*[A-Z].*")) {
                throw new IllegalArgumentException("Password must contain at least one uppercase letter");
            } else if (!password.matches(".*[a-z].*")) {
                throw new IllegalArgumentException("Password must contain at least one lowercase letter");
            } else if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
                throw new IllegalArgumentException("Password must contain at least one special character");
            } else if (password.matches(".*\\s.*")) {
                throw new IllegalArgumentException("Password must not contain any whitespace");
            }

            if (organization == null || organization.isEmpty())
                throw new IllegalArgumentException("Organization is required");

            if (role == null)
                throw new IllegalArgumentException("Role is required");
        }
    }
}