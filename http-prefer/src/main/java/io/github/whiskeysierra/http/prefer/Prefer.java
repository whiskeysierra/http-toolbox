package io.github.whiskeysierra.http.prefer;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

import static java.util.Collections.singleton;

public interface Prefer {

    String PREFER = "Prefer";
    String PREFERENCE_APPLIED = "Preference-Applied";

    Definition<Void> RESPOND_ASYNC = new RespondAsyncDefinition();
    Definition<Return> RETURN = new ReturnDefinition();
    Definition<Integer> WAIT = new WaitDefinition();
    Definition<Handling> HANDLING = new HandlingDefinition();

    boolean contains(final Definition<?> definition);

    default <T> boolean contains(final Definition<T> definition, final T value) {
        return contains(definition) && Objects.equals(get(definition).getValue(), value);
    }

    <T> Preference<T> get(final Definition<T> definition) throws IllegalArgumentException;

    boolean apply(final Definition<?> definition);

    default <T> boolean apply(final Preference<T> preference) {
        return apply(preference.getDefinition(), preference.getValue());
    }

    default <T> boolean apply(final Definition<T> definition, final T value) {
        if (contains(definition, value)) {
            apply(definition);
            return true;
        }

        return false;
    }

    default <T> void apply(final Definition<T> definition, final Consumer<Preference<T>> consumer) {
        if (apply(definition)) {
            consumer.accept(get(definition));
        }
    }

    default <T> void apply(final Definition<T> definition, final T value, final Consumer<Preference<T>> consumer) {
        if (apply(definition, value)) {
            consumer.accept(get(definition));
        }
    }

    String applied();

    static Prefer valueOf(@Nullable final String value) {
        return valueOf(singleton(value));
    }

    static Prefer valueOf(final Collection<String> values) {
        return DefaultPrefer.parse(values);
    }

}
