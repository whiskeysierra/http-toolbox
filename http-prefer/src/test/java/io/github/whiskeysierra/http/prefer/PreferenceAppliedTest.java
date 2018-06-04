package io.github.whiskeysierra.http.prefer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PreferenceAppliedTest {

    @Test
    void shouldQuote() {
        final Prefer unit = Prefer.valueOf("quote=\";\"");

        final Preference<String> quote = unit.get(new StringDefinition("quote"));

        // TODO doesn't belong here
        assertEquals(";", quote.getValue());

        unit.apply(quote);

        assertEquals("quote=\";\"", unit.applied());

    }

}
