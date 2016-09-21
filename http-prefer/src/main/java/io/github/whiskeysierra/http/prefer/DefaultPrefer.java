package io.github.whiskeysierra.http.prefer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.joining;

final class DefaultPrefer implements Prefer {

    private static final DefaultPrefer NONE = new DefaultPrefer(emptyMap());

    private final Map<String, String> preferences;

    DefaultPrefer(final Map<String, String> preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean contains(final String name) {
        return preferences.containsKey(name);
    }

    @Override
    public String get(final String name) {
        return preferences.get(name);
    }

    @Override
    public Map<String, String> getParameters(final String name) {
        return emptyMap(); // TODO
    }

    @Override
    public boolean apply(final String name) {
        return false;
    }

    @Override
    public String applied() {
        return null;
    }

    @Override
    public String toString() {
        return preferences.entrySet().stream()
                .map(e -> e.getValue() == null ? e.getKey() : e.getKey() + "=" + e.getValue())
                .collect(joining(", "));
    }

    static Prefer parse(final Collection<String> values) {
        if (values.isEmpty()) {
            return NONE;
        }

        final Map<String, String> preferences = new CaseInsensitiveMap<>();

        values.stream().filter(Objects::nonNull).forEach(value -> {
            final String[] parts = value.trim().split(";", 2);
            final String first = parts[0].trim();
            final String second = parts.length == 2 ? parts[1].trim() : "";

            if (first.isEmpty() && second.isEmpty()) {
                return;
            }

            parseInto(first, preferences);
        });

        return new DefaultPrefer(unmodifiableMap(preferences));
    }

    // TODO use pre-compiled patterns
    // TODO find a better way than modifying the parameter
    private static void parseInto(final String value, final Map<String, String> preferences) {
        if (value.isEmpty()) {
            return;
        }

        Arrays.stream(value.split(",")).forEach(preference -> {
            final String[] parts = preference.split("=", 2);
            final String token = parseToken(parts);
            final String word = parseWord(parts);
            preferences.putIfAbsent(token, word);
        });
    }

    private static String parseToken(final String[] parts) {
        return parts[0].trim();
    }

    /**
     * <blockquote>
     * Empty or zero-length values on both the preference token and within parameters are equivalent to no value being
     * specified at all.
     * </blockquote>
     *
     * @param parts an array holding a key and potentially a value, i.e. either one or two values
     * @return the unquoted value if present, or an empty string otherwise
     * @see <a href="https://tools.ietf.org/html/rfc7240#section-2">Section 2 of [RFC7240]</a>
     */
    private static String parseWord(final String[] parts) {
        return parts.length == 2 ? unquote(parts[1].trim()) : null;
    }

    private static String unquote(final String word) {
        return emptyToNull(word.length() < 2 ?
                word :
                // TODO parse quoted values correctly
                word.startsWith("\"") && word.endsWith("\"") ?
                        word.substring(1, word.length() - 1) :
                        word);
    }

    private static String emptyToNull(final String s) {
        return s == null || s.isEmpty() ? null : s;
    }

}
