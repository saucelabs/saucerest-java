package com.saucelabs.saucerest.integration;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceException;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.Accounts;
import com.saucelabs.saucerest.model.accounts.*;
import com.saucelabs.saucerest.model.realdevices.Concurrency;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class AccountsTest {
    private static User createTestUser(Accounts accounts) throws IOException {
        CreateUser createUser = new CreateUser.Builder()
            .setEmail(RandomStringUtils.randomAlphabetic(8) + "@example.com")
            .setFirstName(RandomStringUtils.randomAlphabetic(8))
            .setLastName(RandomStringUtils.randomAlphabetic(8))
            .setPassword(RandomStringUtils.randomNumeric(4) + RandomStringUtils.randomAlphabetic(4) + "!$%" + "Aa")
            .setOrganization(accounts.getOrganization().results.get(0).id)
            .setRole(Roles.MEMBER)
            .setUserName("saucerest-java-integration-test-user-" + RandomStringUtils.randomAlphabetic(8))
            .build();

        return accounts.createUser(createUser);
    }

    private static CreateTeam createTeam(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();
        String teamName = "000" + RandomStringUtils.randomAlphabetic(12);

        Settings settings = new Settings.Builder()
            .setVirtualMachines(0)
            .build();

        return accounts.createTeam(teamName, settings, RandomStringUtils.randomAlphabetic(8));
    }

    @AfterAll
    @EnumSource(DataCenter.class)
    public static void cleanup(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();

        // TODO: add user deletion when Sauce Labs has a delete user API. meanwhile, we'll just deactivate them
        List<Result> users = accounts.lookupUsers().results;
        for (Result user : users) {
            if (user.username.startsWith("saucerest-java-integration-test-user-")) {
                accounts.deactivateUser(user.id);
            }
        }

        List<Result> teams = accounts.lookupTeams().results;
        for (Result team : teams) {
            if (team.name.startsWith("000")) {
                accounts.deleteTeam(team.id);
            }
        }
    }

    @BeforeAll
    @EnumSource(DataCenter.class)
    public static void setup(DataCenter dataCenter) throws IOException {
        createTeam(dataCenter);
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void lookupTeamsWithoutNameTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();
        LookupTeams lookupTeams = accounts.lookupTeams();

        assertTrue(lookupTeams.count > 0);
        assertTrue(lookupTeams.results.size() > 0);
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void lookupTeamsWithNameTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();

        LookupTeams lookupTeams = accounts.lookupTeams("NotExisting");

        assertEquals(0, lookupTeams.count);
        assertEquals(0, lookupTeams.results.size());
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getSpecificTeamTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();
        LookupTeams lookupTeams = accounts.lookupTeams();

        for (Result result : lookupTeams.results) {
            Team team = accounts.getSpecificTeam(result.id);

            assertTrue(team.id.length() > 0);
            assertTrue(team.name.length() > 0);
        }
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getSpecificTeamNotFoundTest(DataCenter dataCenter) {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();

        assertThrows(SauceException.NotFound.class, () -> accounts.getSpecificTeam("1234"));
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void getOrganizationTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();

        Organizations organizations = accounts.getOrganization();

        assertNotNull(organizations);
        assertEquals(1, organizations.count);
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_EAST"}, mode = EnumSource.Mode.EXCLUDE)
    public void createTeamTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();
        String teamName = "000" + RandomStringUtils.randomAlphabetic(12);

        Settings settings = new Settings.Builder()
            .setVirtualMachines(0)
            .build();

        CreateTeam createTeam = accounts.createTeam(teamName, settings, RandomStringUtils.randomAlphabetic(8));

        assertNotNull(createTeam);
        assertEquals(teamName, createTeam.name);

        assertEquals(204, accounts.deleteTeam(createTeam.id).code());
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_EAST"}, mode = EnumSource.Mode.EXCLUDE)
    public void updateTeamTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();
        String teamName = "000" + RandomStringUtils.randomAlphabetic(12);

        Settings settings = new Settings.Builder()
            .setVirtualMachines(0)
            .build();

        CreateTeam createTeam = accounts.createTeam(teamName, settings, RandomStringUtils.randomAlphabetic(8));

        assertNotNull(createTeam);
        assertEquals(teamName, createTeam.name);

        UpdateTeam updateTeam = accounts.updateTeam(createTeam.id, "Updated" + teamName, settings, "Updated description");

        assertEquals("Updated description", updateTeam.description);
        assertEquals("Updated" + teamName, updateTeam.name);
        assertEquals(0, updateTeam.settings.virtualMachines);
        assertEquals(204, accounts.deleteTeam(createTeam.id).code());
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_EAST"}, mode = EnumSource.Mode.EXCLUDE)
    public void partiallyUpdateTeamTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();
        String teamName = "000" + RandomStringUtils.randomAlphabetic(12);

        Settings settings = new Settings.Builder()
            .setVirtualMachines(0)
            .build();

        CreateTeam createTeam = accounts.createTeam(teamName, settings, RandomStringUtils.randomAlphabetic(8));

        assertNotNull(createTeam);
        assertEquals(teamName, createTeam.name);

        UpdateTeam partiallyUpdateTeam = new UpdateTeam.Builder()
            .setName("Updated" + teamName)
            .build();

        UpdateTeam updateTeam = accounts.partiallyUpdateTeam(createTeam.id, partiallyUpdateTeam);

        assertEquals("Updated" + teamName, updateTeam.name);
        assertEquals(204, accounts.deleteTeam(createTeam.id).code());
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_EAST"}, mode = EnumSource.Mode.EXCLUDE)
    public void getTeamMembersTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();

        LookupTeams lookupTeams = accounts.lookupTeams();
        TeamMembers teamMembers = accounts.getTeamMembers(lookupTeams.results.get(0).id);

        assertNotNull(teamMembers);
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_EAST"}, mode = EnumSource.Mode.EXCLUDE)
    public void resetAccessKeyTeam(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();
        CreateTeam createTeam = accounts.createTeam("000" + RandomStringUtils.randomAlphabetic(12), new Settings.Builder().setVirtualMachines(0).build(), RandomStringUtils.randomAlphabetic(8));
        User user1 = createTestUser(accounts);
        User user2 = createTestUser(accounts);
        accounts.setUsersTeam(user1.id, createTeam.id);
        accounts.setUsersTeam(user2.id, createTeam.id);
        String accessKeyUser1 = user1.accessKey;
        String accessKeyUser2 = user2.accessKey;

        List<ResetAccessKeyForTeam> resetAccessKeyForTeam = accounts.resetAccessKeyForTeam(createTeam.id);

        assertEquals(2, resetAccessKeyForTeam.size());
        assertNotEquals(accessKeyUser1, resetAccessKeyForTeam.stream().filter(r -> r.id.equals(user1.id)).findFirst().get().accessKey);
        assertNotEquals(accessKeyUser2, resetAccessKeyForTeam.stream().filter(r -> r.id.equals(user2.id)).findFirst().get().accessKey);
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_EAST"}, mode = EnumSource.Mode.EXCLUDE)
    public void lookupUsersWithParametersTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();

        LookupUsersParameter lookupUsersParameter = new LookupUsersParameter.Builder()
            .setRoles(Roles.ORGADMIN)
            .build();

        LookupUsers lookupUsers = accounts.lookupUsers(lookupUsersParameter);

        assertNotNull(lookupUsers);
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_EAST"}, mode = EnumSource.Mode.EXCLUDE)
    public void getUserTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();

        LookupUsers lookupUsers = accounts.lookupUsers();
        User user = accounts.getUser(lookupUsers.results.get(0).id);

        assertNotNull(user);
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_EAST"}, mode = EnumSource.Mode.EXCLUDE)
    public void createUserTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();

        User user = createTestUser(accounts);

        assertNotNull(user);
        assertTrue(user.id.length() > 0);
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_EAST"}, mode = EnumSource.Mode.EXCLUDE)
    public void updateUserTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();
        User testUser = createTestUser(accounts);
        User user = accounts.getUser(testUser.id);
        String timeStamp = String.valueOf(new Random(System.currentTimeMillis()).nextInt()).replace("-", "");

        UpdateUser updateUser = new UpdateUser.Builder()
            .setUserID(user.id)
            .setFirstName("Updated " + timeStamp)
            .setLastName("Updated " + timeStamp)
            .setPhone("+123456789")
            .build();

        User updatedUser = accounts.updateUser(updateUser);

        assertAll("User",
            () -> assertEquals("Updated " + timeStamp, updatedUser.firstName),
            () -> assertEquals("Updated " + timeStamp, updatedUser.lastName),
            () -> assertEquals("+123456789", updatedUser.phone));
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_EAST"}, mode = EnumSource.Mode.EXCLUDE)
    public void partiallyUpdateUserTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();
        User testUser = createTestUser(accounts);
        User user = accounts.getUser(testUser.id);
        String timeStamp = String.valueOf(new Random(System.currentTimeMillis()).nextInt()).replace("-", "");

        UpdateUser updateUser = new UpdateUser.Builder()
            .setUserID(user.id)
            .setFirstName("Updated " + timeStamp)
            .build();

        User updatedUser = accounts.partiallyUpdateUser(updateUser);

        assertEquals("Updated " + timeStamp, updatedUser.firstName);
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_EAST", "APAC_SOUTHEAST"}, mode = EnumSource.Mode.EXCLUDE)
    public void getUserConcurrencyTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();

        UserConcurrency userConcurrency = accounts.getUserConcurrency(sauceREST.getUsername());
        Concurrency realDeviceConcurrency = sauceREST.getRealDevices().getConcurrency();

        assertNotNull(userConcurrency);
        assertNotNull(realDeviceConcurrency);
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_EAST"}, mode = EnumSource.Mode.EXCLUDE)
    public void getUsersTeamTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();

        LookupUsersParameter lookupUsersParameter = new LookupUsersParameter.Builder()
            .setUsername(sauceREST.getUsername())
            .build();

        LookupUsers lookupUsers = accounts.lookupUsers(lookupUsersParameter);

        UsersTeam usersTeam = accounts.getUsersTeam(lookupUsers.results.get(0).id);

        assertNotNull(usersTeam);
        // Integration test user is and should not be part of a non-default team
        assertEquals(0, usersTeam.results.size());
    }

    @Disabled("Need to find a way to reliably get a user with a team.")
    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_EAST"}, mode = EnumSource.Mode.EXCLUDE)
    public void setRoleTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();

        LookupUsersParameter lookupUsersParameter = new LookupUsersParameter.Builder()
            .setUsername("saucerest-java-integration-test-user")
            .setRoles(Roles.MEMBER)
            .build();

        LookupUsers lookupUsers = accounts.lookupUsers(lookupUsersParameter);

        List<Result> validResults = lookupUsers.results.stream()
            .filter(r -> r.teams.size() > 0)
            .collect(Collectors.toList());

        Result result = validResults.stream()
            .skip(new Random().nextInt(validResults.size()))
            .findFirst()
            .get();

        User user = accounts.getUser(result.id);
        CreateTeam createTeam = accounts.createTeam("001-" + RandomStringUtils.randomAlphabetic(8), 0, "Test team created as part of integration tests");

        // Assign fetched user to newly created team
        SetTeam setTeam = accounts.setUsersTeam(user.id, createTeam.id);
        assertEquals(createTeam.id, setTeam.team.id);

        user = accounts.setTeamAdmin(user.id);
        assertTrue(user.roles.get(0).role.equals(Roles.TEAMADMIN.getValue()));

        user = accounts.setAdmin(user.id);
        assertTrue(user.roles.get(0).role.equals(Roles.ORGADMIN.getValue()));

        user = accounts.setMember(user.id);
        assertTrue(user.roles.get(0).role.equals(Roles.MEMBER.getValue()));

        accounts.deleteTeam(createTeam.id);

        assertThrows(SauceException.class, () -> {
            accounts.getSpecificTeam(createTeam.id);
        });
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_EAST"}, mode = EnumSource.Mode.EXCLUDE)
    public void deactivateUserTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();

        User user = createTestUser(accounts);

        if (user.id != null) {
            assertTrue(user.isActive);
            User deactivatedUser = accounts.deactivateUser(user.id);
            assertFalse(deactivatedUser.isActive);
        } else {
            fail("Test user was not created");
        }
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_EAST"}, mode = EnumSource.Mode.EXCLUDE)
    public void activateUserTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();

        User user = createTestUser(accounts);

        if (user.id != null) {
            assertTrue(user.isActive);
            User deactivatedUser = accounts.deactivateUser(user.id);
            assertFalse(deactivatedUser.isActive);
            User activatedUser = accounts.activateUser(user.id);
            assertTrue(activatedUser.isActive);
        } else {
            fail("Test user was not created");
        }
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_EAST"}, mode = EnumSource.Mode.EXCLUDE)
    public void resetAccessKeyTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();

        User user = createTestUser(accounts);

        if (user.id != null) {
            String oldAccessKey = user.accessKey;
            List<User> updatedUser = accounts.resetAccessKey(user.id);
            String newAccessKey = updatedUser.get(0).accessKey;
            assertNotEquals(oldAccessKey, newAccessKey);
        } else {
            fail("Test user was not created");
        }
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_EAST"}, mode = EnumSource.Mode.EXCLUDE)
    public void getAccessKeyTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();

        User user = createTestUser(accounts);

        if (user.id != null) {
            String accessKey = accounts.getAccessKey(user.id).accessKey;
            assertTrue(accessKey.length() > 0);
        } else {
            fail("Test user was not created");
        }
    }
}