package io.github.whiskeysierra.http.prefer;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

final class DefaultPrefer implements Prefer {

    private final Map<String, RawPreference> preferences;
    private final Map<String, Preference<?>> applied = new TreeMap<>(String::compareToIgnoreCase);

    DefaultPrefer(final Map<String, RawPreference> preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean contains(final Definition<?> definition) {
        final String name = definition.getName();
        return preferences.containsKey(name);
    }

    @Override
    public <T> Preference<T> get(final Definition<T> definition) {
        final String name = definition.getName();
        @Nullable final RawPreference raw = preferences.get(name);

        if (raw == null) {
            throw new IllegalArgumentException();
        }

        final T value = definition.parse(raw.getValue());
        final Map<String, String> parameters = raw.getParameters();

        return new DefaultPreference<>(definition, value, parameters);
    }

    @Override
    public boolean applies(final Definition<?> definition) {
        if (contains(definition)) {
            applied.put(definition.getName(), get(definition));
            return true;
        }

        return false;
    }

    @Override
    public String applied() {
        // TODO re-use
        return new PreferenceAppliedRenderer().render(applied);
    }

    @Override
    public String toString() {
        // TODO re-use
        return new PreferRenderer().render(preferences);
    }

    public static Prefer parse(final Collection<String> values) {
        // TODO re-use
        final PreferParser parser = new PreferParser();
        final Map<String, RawPreference> preferences = parser.parse(values);
        return new DefaultPrefer(preferences);
    }

}
