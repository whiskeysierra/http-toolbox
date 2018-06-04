package io.github.whiskeysierra.http.prefer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RespondAsyncDefinitionTest {

    private final Prefer unit = Prefer.valueOf("respond-async");

    @Test
    void parse() {
        assertNull(unit.get(Prefer.RESPOND_ASYNC).getValue());
    }

    @Test
    void render() {
        assertEquals("respond-async", unit.toString());
    }

    @Test
    void renderApplied() {
        unit.apply(Prefer.RESPOND_ASYNC);
        assertEquals("respond-async", unit.applied());
    }

}
