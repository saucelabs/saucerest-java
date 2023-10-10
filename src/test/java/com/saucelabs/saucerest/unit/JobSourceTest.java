package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.*;

import com.saucelabs.saucerest.JobSource;
import org.junit.jupiter.api.Test;

public class JobSourceTest {
    @Test
    public void testValues() {
        assertAll("JobSource values",
            () -> assertSame(JobSource.RDC, JobSource.values()[0]),
            () -> assertSame(JobSource.VDC, JobSource.values()[1])
        );
        assertEquals(2, JobSource.values().length);
    }
}