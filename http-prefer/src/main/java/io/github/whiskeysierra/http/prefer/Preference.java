package io.github.whiskeysierra.http.prefer;

import javax.annotation.Nullable;
import java.util.Map;

public interface Preference<T> {

    Definition<T> getDefinition();

    @Nullable
    T getValue();

    // TODO @Immutable
    Map<String, String> getParameters();

}
