package io.github.whiskeysierra.http.prefer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
final class StringDefinition implements Definition<String> {

    private final String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String parse(final String value) {
        return value;
    }

    @Override
    public String render(final String value) {
        return value;
    }

}
