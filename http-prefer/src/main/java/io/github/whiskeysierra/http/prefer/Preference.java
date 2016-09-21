package io.github.whiskeysierra.http.prefer;

public interface Preference<T> {

    String getName();

    T parse(final String value);

}
