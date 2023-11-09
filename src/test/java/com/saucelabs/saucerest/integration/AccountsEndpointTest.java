package com.saucelabs.saucerest.integration;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceException;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.AccountsEndpoint;
import com.saucelabs.saucerest.model.accounts.*;
import com.saucelabs.saucerest.model.realdevices.Concurrency;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import okhttp3.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@Disabled(
    "These tests are disabled because now accounts need to be verified before they can be deactivated.")
public class AccountsEndpointTest {

  private static User createTestUser(AccountsEndpoint accountsEndpoint) throws IOException {
    CreateUser createUser =
        new CreateUser.Builder()
            .setEmail(RandomStringUtils.randomAlphabetic(8) + "@saucelabs.com")
            .setFirstName(RandomStringUtils.randomAlphabetic(8))
            .setLastName(RandomStringUtils.randomAlphabetic(8))
            .setPassword(
                RandomStringUtils.randomNumeric(4)
                    + RandomStringUtils.randomAlphabetic(4)
                    + "!$%"
                    + "Aa")
            .setOrganization(accountsEndpoint.getOrganization().results.get(0).id)
            .setRole(Roles.MEMBER)
            .setUserName("sl-test-del-" + RandomStringUtils.randomAlphabetic(8))
            .build();

    return accountsEndpoint.createUser(createUser);
  }

  private static void createTeam(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();
    String teamName = "000" + RandomStringUtils.randomAlphabetic(12);

    Settings settings = new Settings.Builder().setVirtualMachines(0).build();

    accountsEndpoint.createTeam(teamName, settings, RandomStringUtils.randomAlphabetic(8));
  }

  @AfterAll
  public static void cleanup() throws IOException {
    for (DataCenter dataCenter : DataCenter.values()) {
      SauceREST sauceREST = new SauceREST(dataCenter);
      AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();
      List<Result> teams = accountsEndpoint.lookupTeams().results;

      TeamDeletionHelper.deleteTeamsWithPrefix(teams, accountsEndpoint);
    }
  }

