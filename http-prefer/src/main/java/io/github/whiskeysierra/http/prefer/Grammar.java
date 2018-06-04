package io.github.whiskeysierra.http.prefer;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

final class Grammar {

    static final Pattern TOKEN = compile("(?:[-!#$%&'*+.^_`|~]|\\w)+");
    static final Pattern QUOTED_STRING = compile("(?:\"(?:[\\t !#-\\[\\]-~\\x80-\\xFF]|(?:\\\\[\\t !-~\\x80-\\xFF]))*\")");
    static final Pattern NAMED_VALUE = compile("(" + TOKEN + ")(?:=(" + TOKEN + "|" + QUOTED_STRING + ")?)?");
    static final Pattern PARAMETER = compile("(?:\\s?;\\s?)+" + NAMED_VALUE);
    static final Pattern PREFERENCE = compile("(?:\\s?,\\s?)*" + NAMED_VALUE + "((?:\\s?;\\s?(?:" + NAMED_VALUE + ")?)*)");

    private Grammar() {

    }

}
