package io.github.whiskeysierra.http.prefer;

import javax.annotation.Nullable;
import java.util.Map;

import static io.github.whiskeysierra.http.prefer.Grammar.TOKEN;
import static java.util.stream.Collectors.joining;

class PreferRenderer {

    String render(final Map<String, RawPreference> preferences) {
        return preferences.entrySet().stream()
                .map(this::renderPreference)
                .collect(joining(", "));
    }

    private String renderPreference(final Map.Entry<String, RawPreference> entry) {
        final String name = entry.getKey();

        final RawPreference preference = entry.getValue();
        final String value = renderValue(preference.getValue());
        final String parameters = renderParameters(preference.getParameters());

        return name + value + parameters;
    }

    private String renderValue(@Nullable final String value) {
        return value == null ? "" : "=" + quote(value);
    }

    private String renderParameters(final Map<String, String> parameters) {
        if (parameters.isEmpty()) {
            return "";
        }

        return parameters.entrySet().stream()
                .map(this::renderParameter)
                .collect(joining("; ", "; ", ""));
    }

    private String renderParameter(final Map.Entry<String, String> entry) {
        return entry.getValue() == null ? entry.getKey() : entry.getKey() + "=" + quote(entry.getValue());
    }

    private String quote(final String value) {
        return TOKEN.matcher(value).matches() ? value : "\"" + value + "\"";
    }

}
