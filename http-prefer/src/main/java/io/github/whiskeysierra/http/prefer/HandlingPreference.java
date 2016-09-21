package io.github.whiskeysierra.http.prefer;

final class HandlingPreference implements Preference<Handling> {
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
                return null; // TODO throw?
        }
    }
}
