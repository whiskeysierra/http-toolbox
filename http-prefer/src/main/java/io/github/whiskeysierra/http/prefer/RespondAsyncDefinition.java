package io.github.whiskeysierra.http.prefer;

final class RespondAsyncDefinition implements Definition<Void> {

    @Override
    public String getName() {
        return "respond-async";
    }

    @Override
    public Void parse(final String value) {
        return null;
    }

}
