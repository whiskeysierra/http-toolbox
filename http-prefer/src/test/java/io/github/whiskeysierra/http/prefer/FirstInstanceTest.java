package io.github.whiskeysierra.http.prefer;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;

public final class FirstInstanceTest {

    /**
     * To avoid any possible ambiguity, individual preference tokens SHOULD NOT appear multiple times within a single
     * request. If any preference is specified more than once, only the first instance is to be considered. All
     * subsequent occurrences SHOULD be ignored without signaling an error or otherwise altering the processing of the
     * request.  This is the only case in which the ordering of preferences within a request is considered to be
     * significant.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7240#section-2">Section 2 of [RFC7240]</a>
     */
    @Test
    public void shouldConsiderFirstInstanceOnly() {
        final Prefer prefer = Prefer.valueOf("foo=bar, foo=baz");

        assertThat(prefer, hasToString("foo=bar"));
        assertThat(prefer.get("foo"), is("bar"));
    }

}
