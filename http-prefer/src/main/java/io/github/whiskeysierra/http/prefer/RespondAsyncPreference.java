package io.github.whiskeysierra.http.prefer;

final class RespondAsyncPreference implements Preference<Void> {

    @Override
    public String getName() {
        return "respond-async";
    }

    @Override
    public Void parse(final String value) {
        return null;
    }

}
