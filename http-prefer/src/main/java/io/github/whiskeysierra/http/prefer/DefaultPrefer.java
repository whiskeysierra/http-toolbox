package io.github.whiskeysierra.http.prefer;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;

final class DefaultPrefer implements Prefer {

    private static final Pattern TOKEN = compile("(?:[-!#$%&'*+.^_`|~]|\\w)+");
    private static final Pattern QUOTED_STRING = compile("(?:\"(?:[\\t !#-\\[\\]-~\\x80-\\xFF]|(?:\\\\[\\t !-~\\x80-\\xFF]))*\")");
    private static final Pattern NAMED_VALUE = compile("(" + TOKEN + ")(?:\\s*=\\s*(" + TOKEN + "|" + QUOTED_STRING + "))?");
    private static final Pattern PREFERENCE = compile("\\s*(,\\s*)+|(?:" + NAMED_VALUE + "((?:\\s*;\\s*(?:" + NAMED_VALUE + ")?)*))");
    private static final Pattern PARAMETER = compile("\\s*(;\\s*)+|(?:" + NAMED_VALUE + ")");

    private static final DefaultPrefer NONE = new DefaultPrefer(emptyMap(), emptyMap());

    private final Map<String, String> preferences;
    private final Map<String, Map<String, String>> parameters;

    DefaultPrefer(final Map<String, String> preferences, final Map<String, Map<String, String>> parameters) {
        this.preferences = preferences;
        this.parameters = parameters;
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
        return parameters.getOrDefault(name, emptyMap());
    }

    @Override
    public <T> boolean apply(final Definition<T> definition, final T value) {
        return false;
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
                .map(entry -> {
                    final String name = entry.getKey();
                    final String value = entry.getValue();

                    final Map<String, String> parameters = getParameters(name);

                    final String p = parameters.isEmpty() ? "" : "; " + parameters.entrySet().stream()
                            .map(e -> e.getValue() == null ?
                                    e.getKey() :
                                    e.getKey() + "=" + (TOKEN.matcher(e.getValue()).matches() ? e.getValue() : "\"" + e.getValue() + "\""))
                            .collect(joining("; "));

                    if (value == null) {
                        return name + p;
                    } else {
                        return name + "=" + (TOKEN.matcher(value).matches() ? value : "\"" + value + "\"") + p;
                    }
                })
                .collect(joining(", "));
    }

    static Prefer parse(final Collection<String> values) {
        if (values.isEmpty()) {
            return NONE;
        }

        final Map<String, String> preferences = new CaseInsensitiveMap<>();
        final Map<String, Map<String, String>> parameters = new CaseInsensitiveMap<>();

        values.stream()
                .filter(Objects::nonNull)
                .filter(not(String::isEmpty))
                .forEach(value -> parseInto(value, preferences, parameters));

        return new DefaultPrefer(unmodifiableMap(preferences), parameters);
    }

    private static <T> Predicate<T> not(final Predicate<T> predicate) {
        return predicate.negate();
    }

    // TODO find a better way than modifying the parameter
    private static void parseInto(final String value, final Map<String, String> result,
            final Map<String, Map<String, String>> parameters) {

        String separator = "";
        final Matcher matcher = PREFERENCE.matcher(value.trim());

        while (matcher.find() ) {
            if (matcher.group(1) != null) {
                separator = matcher.group(1);
            } else if (separator != null) {
                final String name = matcher.group(2).toLowerCase(Locale.ROOT);
                // RFC 7240:
                // If any preference is specified more than once, only the first instance is to be
                // considered. All subsequent occurrences SHOULD be ignored without signaling
                // an error or otherwise altering the processing of the request.
                if (!result.containsKey(name)) {
                    final String preferenceValue = parseWord(matcher.group(3));
                    final Map<String, String> parameters2 =
                            matcher.group(4) == null || matcher.group(4).isEmpty() ?
                                    emptyMap() :
                                    parseParameters(matcher.group(4));

                    result.put(name, preferenceValue);
                    parameters.put(name, parameters2);
                }

                separator = null;
            } else {
                return;
            }
        }

        // TODO handle dangling, unmatched parts (using matcher.hitEnd)
    }

    // TODO find a better way than modifying the parameter
    private static Map<String, String> parseParameters(final String parameters) {
        final Map<String, String> result = new CaseInsensitiveMap<>();
        String separator = "";
        int start = 0;
        final Matcher matcher = PARAMETER.matcher(parameters.trim());
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
