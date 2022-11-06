package com.saucelabs.saucerest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Helper {
    /**
     * Reads given resource file as a string.
     *
     * @param fileName path to the resource file
     * @return the file's contents
     * @throws IOException if read fails for any reason
     * @see <a href="https://stackoverflow.com/a/46613809">https://stackoverflow.com/a/46613809</a>
     */
    public String getResourceFileAsString(String fileName) throws IOException {
        try (InputStream is = getClass().getResource(fileName).openStream()) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }
}
