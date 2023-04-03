package com.saucelabs.saucerest.unit;

import com.saucelabs.saucerest.Helper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HelperTest {

    @Test
    void testGetResourceFileAsStringWithValidFile() throws IOException {
        // Test for valid file
        Helper helper = new Helper();
        String fileName = "/testfile.txt";
        String expectedContent = "This is a test file.\nIt has multiple lines.";
        String actualContent = helper.getResourceFileAsString(fileName);
        assertEquals(expectedContent, actualContent);
    }

    @Test
    void testGetResourceFileAsStringWithNonexistentFile() {
        // Test for nonexistent file
        Helper helper = new Helper();
        String fileName = "/nonexistentfile.txt";
        assertThrows(NullPointerException.class, () -> helper.getResourceFileAsString(fileName));
    }

    @Test
    void testGetResourceFileAsStringWithNullFileName() {
        // Test for null file name
        Helper helper = new Helper();
        String fileName = null;
        assertThrows(IllegalArgumentException.class, () -> helper.getResourceFileAsString(fileName));
    }

    @Test
    void testGetResourceFileAsStringWithEmptyFileName() {
        // Test for empty file name
        Helper helper = new Helper();
        String fileName = "";
        assertThrows(IllegalArgumentException.class, () -> helper.getResourceFileAsString(fileName));
    }
}