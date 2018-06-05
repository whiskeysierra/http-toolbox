package io.github.whiskeysierra.http.prefer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReturnDefinitionTest {

    @Test
    void parseMinimal() {
        final Prefer unit = Prefer.valueOf("return=minimal");

        assertTrue(unit.contains(Prefer.RETURN, Return.MINIMAL));
    }


    @Test
    void parseRepresentation() {
        final Prefer unit = Prefer.valueOf("return=representation");

        assertTrue(unit.contains(Prefer.RETURN, Return.REPRESENTATION));
    }

    @Test
    void parseUnsupported() {
        final Prefer unit = Prefer.valueOf("return=medium");

        assertThrows(IllegalArgumentException.class, () -> unit.get(Prefer.RETURN));
    }

    @Test
    void render() {
        final Prefer unit = Prefer.valueOf("return=minimal");

        assertEquals("return=minimal", unit.toString());
    }

    @Test
    void renderApplied() {
        final Prefer unit = Prefer.valueOf("return=minimal");

        unit.applies(Prefer.RETURN);

        assertEquals("return=minimal", unit.applied());
    }

    @Test
    void shouldFailOnNullValue() {
        final Prefer unit = Prefer.valueOf("return");

        assertThrows(IllegalArgumentException.class, () -> unit.get(Prefer.RETURN));
    }

}
