package io.github.whiskeysierra.http.prefer;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

final class HandlingDefinition implements Definition<Handling> {

    private final Map<String, Handling> mapping;

    HandlingDefinition() {
        final Map<String, Handling> mapping = new HashMap<>(3);

        mapping.put("strict", Handling.STRICT);
        mapping.put("lenient", Handling.LENIENT);

        this.mapping = Collections.unmodifiableMap(mapping);
    }

    @Override
    public String getName() {
        return "handling";
    }

    @Override
    public Handling parse(final String value) {
        @Nullable final Handling handling = mapping.get(value);

        if (handling == null) {
            throw new IllegalArgumentException("Unknown handling=" + value);
        }

        return handling;
    }

    @Override
    public String render(final Handling value) {
        return value.name().toLowerCase(Locale.ROOT);
    }

}
