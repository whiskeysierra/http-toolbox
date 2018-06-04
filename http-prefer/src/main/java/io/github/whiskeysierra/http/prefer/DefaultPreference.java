package io.github.whiskeysierra.http.prefer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor
@Getter
@FieldDefaults(level = PRIVATE, makeFinal = true)
class DefaultPreference<T> implements Preference<T> {

    Definition<T> definition;
    T value;
    Map<String, String> parameters;

}
