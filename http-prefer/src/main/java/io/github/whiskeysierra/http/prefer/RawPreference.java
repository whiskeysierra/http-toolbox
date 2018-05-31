package io.github.whiskeysierra.http.prefer;

import lombok.Value;

import javax.annotation.Nullable;
import java.util.Map;

@Value
final class RawPreference {

    String name;
    String value;

    // TODO @Immutable
    Map<String, String> parameters;

    <T> Preference<T> toPreference(final Definition<T> definition) {
        return new Preference<T>() {
            @Override
            public Definition<T> getDefinition() {
                return definition;
            }

            @Nullable
            @Override
            public T getValue() {
                return definition.parse(value);
            }

            @Override
            public Map<String, String> getParameters() {
                return parameters;
            }
        };
    }

}
