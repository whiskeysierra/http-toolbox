package io.github.whiskeysierra.http.prefer;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;

final class DefaultPrefer implements Prefer {

    private final Map<String, RawPreference> preferences;
    private final Map<String, Preference<?>> applied = new CaseInsensitiveMap<>();

    DefaultPrefer(final Map<String, RawPreference> preferences) {
        this.preferences = preferences;
    }

    @Override
    public boolean contains(final Definition<?> definition) {
        return preferences.containsKey(definition.getName());
    }

    @Override
    public <T> Preference<T> get(final Definition<T> definition) {
        return preferences.get(definition.getName()).toPreference(definition);
    }

    @Override
    public boolean apply(final Definition<?> definition) {
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
    public void applyTo(final BiConsumer<String, String> target) {
        if (applied.isEmpty()) {
            return;
        }

        target.accept("Preference-Applied", applied());
    }

    @Override
    public String toString() {
        // TODO re-use
        return new PreferRenderer().render(preferences);
    }

    public static Prefer parse(final Collection<String> values) {
        final PreferParser parser = new PreferParser();
        final Map<String, RawPreference> preferences = parser.parse(values);
        return new DefaultPrefer(preferences);
    }

}
