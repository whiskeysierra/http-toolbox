package io.github.whiskeysierra.http.prefer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
final class ValuelessDefinition implements Definition<Void> {

    private final String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Void parse(final String value) {
        return null;
    }

    @Override
    public String render(final Void value) {
        return null;
    }

}
