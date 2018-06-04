package io.github.whiskeysierra.http.prefer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContainsTest {

    private final Prefer unit = Prefer.valueOf("handling=lenient, respond-async");

    @Test
    void shouldContainDefinition() {
        assertTrue(unit.contains(Prefer.HANDLING));
    }

    @Test
    void shouldNotContainDefinition() {
        assertFalse(unit.contains(Prefer.RETURN));
    }

    @Test
    void shouldContainDefinitionWithValue() {
        assertTrue(unit.contains(Prefer.HANDLING, Handling.LENIENT));
    }

    @Test
    void shouldNotContainCorrectDefinitionWithIncorrectValue() {
        assertFalse(unit.contains(Prefer.HANDLING, Handling.STRICT));
    }

    @Test
    void shouldNotContainIncorrectDefinitionWithIncorrectValue() {
        assertFalse(unit.contains(Prefer.RETURN, Return.MINIMAL));
    }

    @Test
    void get() {
        // no exception
        unit.get(Prefer.HANDLING);
        unit.get(Prefer.RESPOND_ASYNC);
    }

    @Test
    void shouldNotGet() {
        assertThrows(IllegalArgumentException.class, () -> unit.get(Prefer.RETURN));
    }

}
