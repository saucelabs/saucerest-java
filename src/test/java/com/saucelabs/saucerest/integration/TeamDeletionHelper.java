package com.saucelabs.saucerest.integration;

import com.saucelabs.saucerest.api.AccountsEndpoint;
import com.saucelabs.saucerest.model.accounts.Result;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TeamDeletionHelper {
    private static final Logger logger = Logger.getLogger(TeamDeletionHelper.class.getName());

    public static void deleteTeamsWithPrefix(List<Result> teams, AccountsEndpoint accountsEndpoint) throws IOException {
        for (Result team : teams) {
            if (team.name.startsWith("000")) {
                deleteTeam(team, accountsEndpoint);
            }
        }
    }

    private static void deleteTeam(Result team, AccountsEndpoint accountsEndpoint) throws IOException {
        try (Response response = accountsEndpoint.deleteTeam(team.id)) {
            String responseCode = String.valueOf(response.code());

            if (responseCode.startsWith("2")) {
                logger.log(Level.INFO, "Deleted team " + team.name);
            } else {
                logger.log(Level.WARNING, "Failed to delete team " + team.name + " with response code " + responseCode);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to delete team " + team.name, e);
            throw e;
        }
    }
}