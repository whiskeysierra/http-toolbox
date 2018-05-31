package io.github.whiskeysierra.http.prefer;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Matcher;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

class PreferParser {

    Map<String, RawPreference> parse(final Collection<String> values) {
        if (values.isEmpty()) {
            return emptyMap();
        }

        final Map<String, RawPreference> preferences = new CaseInsensitiveMap<>();

        values.stream()
                .filter(Objects::nonNull)
                .filter(not(String::isEmpty))
                .forEach(value -> parseInto(value, preferences));

        return unmodifiableMap(preferences);
    }

    private static <T> Predicate<T> not(final Predicate<T> predicate) {
        return predicate.negate();
    }

    // TODO find a better way than modifying the parameter
    private static void parseInto(final String value, final Map<String, RawPreference> result) {

        String separator = "";
        final Matcher matcher = Grammar.PREFERENCE.matcher(value.trim());

        while (matcher.find() ) {
            if (matcher.group(1) != null) {
                separator = matcher.group(1);
            } else if (separator != null) {
                final String name = matcher.group(2);
                // RFC 7240:
                // If any preference is specified more than once, only the first instance is to be
                // considered. All subsequent occurrences SHOULD be ignored without signaling
                // an error or otherwise altering the processing of the request.
                if (!result.containsKey(name)) {
                    final String preferenceValue = parseWord(matcher.group(3));
                    final String group = matcher.group(4);
                    final Map<String, String> parameters2 =
                            group == null || group.isEmpty() ?
                                    emptyMap() :
                                    parseParameters(group);

                    result.put(name, new RawPreference(name, preferenceValue, parameters2));
                }

                separator = null;
            } else {
                return;
            }
        }

    }

    // TODO find a better way than modifying the parameter
    private static Map<String, String> parseParameters(final String parameters) {
        final Map<String, String> result = new CaseInsensitiveMap<>();
        String separator = "";
        int start = 0;
        final Matcher matcher = Grammar.PARAMETER.matcher(parameters.trim());
        while (matcher.find() && matcher.start() == start) {
            start = matcher.end();
            if (matcher.group(1) != null) {
                separator = matcher.group(1);
            } else if (separator != null) {
                final String name = matcher.group(2);// TODO? .toLowerCase(Locale.ROOT);
                // We have to keep already existing parameters.
                if (!result.containsKey(name)) {
                    result.put(name, parseWord(matcher.group(3)));
                }
                separator = null;
            } else {
                return null;
            }
        }
        return matcher.hitEnd() ? unmodifiableMap(result) : null;
    }

    private static String parseWord(final String value) {
        if (value == null) {
            return null;
        }
        String result = value;
        if (value.startsWith("\"")) {
            result = value.substring(1, value.length() - 1);
        }
        // Unquote backslash-quoted characters.
        if (result.indexOf('\\') >= 0) {
            result = result.replaceAll("\\\\(.)", "$1");
        }
        return emptyToNull(result);
    }

    private static String emptyToNull(final String s) {
        return s == null || s.isEmpty() ? null : s;
    }

}
