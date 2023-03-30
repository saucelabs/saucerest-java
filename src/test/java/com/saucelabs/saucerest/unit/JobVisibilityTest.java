package com.saucelabs.saucerest.unit;

import com.saucelabs.saucerest.JobVisibility;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JobVisibilityTest {

    @Test
    public void testValues() {
        assertAll("JobVisibility values",
            () -> assertSame(JobVisibility.PUBLIC, JobVisibility.valueOf("PUBLIC")),
            () -> assertSame(JobVisibility.PUBLIC_RESTRICTED, JobVisibility.valueOf("PUBLIC_RESTRICTED")),
            () -> assertSame(JobVisibility.SHARE, JobVisibility.valueOf("SHARE")),
            () -> assertSame(JobVisibility.TEAM, JobVisibility.valueOf("TEAM")),
            () -> assertSame(JobVisibility.PRIVATE, JobVisibility.valueOf("PRIVATE"))
        );
    }

    @Test
    public void testValue() {
        assertAll("JobVisibility value",
            () -> assertEquals("public", JobVisibility.PUBLIC.value),
            () -> assertEquals("public restricted", JobVisibility.PUBLIC_RESTRICTED.value),
            () -> assertEquals("share", JobVisibility.SHARE.value),
            () -> assertEquals("team", JobVisibility.TEAM.value),
            () -> assertEquals("private", JobVisibility.PRIVATE.value)
        );
    }
}