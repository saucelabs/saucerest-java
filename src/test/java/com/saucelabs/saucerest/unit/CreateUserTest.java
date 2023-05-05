package com.saucelabs.saucerest.unit;

import com.saucelabs.saucerest.model.accounts.CreateUser;
import com.saucelabs.saucerest.model.accounts.Roles;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreateUserTest {

    @Test
    public void checkParameterTest() {
        List<String> passwords = Arrays.asList("testtest", "testtest1", "testtest!", "testtest1!", "testTEST1", "test");

        for (String password : passwords) {
            assertThrows(IllegalArgumentException.class, () ->
                new CreateUser.Builder()
                    .setUserName("test")
                    .setFirstName("test")
                    .setLastName("test")
                    .setEmail("test@example.com")
                    .setOrganization("test")
                    .setRole(Roles.MEMBER)
                    .setPassword(password)
                    .build());
        }

        assertDoesNotThrow(() -> {
            new CreateUser.Builder()
                    .setUserName("test")
                    .setFirstName("test")
                    .setLastName("test")
                    .setEmail("test@example.com")
                    .setOrganization("test")
                    .setRole(Roles.MEMBER)
                    .setPassword("testTEST1!")
                    .build();
        });
    }

    @Test
    public void testSetUserName() {
        assertThrows(NullPointerException.class, () -> {
            new CreateUser.Builder()
                    .setFirstName("test")
                    .setLastName("test")
                    .setEmail("test@example.com")
                    .setOrganization("test")
                    .setRole(Roles.MEMBER)
                    .setPassword("testTEST1!")
                    .build();
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new CreateUser.Builder()
                    .setUserName("")
                    .setFirstName("test")
                    .setLastName("test")
                    .setEmail("test@example.com")
                    .setOrganization("test")
                    .setRole(Roles.MEMBER)
                    .setPassword("testTEST1!")
                    .build();
        });
    }

    @Test
    public void testSetFirstName() {
        assertThrows(NullPointerException.class, () -> {
            new CreateUser.Builder()
                    .setUserName("test")
                    .setLastName("test")
                    .setEmail("test@example.com")
                    .setOrganization("test")
                    .setRole(Roles.MEMBER)
                    .setPassword("testTEST1!")
                    .build();
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new CreateUser.Builder()
                    .setUserName("test")
                    .setFirstName("")
                    .setLastName("test")
                    .setEmail("test@example.com")
                    .setOrganization("test")
                    .setRole(Roles.MEMBER)
                    .setPassword("testTEST1!")
                    .build();
        });
    }

    @Test
    public void testSetLastName() {
        assertThrows(NullPointerException.class, () -> {
            new CreateUser.Builder()
                    .setUserName("test")
                    .setFirstName("test")
                    .setEmail("test@example.com")
                    .setOrganization("test")
                    .setRole(Roles.MEMBER)
                    .setPassword("testTEST1!")
                    .build();
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new CreateUser.Builder()
                    .setUserName("test")
                    .setFirstName("test")
                    .setLastName("")
                    .setEmail("test@example.com")
                    .setOrganization("test")
                    .setRole(Roles.MEMBER)
                    .setPassword("testTEST1!")
                    .build();
        });
    }

    @Test
    public void testSetEmail() {
        assertThrows(NullPointerException.class, () -> {
            new CreateUser.Builder()
                    .setUserName("test")
                    .setFirstName("test")
                    .setLastName("test")
                    .setOrganization("test")
                    .setRole(Roles.MEMBER)
                    .setPassword("testTEST1!")
                    .build();
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new CreateUser.Builder()
                    .setUserName("test")
                    .setFirstName("test")
                    .setLastName("test")
                    .setEmail("")
                    .setOrganization("test")
                    .setRole(Roles.MEMBER)
                    .setPassword("testTEST1!")
                    .build();
        });
    }

    @Test
    public void testSetOrganization() {
        assertThrows(NullPointerException.class, () -> {
            new CreateUser.Builder()
                    .setUserName("test")
                    .setFirstName("test")
                    .setLastName("test")
                    .setEmail("test@example.com")
                    .setRole(Roles.MEMBER)
                    .setPassword("testTEST1!")
                    .build();
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new CreateUser.Builder()
                    .setUserName("test")
                    .setFirstName("test")
                    .setLastName("test")
                    .setEmail("")
                    .setOrganization("")
                    .setRole(Roles.MEMBER)
                    .setPassword("testTEST1!")
                    .build();
        });
    }

    @Test
    public void testSetRole() {
        assertThrows(NullPointerException.class, () -> {
            new CreateUser.Builder()
                    .setUserName("test")
                    .setFirstName("test")
                    .setLastName("test")
                    .setEmail("test@example.com")
                    .setOrganization("test")
                    .setPassword("testTEST1!")
                    .build();
        });
    }

    @Test
    public void testSetPassword() {
        List<String> invalidPasswords = Arrays.asList("", "test", "testtest", "testtest1", "testtest!", "testtest1!", "testTEST1");

        for (String password : invalidPasswords) {
            assertThrows(IllegalArgumentException.class, () ->
                    new CreateUser.Builder()
                            .setUserName("test")
                            .setFirstName("test")
                            .setLastName("test")
                            .setEmail("test@example.com")
                            .setOrganization("test")
                            .setRole(Roles.MEMBER)
                            .setPassword(password)
                            .build());
        }

        assertThrows(NullPointerException.class, () ->
                new CreateUser.Builder()
                        .setUserName("test")
                        .setFirstName("test")
                        .setLastName("test")
                        .setEmail("test@example.com")
                        .setOrganization("test")
                        .setRole(Roles.MEMBER)
                        .setPassword(null)
                        .build());
    }
}