package io.github.whiskeysierra.http.prefer;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

final class ReturnDefinition implements Definition<Return> {

    private final Map<String, Return> mapping;

    ReturnDefinition() {
        final Map<String, Return> mapping = new HashMap<>(3);

        mapping.put("minimal", Return.MINIMAL);
        mapping.put("representation", Return.REPRESENTATION);

        this.mapping = Collections.unmodifiableMap(mapping);
    }

    @Override
    public String getName() {
        return "return";
    }

    @Override
    public Return parse(final String value) {
        @Nullable final Return _return = mapping.get(value);

        if (_return == null) {
            throw new IllegalArgumentException("Unknown return=" + value);
        }

        return _return;
    }

    @Override
    public String render(final Return value) {
        return value.name().toLowerCase(Locale.ROOT);
    }

}
