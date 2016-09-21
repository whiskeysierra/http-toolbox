package io.github.whiskeysierra.http.prefer;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

import static java.util.Collections.singletonList;

public interface Prefer {

    Preference<Void> RESPOND_ASYNC = new RespondAsyncPreference();
    Preference<Return> RETURN = new ReturnPreference();
    Preference<Integer> WAIT = new WaitPreference();
    Preference<Handling> HANDLING = new HandlingPreference();

    default boolean contains(final Preference<?> preference) {
        return contains(preference.getName());
    }

    boolean contains(final String preference);

    default <T> T get(final Preference<T> preference) {
        return preference.parse(get(preference.getName()));
    }

    // TODO null vs empty string
    String get(final String preference);

    default boolean apply(final Preference<?> preference) {
        return apply(preference.getName());
    }

    boolean apply(final String preference);

    // TODO single vs. multiple headers
    String applied();

    static Prefer valueOf(@Nullable final String value) {
        return valueOf(singletonList(value));
    }

    static Prefer valueOf(final Collection<String> values) {
        return DefaultPrefer.parse(values);
    }

}
