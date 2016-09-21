package io.github.whiskeysierra.http.prefer;

import java.util.Map;

public interface Preference<T> {

    String getName();

    // TODO nullable?
    T getValue();

    // TODO unmodifiable
    Map<String, String> getParameters();

}
