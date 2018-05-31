package io.github.whiskeysierra.http.prefer;

import javax.annotation.Nullable;
import java.util.Map;

import static io.github.whiskeysierra.http.prefer.Grammar.TOKEN;
import static java.util.stream.Collectors.joining;

class PreferenceAppliedRenderer {

    String render(final Map<String, Preference<?>> preferences) {
        return preferences.entrySet().stream()
                .map(this::renderPreference)
                .collect(joining(", "));
    }

    private String renderPreference(final Map.Entry<String, Preference<?>> entry) {
        final String name = entry.getKey();

        final Preference<?> preference = entry.getValue();
        final String value = renderValue(preference);

        return name + value;
    }

    private <T> String renderValue(final Preference<T> preference) {
        @Nullable final String value = preference.getDefinition().render(preference.getValue());
        return value == null ? "" : "=" + quote(value);
    }

    private String quote(final String value) {
        return TOKEN.matcher(value).matches() ? value : "\"" + value + "\"";
    }

}
