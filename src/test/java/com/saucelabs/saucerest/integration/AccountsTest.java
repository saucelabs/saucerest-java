package com.saucelabs.saucerest.integration;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceException;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.Accounts;
import com.saucelabs.saucerest.model.accounts.*;
import com.saucelabs.saucerest.model.realdevices.Concurrency;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class AccountsTest {
    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void lookupTeamsWithoutNameTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();

        LookupTeams lookupTeams = accounts.lookupTeams();

        assertNotNull(lookupTeams);
    }

    @ParameterizedTest
    @EnumSource(DataCenter.class)
    public void lookupTeamsWithNameTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();

        LookupTeams lookupTeams = accounts.lookupTeams("NotExisting");

        assertNotNull(lookupTeams);
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

            assertNotNull(team);
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

        accounts.deleteTeam(createTeam.id);
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
        accounts.deleteTeam(createTeam.id);
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
        accounts.deleteTeam(createTeam.id);
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

        // TODO: after create user endpoint is implemented expand test to create a user and add it to the team and then reset the access key
        CreateTeam createTeam = accounts.createTeam("000" + RandomStringUtils.randomAlphabetic(12), new Settings.Builder().setVirtualMachines(0).build(), RandomStringUtils.randomAlphabetic(8));

        List<ResetAccessKeyForTeam> resetAccessKeyForTeam = accounts.resetAccessKeyForTeam(createTeam.id);

        assertNotNull(resetAccessKeyForTeam);
        assertEquals(0, resetAccessKeyForTeam.size());
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

        CreateUser createUser = new CreateUser.Builder()
            .setEmail(RandomStringUtils.randomAlphabetic(8) + "@example.com")
            .setFirstName(RandomStringUtils.randomAlphabetic(8))
            .setLastName(RandomStringUtils.randomAlphabetic(8))
            .setPassword(RandomStringUtils.randomNumeric(4) + RandomStringUtils.randomAlphabetic(4) + "!$%" + "Aa")
            .setOrganization(accounts.getOrganization().results.get(0).id)
            .setRole(Roles.MEMBER)
            .setUserName("saucerest-java-integration-test-user-" + RandomStringUtils.randomAlphabetic(8))
            .build();

        User user = accounts.createUser(createUser);

        assertNotNull(user);
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_EAST"}, mode = EnumSource.Mode.EXCLUDE)
    public void updateUserTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();

        LookupUsers lookupUsers = accounts.lookupUsers();
        User user;
        Random rand = new Random();

        List<Result> validResults = lookupUsers.results.stream()
            .filter(r -> r.username.startsWith("saucerest-java-integration-test-user"))
            .collect(Collectors.toList());

        Result result = validResults.stream()
            .skip(rand.nextInt(validResults.size()))
            .findFirst()
            .get();

        user = accounts.getUser(result.id);

        String timeStamp = String.valueOf(new Random(System.currentTimeMillis()).nextInt()).replace("-", "");

        UpdateUser updateUser = new UpdateUser.Builder()
            .setUserID(user.id)
            .setFirstName("Updated " + timeStamp)
            .setLastName("Updated " + timeStamp)
            .setPhone("+123456789")
            .build();

        User updatedUser = accounts.updateUser(updateUser);

        assertEquals("Updated " + timeStamp, updatedUser.firstName);
        assertEquals("Updated " + timeStamp, updatedUser.lastName);
        assertEquals(user.phone, updatedUser.phone);
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_EAST"}, mode = EnumSource.Mode.EXCLUDE)
    public void partiallyUpdateUserTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();

        LookupUsers lookupUsers = accounts.lookupUsers();
        User user;
        Random rand = new Random();

        List<Result> validResults = lookupUsers.results.stream()
            .filter(r -> r.username.startsWith("saucerest-java-integration-test-user"))
            .collect(Collectors.toList());

        Result result = validResults.stream()
            .skip(rand.nextInt(validResults.size()))
            .findFirst()
            .get();

        user = accounts.getUser(result.id);

        String timeStamp = String.valueOf(new Random(System.currentTimeMillis()).nextInt()).replace("-", "");

        UpdateUser updateUser = new UpdateUser.Builder()
            .setUserID(user.id)
            .setFirstName("Updated " + timeStamp)
            .build();

        User updatedUser = accounts.partiallyUpdateUser(updateUser);

        assertEquals("Updated " + timeStamp, updatedUser.firstName);
    }

    @ParameterizedTest
    @EnumSource(value = DataCenter.class, names = {"US_EAST"}, mode = EnumSource.Mode.EXCLUDE)
    public void getUserConcurrencyTest(DataCenter dataCenter) throws IOException {
        SauceREST sauceREST = new SauceREST(dataCenter);
        Accounts accounts = sauceREST.getAccounts();

        UserConcurrency userConcurrency = accounts.getUserConcurrency(sauceREST.getUsername());
        Concurrency realDeviceConcurrency = sauceREST.getRealDevices().getConcurrency();

        assertNotNull(userConcurrency);
        assertNotNull(realDeviceConcurrency);
    }
}