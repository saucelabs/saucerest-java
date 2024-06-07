package com.saucelabs.saucerest.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.saucelabs.saucerest.Helper;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class HelperTest {

    @Test
    void testGetResourceFileAsStringWithValidFile() throws IOException {
        // Test for valid file
        String fileName = "/testfile.txt";
        String expectedContent = "This is a test file.\nIt has multiple lines.";
        String actualContent = Helper.getResourceFileAsString(fileName);
        assertEquals(expectedContent, actualContent);
    }

    @Test
    void testGetResourceFileAsStringWithNonexistentFile() {
        // Test for nonexistent file
        String fileName = "/nonexistentfile.txt";
        assertThrows(NullPointerException.class, () -> Helper.getResourceFileAsString(fileName));
    }

    @Test
    void testGetResourceFileAsStringWithNullFileName() {
        // Test for null file name
        String fileName = null;
        assertThrows(IllegalArgumentException.class, () -> Helper.getResourceFileAsString(fileName));
    }

    @Test
    void testGetResourceFileAsStringWithEmptyFileName() {
        // Test for empty file name
        String fileName = "";
        assertThrows(IllegalArgumentException.class, () -> Helper.getResourceFileAsString(fileName));
    }
}