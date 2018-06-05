package io.github.whiskeysierra.http.prefer;

import org.junit.jupiter.api.Test;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WaitDefinitionTest {

    @Test
    void parse() {
        final Prefer unit = Prefer.valueOf("wait=1");

        assertEquals(1, requireNonNull(unit.get(Prefer.WAIT).getValue()).intValue());
    }

    @Test
    void parseMissingValue() {
        final Prefer unit = Prefer.valueOf("wait");

        assertThrows(IllegalArgumentException.class, () -> unit.get(Prefer.WAIT));
    }

    @Test
    void render() {
        final Prefer unit = Prefer.valueOf("wait=1");

        assertEquals("wait=1", unit.toString());
    }

    @Test
    void renderApplied() {
        final Prefer unit = Prefer.valueOf("wait=1");

        unit.applies(Prefer.WAIT);

        assertEquals("wait=1", unit.applied());
    }

}
