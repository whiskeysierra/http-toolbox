package io.github.whiskeysierra.http.prefer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ApplyTest {

    private final Prefer unit = Prefer.valueOf("handling=lenient, respond-async");

    @Mock
    private Consumer<Preference<Handling>> handlingConsumer;

    @Mock
    private Consumer<Preference<Return>> returnConsumer;

    @Test
    void shouldApplyDefinition() {
        assertTrue(unit.apply(Prefer.HANDLING));
    }

    @Test
    void shouldNotApplyDefinition() {
        assertFalse(unit.apply(Prefer.RETURN));
    }

    @Test
    void shouldApplyPreference() {
        final Preference<Handling> preference = unit.get(Prefer.HANDLING);

        assertTrue(unit.apply(preference));
    }

    @Test
    void shouldNotApplyPreference() {
        assertThrows(IllegalArgumentException.class, () -> unit.get(Prefer.RETURN));
    }

    @Test
    void shouldApplyDefinitionWithValue() {
        assertTrue(unit.apply(Prefer.HANDLING, Handling.LENIENT));
    }

    @Test
    void shouldNotApplyDefinitionWithValue() {
        assertFalse(unit.apply(Prefer.HANDLING, Handling.STRICT));
    }

    @Test
    void shouldApplyDefinitionConsumer() {
        unit.apply(Prefer.HANDLING, handlingConsumer);

        verify(handlingConsumer).accept(any());
    }

    @Test
    void shouldNotApplyDefinitionConsumer() {
        unit.apply(Prefer.RETURN, returnConsumer);

        verify(returnConsumer, never()).accept(any());
    }

    @Test
    void shouldApplyDefinitionWithValueConsumer() {
        unit.apply(Prefer.HANDLING, Handling.LENIENT, handlingConsumer);

        verify(handlingConsumer).accept(any());
    }

    @Test
    void shouldNotApplyDefinitionWithValueConsumer() {
        unit.apply(Prefer.HANDLING, Handling.STRICT, handlingConsumer);

        verify(handlingConsumer, never()).accept(any());
    }

    @Test
    void apply2() {
    }

    @Test
    void apply3() {
    }

    @Test
    void apply4() {
    }
}
