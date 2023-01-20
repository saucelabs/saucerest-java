package com.saucelabs.saucerest.integration;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.Accounts;
import com.saucelabs.saucerest.model.accounts.LookupTeams;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
