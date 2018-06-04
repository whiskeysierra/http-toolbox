package io.github.whiskeysierra.http.prefer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HandlingDefinitionTest {

    @Test
    void parseStrict() {
        final Prefer unit = Prefer.valueOf("handling=strict");

        assertTrue(unit.contains(Prefer.HANDLING, Handling.STRICT));
    }

    @Test
    void parseLenient() {
        final Prefer unit = Prefer.valueOf("handling=lenient");

        assertTrue(unit.contains(Prefer.HANDLING, Handling.LENIENT));
    }

    @Test
    void parseUnsupported() {
        final Prefer unit = Prefer.valueOf("handling=lax");

        assertThrows(IllegalArgumentException.class, () -> unit.get(Prefer.HANDLING));
    }

    @Test
    void render() {
        final Prefer unit = Prefer.valueOf("handling=strict");


        assertEquals("handling=strict", unit.toString());
    }

    @Test
    void renderApplied() {
        final Prefer unit = Prefer.valueOf("handling=strict");

        unit.apply(Prefer.HANDLING);

        assertEquals("handling=strict", unit.applied());
    }

}
