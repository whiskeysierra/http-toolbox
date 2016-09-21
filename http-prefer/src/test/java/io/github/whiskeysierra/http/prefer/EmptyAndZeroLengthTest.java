package io.github.whiskeysierra.http.prefer;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public final class EmptyAndZeroLengthTest {

    @Test
    public void shouldSupportNullPreference() {
        final String value = null;
        final Prefer prefer = Prefer.valueOf(value);

        assertThat(prefer, hasToString(""));
        assertThat(prefer.contains("foo"), is(false));
    }

    @Test
    public void shouldSupportEmptyPreference() {
        final Prefer prefer = Prefer.valueOf(" ");

        assertThat(prefer, hasToString(""));
        assertThat(prefer.contains("foo"), is(false));
    }

    @Test
    public void shouldSupportZeroLengthPreference() {
        final Prefer prefer = Prefer.valueOf("");

        assertThat(prefer, hasToString(""));
        assertThat(prefer.contains("foo"), is(false));
    }

    /**
     * Empty [..] values on [..] the preference token [..]
     * are equivalent to no value being specified at all.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7240#section-2">Section 2 of [RFC7240]</a>
     */
    @Test
    public void shouldSupportEmptyTokenValue() {
        final Prefer prefer = Prefer.valueOf("foo=, bar");

        assertThat(prefer, hasToString("foo, bar"));
        assertThat(prefer.contains("foo"), is(true));
        assertThat(prefer.get("foo"), is(nullValue()));
    }

    /**
     * [..] zero-length values on [..] the preference token [..]
     * are equivalent to no value being specified at all.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7240#section-2">Section 2 of [RFC7240]</a>
     */
    @Test
    public void shouldSupportZeroLengthTokenValue() {
        final Prefer prefer = Prefer.valueOf("foo=\"\"");

        assertThat(prefer, hasToString("foo"));
    }

}
