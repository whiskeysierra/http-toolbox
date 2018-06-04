package io.github.whiskeysierra.http.prefer;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class ParseTest {

    private final Definition<String> foo = new StringDefinition("foo");
    private final Definition<String> weird = new StringDefinition("weird");

    @Test
    void shouldSupportNullPreference() {
        final String value = null;
        final Prefer prefer = Prefer.valueOf(value);

        assertThat(prefer, hasToString(""));
        assertThat(prefer.contains(foo), is(false));
    }

    @Test
    void shouldSupportNullPreferences() {
        final Prefer prefer = Prefer.valueOf(emptyList());

        assertThat(prefer, hasToString(""));
        assertThat(prefer.contains(foo), is(false));
    }

    @Test
    void shouldSupportEmptyPreference() {
        final Prefer prefer = Prefer.valueOf(" ");

        assertThat(prefer, hasToString(""));
        assertThat(prefer.contains(foo), is(false));
    }

    @Test
    void shouldSupportZeroLengthPreference() {
        final Prefer prefer = Prefer.valueOf("");

        assertThat(prefer, hasToString(""));
        assertThat(prefer.contains(foo), is(false));
    }

    /**
     * Empty [..] values on [..] the preference token [..]
     * are equivalent to no value being specified at all.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7240#section-2">Section 2 of [RFC7240]</a>
     */
    @Test
    void shouldSupportEmptyTokenValue() {
        final Prefer prefer = Prefer.valueOf("foo=, bar");

        assertThat(prefer, hasToString("bar, foo"));
        assertThat(prefer.contains(foo), is(true));
        assertThat(prefer.get(foo).getValue(), is(nullValue()));
    }

    /**
     * [..] zero-length values on [..] the preference token [..]
     * are equivalent to no value being specified at all.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7240#section-2">Section 2 of [RFC7240]</a>
     */
    @Test
    void shouldSupportZeroLengthTokenValue() {
        final Prefer prefer = Prefer.valueOf("foo=\"\"");

        assertThat(prefer, hasToString("foo"));
        assertThat(prefer.contains(foo), is(true));
        assertThat(prefer.get(foo).getValue(), is(nullValue()));
    }

    @Test
    void shouldSupportTokenValue() {
        final Prefer prefer = Prefer.valueOf("wait=1");

        assertThat(prefer, hasToString("wait=1"));
        assertThat(prefer.get(Prefer.WAIT).getValue(), is(1));
    }

    // TODO test comma + semicolon
    @Test
    void shouldSupportQuotedTokenValue() {
        final Prefer prefer = Prefer.valueOf("weird=\"a\\\\b,\\\"123\\\"c\", wait=1");

        // TODO force quote on toString
        // TODO assertThat(prefer, hasToString("weird=\"a\\\\b,\\\"123\\\"c\", wait=1"));
        assertThat(prefer.get(weird).getValue(), is("a\\b,\"123\"c"));
        assertThat(prefer.get(Prefer.WAIT).getValue(), is(1));
    }

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
    void shouldConsiderFirstTokenOnly() {
        final Prefer prefer = Prefer.valueOf("foo=bar, foo=baz");

        assertThat(prefer, hasToString("foo=bar"));
        assertThat(prefer.get(foo).getValue(), is("bar"));
    }

    /**
     * A client MAY use multiple instances of the Prefer header field in a single message, or it MAY use a single
     * Prefer header field with multiple comma-separated preference tokens. If multiple Prefer header fields are used,
     * it is equivalent to a single Prefer header field with the comma-separated concatenation of all of the tokens.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7240#section-2">Section 2 of [RFC7240]</a>
     */
    @Test
    void shouldCombineTokensFromMultipleHeaders() {
        final Prefer prefer = Prefer.valueOf(Arrays.asList(
                null,
                "",
                "return=representation, wait=1",
                "return=minimal, wait=2",
                "wait=3",
                "handling=strict",
                "wait=4",
                "respond-async"
        ));

        assertThat(prefer, hasToString("handling=strict, respond-async, return=representation, wait=1"));
        assertThat(prefer.get(Prefer.RETURN).getValue(), is(Return.REPRESENTATION));
        assertThat(prefer.get(Prefer.HANDLING).getValue(), is(Handling.STRICT));
        assertThat(prefer.get(Prefer.WAIT).getValue(), is(1));
        assertThat(prefer.contains(Prefer.RESPOND_ASYNC), is(true));
    }

    @Test
    void shouldIgnoreEmptyTokens() {
        final Prefer prefer = Prefer.valueOf("foo=bar, , foo=baz");

        assertThat(prefer, hasToString("foo=bar"));
        assertThat(prefer.get(foo).getValue(), is("bar"));
    }

    @Test
    void shouldIgnoreZeroLengthTokens() {
        final Prefer prefer = Prefer.valueOf("foo=bar,, foo=baz");

        assertThat(prefer, hasToString("foo=bar"));
        assertThat(prefer.get(foo).getValue(), is("bar"));
    }

    @Test
    void shouldFailOnInvalidTokens() {
        assertThrows(IllegalArgumentException.class, () -> Prefer.valueOf("ä=foo"));
    }

    @Test
    void shouldIgnoreEmptyParameter() {
        final Prefer prefer = Prefer.valueOf("handling=strict; ;");

        assertThat(prefer, hasToString("handling=strict"));
        assertThat(prefer.get(Prefer.HANDLING).getParameters(), is(emptyMap()));
    }

    @Test
    void shouldIgnoreZeroLengthParameter() {
        final Prefer prefer = Prefer.valueOf("handling=strict;;");

        assertThat(prefer, hasToString("handling=strict"));
        assertThat(prefer.get(Prefer.HANDLING).getParameters(), is(emptyMap()));
    }

    @Test
    void shouldIgnoreAbsentParameterValue() {
        final Prefer prefer = Prefer.valueOf("handling=strict; version");

        assertThat(prefer, hasToString("handling=strict; version"));
        final Map<String, String> parameters = prefer.get(Prefer.HANDLING).getParameters();
        assertThat(parameters.containsKey("version"), is(true));
        assertThat(parameters.get("version"), is(nullValue()));
    }

    @Test
    void shouldIgnoreEmptyParameterValue() {
        final Prefer prefer = Prefer.valueOf("handling=strict; version= ");

        assertThat(prefer, hasToString("handling=strict; version"));
        final Map<String, String> parameters = prefer.get(Prefer.HANDLING).getParameters();
        assertThat(parameters.containsKey("version"), is(true));
        assertThat(parameters.get("version"), is(nullValue()));
    }

    @Test
    void shouldIgnoreZeroLengthParameterValue() {
        final Prefer prefer = Prefer.valueOf("handling=strict; version=\"\"");

        assertThat(prefer, hasToString("handling=strict; version"));
        final Map<String, String> parameters = prefer.get(Prefer.HANDLING).getParameters();
        assertThat(parameters.containsKey("version"), is(true));
        assertThat(parameters.get("version"), is(nullValue()));
    }

    @Test
    void shouldSupportParameterValue() {
        final Prefer prefer = Prefer.valueOf("handling=strict; version=1");

        assertThat(prefer, hasToString("handling=strict; version=1"));
        assertThat(prefer.get(Prefer.HANDLING).getParameters().get("version"), is("1"));
    }

    @Test
    void shouldSupportQuotedParameterValue() {
        final Prefer prefer = Prefer.valueOf("handling=strict; separator=\";\"");

        assertThat(prefer, hasToString("handling=strict; separator=\";\""));
        assertThat(prefer.get(Prefer.HANDLING).getParameters().get("separator"), is(";"));
    }

    @Test
    void shouldSupportMultipleParameterValues() {
        final Prefer prefer = Prefer.valueOf("handling=strict; version=1; charset=utf8, wait=500; unit=millis");

        assertThat(prefer, hasToString("handling=strict; charset=utf8; version=1, wait=500; unit=millis"));
        assertThat(prefer.get(Prefer.HANDLING).getParameters(), aMapWithSize(2));
        assertThat(prefer.get(Prefer.HANDLING).getParameters(), hasEntry("version", "1"));
        assertThat(prefer.get(Prefer.HANDLING).getParameters(), hasEntry("charset", "utf8"));
        assertThat(prefer.get(Prefer.WAIT).getParameters(), is(singletonMap("unit", "millis")));
    }

    @Test
    void shouldFailOnInvalidParameters() {
        assertThrows(IllegalArgumentException.class, () -> Prefer.valueOf("handling=strict; ä"));
    }

    @Test
    void shouldIgnoreDuplicateParameters() {
        final Prefer prefer = Prefer.valueOf("handling=strict; version=1; version=2");
        final Preference<Handling> preference = prefer.get(Prefer.HANDLING);

        assertThat(preference.getParameters(), is(singletonMap("version", "1")));
    }

}
