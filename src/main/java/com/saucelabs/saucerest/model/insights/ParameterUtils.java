package com.saucelabs.saucerest.model.insights;

import java.util.Arrays;
import java.util.function.Function;

final class ParameterUtils {
    private ParameterUtils() {
    }

    /** Maps an array of enum constants to their query-parameter string values, or {@code null} if the input is {@code null}. */
    static <T> String[] valuesOf(T[] values, Function<T, String> mapper) {
        if (values == null) {
            return null;
        }

        return Arrays.stream(values).map(mapper).toArray(String[]::new);
    }
}
