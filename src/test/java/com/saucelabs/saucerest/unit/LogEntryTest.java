package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.saucelabs.saucerest.LogEntry;
import org.junit.jupiter.api.Test;

public class LogEntryTest {

    @Test
    public void testConstructorAndGetters() {
        String time = "2022-05-05T12:34:56Z";
        String level = "INFO";
        String message = "Application started";
        LogEntry logEntry = new LogEntry(time, level, message);
        assertEquals(time, logEntry.getTime());
        assertEquals(level, logEntry.getLevel());
        assertEquals(message, logEntry.getMessage());
    }

    @Test
    public void testToString() {
        String time = "2022-05-05T12:34:56Z";
        String level = "INFO";
        String message = "Application started";
        LogEntry logEntry = new LogEntry(time, level, message);
        assertEquals(time + " " + level + " " + message, logEntry.toString());
    }
}