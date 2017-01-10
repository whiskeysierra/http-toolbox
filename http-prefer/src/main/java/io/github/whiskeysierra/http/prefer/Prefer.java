package io.github.whiskeysierra.http.prefer;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

import static java.util.Collections.singletonList;

public interface Prefer {

    Definition<Void> RESPOND_ASYNC = new RespondAsyncDefinition();
    Definition<Return> RETURN = new ReturnDefinition();
    Definition<Integer> WAIT = new WaitDefinition();
    Definition<Handling> HANDLING = new HandlingDefinition();

    default boolean contains(final Definition<?> definition) {
        return contains(definition.getName());
    }

    boolean contains(final String name);

    // TODO nullable or throw?
    default <T> Preference<T> get(final Definition<T> definition) {
        final String name = definition.getName();
        final String value = get(name);

        if (value == null) {
            return null;
        }

        final T parsed = definition.parse(value);
        final Map<String, String> parameters = getParameters(name);

        return new Preference<T>() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public T getValue() {
                return parsed;
            }

            @Override
            public Map<String, String> getParameters() {
                return parameters;
            }
        };
    }

    // TODO nullable
    String get(final String name);

    Map<String, String> getParameters(final String name);

    // TODO test
    default boolean apply(final Definition<?> definition) {
        return apply(definition.getName());
    }

    // TODO test
    <T> boolean apply(final Definition<T> definition, final T value);

    // TODO test
    boolean apply(final String name);

    // TODO apply(String, String)?

    // TODO test
    // TODO single vs. multiple headers
    String applied();

    static Prefer valueOf(@Nullable final String value) {
        return valueOf(singletonList(value));
    }

    static Prefer valueOf(final Collection<String> values) {
        return DefaultPrefer.parse(values);
    }

}
