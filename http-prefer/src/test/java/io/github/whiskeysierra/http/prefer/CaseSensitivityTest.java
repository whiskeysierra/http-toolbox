package io.github.whiskeysierra.http.prefer;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public final class CaseSensitivityTest {

    /**
     * For [..] preference token names [..] comparison is case insensitive while values are case
     * sensitive regardless of whether token or quoted-string values are used.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7240#section-2">Section 2 of [RFC7240]</a>
     */
    @Test
    public void shouldCompareTokenNamesCaseInsensitive() {
        assertThat(Prefer.valueOf("RESPOND-ASYNC").contains(Prefer.RESPOND_ASYNC), is(true));
    }

    /**
     * [..] while values are case sensitive regardless of whether token or quoted-string values are used.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7240#section-2">Section 2 of [RFC7240]</a>
     */
    @Test
    public void shouldCompareTokenValuesCaseSensitive() {
        assertThat(Prefer.valueOf("return=minimal").get(Prefer.RETURN).getValue(), is(Return.MINIMAL));
        assertThat(Prefer.valueOf("return=MINIMAL").get(Prefer.RETURN).getValue(), is(not(Return.MINIMAL)));
    }

}
