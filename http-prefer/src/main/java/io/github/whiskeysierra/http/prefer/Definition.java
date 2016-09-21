package io.github.whiskeysierra.http.prefer;

public interface Definition<T> {

    String getName();

    T parse(final String value);

}
