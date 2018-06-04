package io.github.whiskeysierra.http.prefer;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class CaseSensitivityTest {

    /**
     * For [..] preference token names [..] comparison is case insensitive while values are case
     * sensitive regardless of whether token or quoted-string values are used.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7240#section-2">Section 2 of [RFC7240]</a>
     */
    @Test
    void shouldCompareTokenNamesCaseInsensitive() {
        assertThat(Prefer.valueOf("RESPOND-ASYNC").contains(Prefer.RESPOND_ASYNC), is(true));
    }

    /**
     * [..] while values are case sensitive regardless of whether token or quoted-string values are used.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7240#section-2">Section 2 of [RFC7240]</a>
     */
    @Test
    void shouldCompareTokenValuesCaseSensitive() {
        assertThrows(IllegalArgumentException.class, () ->
                Prefer.valueOf("return=MINIMAL").get(Prefer.RETURN));
    }

}
