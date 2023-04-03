package com.saucelabs.saucerest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
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
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty.");
        }

        try (InputStream is = Objects.requireNonNull(getClass().getResource(fileName), "File not found: " + fileName).openStream()) {

            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (IOException e) {
            String errorMessage = String.format("Error reading file %s: %s", fileName, e.getMessage());
            throw new IOException(errorMessage, e);
        }
    }
}