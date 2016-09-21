package io.github.whiskeysierra.http.prefer;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;

/**
 * A client MAY use multiple instances of the Prefer header field in a single message, or it MAY use a single
 * Prefer header field with multiple comma-separated preference tokens. If multiple Prefer header fields are used,
 * it is equivalent to a single Prefer header field with the comma-separated concatenation of all of the tokens.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7240#section-2">Section 2 of [RFC7240]</a>
 */
public final class MultipleInstancesTest {

    @Test
    public void shouldCombineTokensFromMultipleInstances() {
        final Prefer prefer = Prefer.valueOf(Arrays.asList(
                null,
                "",
                "return=representation, wait=1",
                "return=minimal, wait=2",
                "wait=3",
                "handling=strict",
                "wait=4"
        ));

        assertThat(prefer, hasToString("return=representation, wait=1, handling=strict"));
        assertThat(prefer.get(Prefer.RETURN).getValue(), is(Return.REPRESENTATION));
        assertThat(prefer.get(Prefer.HANDLING).getValue(), is(Handling.STRICT));
        assertThat(prefer.get(Prefer.WAIT).getValue(), is(1));
    }

}