  @BeforeAll
  public static void setup() throws IOException {
    for (DataCenter dataCenter : DataCenter.values()) {
      createTeam(dataCenter);
    }
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void lookupTeamsWithoutNameTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();
    LookupTeams lookupTeams = accountsEndpoint.lookupTeams();

    assertTrue(lookupTeams.count > 0);
    assertFalse(lookupTeams.results.isEmpty());
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void lookupTeamsWithNameTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();

    LookupTeams lookupTeams = accountsEndpoint.lookupTeams("NotExisting");

    assertEquals(0, lookupTeams.count);
    assertEquals(0, lookupTeams.results.size());
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void getSpecificTeamTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();
    LookupTeams lookupTeams = accountsEndpoint.lookupTeams();

    for (Result result : lookupTeams.results) {
      Team team = accountsEndpoint.getSpecificTeam(result.id);

      assertFalse(team.id.isEmpty());
      assertFalse(team.name.isEmpty());
    }
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void getSpecificTeamNotFoundTest(DataCenter dataCenter) {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();

    assertThrows(SauceException.NotFound.class, () -> accountsEndpoint.getSpecificTeam("1234"));
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void getOrganizationTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();

    Organizations organizations = accountsEndpoint.getOrganization();

    assertNotNull(organizations);
    assertEquals(1, organizations.count);
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void createTeamTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();
    String teamName = "000" + RandomStringUtils.randomAlphabetic(12);

    Settings settings = new Settings.Builder().setVirtualMachines(0).build();

    CreateTeam createTeam =
        accountsEndpoint.createTeam(teamName, settings, RandomStringUtils.randomAlphabetic(8));

    assertNotNull(createTeam);
    assertEquals(teamName, createTeam.name);

    try (Response response = accountsEndpoint.deleteTeam(createTeam.id)) {
      assertEquals(204, response.code());
    }
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void updateTeamTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();
    String teamName = "000" + RandomStringUtils.randomAlphabetic(12);

    Settings settings = new Settings.Builder().setVirtualMachines(0).build();

    CreateTeam createTeam =
        accountsEndpoint.createTeam(teamName, settings, RandomStringUtils.randomAlphabetic(8));

    assertNotNull(createTeam);
    assertEquals(teamName, createTeam.name);

    UpdateTeam updateTeam =
        accountsEndpoint.updateTeam(
            createTeam.id, "Updated" + teamName, settings, "Updated description");

    assertEquals("Updated description", updateTeam.description);
    assertEquals("Updated" + teamName, updateTeam.name);
    assertEquals(0, updateTeam.settings.virtualMachines);

    try (Response response = accountsEndpoint.deleteTeam(createTeam.id)) {
      assertEquals(204, response.code());
    }
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void partiallyUpdateTeamTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();
    String teamName = "000" + RandomStringUtils.randomAlphabetic(12);

    Settings settings = new Settings.Builder().setVirtualMachines(0).build();

    CreateTeam createTeam =
        accountsEndpoint.createTeam(teamName, settings, RandomStringUtils.randomAlphabetic(8));

    assertNotNull(createTeam);
    assertEquals(teamName, createTeam.name);

    UpdateTeam partiallyUpdateTeam = new UpdateTeam.Builder().setName("Updated" + teamName).build();

    UpdateTeam updateTeam =
        accountsEndpoint.partiallyUpdateTeam(createTeam.id, partiallyUpdateTeam);

    assertEquals("Updated" + teamName, updateTeam.name);

    try (Response response = accountsEndpoint.deleteTeam(createTeam.id)) {
      assertEquals(204, response.code());
    }
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void getTeamMembersTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();

    LookupTeams lookupTeams = accountsEndpoint.lookupTeams();
    TeamMembers teamMembers = accountsEndpoint.getTeamMembers(lookupTeams.results.get(0).id);

    assertNotNull(teamMembers);
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void resetAccessKeyTeam(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();
    CreateTeam createTeam =
        accountsEndpoint.createTeam(
            "000" + RandomStringUtils.randomAlphabetic(12),
            new Settings.Builder().setVirtualMachines(0).build(),
            RandomStringUtils.randomAlphabetic(8));
    User user1 = createTestUser(accountsEndpoint);
    User user2 = createTestUser(accountsEndpoint);
    accountsEndpoint.setUsersTeam(user1.id, createTeam.id);
    accountsEndpoint.setUsersTeam(user2.id, createTeam.id);
    String accessKeyUser1 = user1.accessKey;
    String accessKeyUser2 = user2.accessKey;

    List<ResetAccessKeyForTeam> resetAccessKeyForTeam =
        accountsEndpoint.resetAccessKeyForTeam(createTeam.id);

    assertEquals(2, resetAccessKeyForTeam.size());

    String resetAccessKey =
        resetAccessKeyForTeam.stream()
            .filter(r -> r.id.equals(user1.id))
            .map(r -> r.accessKey)
            .findFirst()
            .orElseThrow(() -> new AssertionError("Access key not found for user1"));

    assertNotEquals(
        accessKeyUser1, resetAccessKey, "Access key for user1 should not match reset access key");

    assertNotEquals(
        accessKeyUser1,
        resetAccessKeyForTeam.stream()
            .filter(r -> r.id.equals(user1.id))
            .findFirst()
            .get()
            .accessKey);
    assertNotEquals(
        accessKeyUser2,
        resetAccessKeyForTeam.stream()
            .filter(r -> r.id.equals(user2.id))
            .findFirst()
            .get()
            .accessKey);
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void lookupUsersWithParametersTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();

    LookupUsersParameter lookupUsersParameter =
        new LookupUsersParameter.Builder().setRoles(Roles.ORGADMIN).build();

    LookupUsers lookupUsers = accountsEndpoint.lookupUsers(lookupUsersParameter);

    assertNotNull(lookupUsers);
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void getUserTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();

    LookupUsers lookupUsers = accountsEndpoint.lookupUsers();
    User user = accountsEndpoint.getUser(lookupUsers.results.get(0).id);

    assertNotNull(user);
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void createUserTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();

    User user = createTestUser(accountsEndpoint);

    assertNotNull(user);
    assertFalse(user.id.isEmpty());
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void updateUserTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();
    User testUser = createTestUser(accountsEndpoint);
    User user = accountsEndpoint.getUser(testUser.id);

    UpdateUser updateUser =
        new UpdateUser.Builder()
            .setUserID(user.id)
            .setFirstName("Updated " + user.firstName)
            .setLastName("Updated " + user.lastName)
            .setPhone("+123456789")
            .build();

    User updatedUser = accountsEndpoint.updateUser(updateUser);

    assertAll(
        "User",
        () -> assertEquals("Updated " + user.firstName, updatedUser.firstName),
        () -> assertEquals("Updated " + user.lastName, updatedUser.lastName),
        () -> assertEquals("+123456789", updatedUser.phone));
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void partiallyUpdateUserTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();
    User testUser = createTestUser(accountsEndpoint);
    User user = accountsEndpoint.getUser(testUser.id);

    UpdateUser updateUser =
        new UpdateUser.Builder()
            .setUserID(user.id)
            .setFirstName("Updated " + user.firstName)
            .build();

    User updatedUser = accountsEndpoint.partiallyUpdateUser(updateUser);

    assertEquals("Updated " + user.firstName, updatedUser.firstName);
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void getUserConcurrencyTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();

    UserConcurrency userConcurrency = accountsEndpoint.getUserConcurrency(sauceREST.getUsername());
    Concurrency realDeviceConcurrency = sauceREST.getRealDevicesEndpoint().getConcurrency();

    assertNotNull(userConcurrency);
    assertNotNull(realDeviceConcurrency);
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void getUsersTeamTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();

    LookupUsersParameter lookupUsersParameter =
        new LookupUsersParameter.Builder().setUsername(sauceREST.getUsername()).build();

    LookupUsers lookupUsers = accountsEndpoint.lookupUsers(lookupUsersParameter);

    UsersTeam usersTeam = accountsEndpoint.getUsersTeam(lookupUsers.results.get(0).id);

    assertNotNull(usersTeam);
    assertEquals(1, usersTeam.results.size());
  }

  @Disabled("Need to find a way to reliably get a user with a team.")
  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void setRoleTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();

    LookupUsersParameter lookupUsersParameter =
        new LookupUsersParameter.Builder()
            .setUsername("sl-test-del")
            .setRoles(Roles.MEMBER)
            .build();

    LookupUsers lookupUsers = accountsEndpoint.lookupUsers(lookupUsersParameter);

    List<Result> validResults =
        lookupUsers.results.stream().filter(r -> !r.teams.isEmpty()).collect(Collectors.toList());

    Result result =
        validResults.stream().skip(new Random().nextInt(validResults.size())).findFirst().get();

    User user = accountsEndpoint.getUser(result.id);
    CreateTeam createTeam =
        accountsEndpoint.createTeam(
            "001-" + RandomStringUtils.randomAlphabetic(8),
            0,
            "Test team created as part of integration tests");

    // Assign fetched user to newly created team
    SetTeam setTeam = accountsEndpoint.setUsersTeam(user.id, createTeam.id);
    assertEquals(createTeam.id, setTeam.team.id);

    user = accountsEndpoint.setTeamAdmin(user.id);
    assertEquals((int) user.roles.get(0).role, Roles.TEAMADMIN.getValue());

    user = accountsEndpoint.setAdmin(user.id);
    assertEquals((int) user.roles.get(0).role, Roles.ORGADMIN.getValue());

    user = accountsEndpoint.setMember(user.id);
    assertEquals((int) user.roles.get(0).role, Roles.MEMBER.getValue());

    try (Response response = accountsEndpoint.deleteTeam(createTeam.id)) {
      assertTrue(response.isSuccessful());
    }

    assertThrows(SauceException.class, () -> accountsEndpoint.getSpecificTeam(createTeam.id));
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void deactivateUserTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();

    User user = createTestUser(accountsEndpoint);

    if (user.id != null) {
      assertTrue(user.isActive);
      User deactivatedUser = accountsEndpoint.deactivateUser(user.id);
      assertFalse(deactivatedUser.isActive);
    } else {
      fail("Test user was not created");
    }
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void activateUserTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();

    User user = createTestUser(accountsEndpoint);

    if (user.id != null) {
      assertTrue(user.isActive);
      User deactivatedUser = accountsEndpoint.deactivateUser(user.id);
      assertFalse(deactivatedUser.isActive);
      User activatedUser = accountsEndpoint.activateUser(user.id);
      assertTrue(activatedUser.isActive);
    } else {
      fail("Test user was not created");
    }
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void resetAccessKeyTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();

    User user = createTestUser(accountsEndpoint);

    if (user.id != null) {
      String oldAccessKey = user.accessKey;
      List<User> updatedUser = accountsEndpoint.resetAccessKey(user.id);
      String newAccessKey = updatedUser.get(0).accessKey;
      assertNotEquals(oldAccessKey, newAccessKey);
    } else {
      fail("Test user was not created");
    }
  }

  @ParameterizedTest
  @EnumSource(DataCenter.class)
  public void getAccessKeyTest(DataCenter dataCenter) throws IOException {
    SauceREST sauceREST = new SauceREST(dataCenter);
    AccountsEndpoint accountsEndpoint = sauceREST.getAccountsEndpoint();

    User user = createTestUser(accountsEndpoint);

    if (user.id != null) {
      String accessKey = accountsEndpoint.getAccessKey(user.id).accessKey;
      assertFalse(accessKey.isEmpty());
    } else {
      fail("Test user was not created");
    }
  }
}
