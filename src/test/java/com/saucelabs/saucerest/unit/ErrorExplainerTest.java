package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.saucelabs.saucerest.ErrorExplainers;
import org.junit.jupiter.api.Test;

public class ErrorExplainerTest {
    @Test
    void errorMessageBuilder_shouldJoinTwoStrings() {
        String errorReason = "error reason";
        String errorExplanation = "error explanation";
        String expected = errorReason + System.lineSeparator() + errorExplanation;
        String actual = ErrorExplainers.errorMessageBuilder(errorReason, errorExplanation);
        assertEquals(expected, actual);
    }
}