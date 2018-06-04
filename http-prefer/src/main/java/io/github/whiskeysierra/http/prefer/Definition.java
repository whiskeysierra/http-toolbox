package io.github.whiskeysierra.http.prefer;

import javax.annotation.Nullable;

public interface Definition<T> {

    String getName();

    T parse(final String value);

    @Nullable
    String render(final T value);

}
