package io.github.whiskeysierra.http.prefer;

public interface Definition<T> {

    String getName();

    T parse(final String value);

    String render(final T value);

    static Definition<String> string(final String name) {
        return new StringDefinition(name);
    }

    static Definition<Void> valueless(final String name) {
        return new ValuelessDefinition(name);
    }

}
