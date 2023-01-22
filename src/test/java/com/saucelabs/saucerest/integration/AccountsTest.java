package com.saucelabs.saucerest.integration;

import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceException;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.api.Accounts;
import com.saucelabs.saucerest.model.accounts.LookupTeams;
import com.saucelabs.saucerest.model.accounts.Result;
import com.saucelabs.saucerest.model.accounts.Team;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;

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
}
