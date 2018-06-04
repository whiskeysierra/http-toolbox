package io.github.whiskeysierra.http.prefer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.regex.Matcher;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

class PreferParser {

    Map<String, RawPreference> parse(final Collection<String> values) {
        if (values.isEmpty()) {
            return emptyMap();
        }

        final Map<String, RawPreference> preferences = new TreeMap<>(String::compareToIgnoreCase);

        values.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(not(String::isEmpty))
                .forEach(value -> parseInto(value, preferences));

        return unmodifiableMap(preferences);
    }

    private static <T> Predicate<T> not(final Predicate<T> predicate) {
        return predicate.negate();
    }

    // TODO find a better way than modifying the parameter
    private static void parseInto(final String prefer, final Map<String, RawPreference> result) {
        final Matcher matcher = Grammar.PREFERENCE.matcher(prefer);

        int start = 0;

        while (matcher.find()) {
            if (matcher.start() != start) {
                throw new IllegalArgumentException();
            }

            start = matcher.end();

            final String name = matcher.group(1);
            if (!result.containsKey(name)) {
                final String value = parseWord(matcher.group(2));
                final Map<String, String> parameters = parseParameters(matcher.group(3));

                result.put(name, new RawPreference(value, parameters));
            }

            if (matcher.hitEnd()) {
                return;
            }
        }

        throw new IllegalArgumentException();
    }

    @Nonnull
    private static Map<String, String> parseParameters(final String parameters) {
        final Map<String, String> result = new TreeMap<>(String::compareToIgnoreCase);
        final Matcher matcher = Grammar.PARAMETER.matcher(parameters.trim());

        while (matcher.find()) {
            @Nullable final String name = matcher.group(1);
            // We have to keep already existing parameters.
            if (!result.containsKey(name)) {
                result.put(name, parseWord(matcher.group(2)));
            }
        }

        return unmodifiableMap(result);
    }

    @Nullable
    private static String parseWord(@Nullable final String value) {
        if (value == null) {
            return null;
        }
        String result = value;
        if (value.startsWith("\"")) {
            result = value.substring(1, value.length() - 1);
        }
        // Unquote backslash-quoted characters.
        if (result.indexOf('\\') >= 0) {
            // TODO compile and re-use pattern
            result = result.replaceAll("\\\\(.)", "$1");
        }
        return emptyToNull(result);
    }

    @Nullable
    private static String emptyToNull(@Nonnull final String s) {
        return s.isEmpty() ? null : s;
    }

}
