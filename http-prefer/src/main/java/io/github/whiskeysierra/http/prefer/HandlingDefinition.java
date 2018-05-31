package io.github.whiskeysierra.http.prefer;

import java.util.Locale;

final class HandlingDefinition implements Definition<Handling> {

    @Override
    public String getName() {
        return "handling";
    }

    @Override
    public Handling parse(final String value) {
        switch (value) {
            case "strict":
                return Handling.STRICT;
            case "lenient":
                return Handling.LENIENT;
            default:
                return null;
        }
    }

    @Override
    public String render(final Handling value) {
        return getName() + "=" + value.name().toLowerCase(Locale.ROOT);
    }

}
