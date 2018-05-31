package io.github.whiskeysierra.http.prefer;

import java.util.Locale;

final class ReturnDefinition implements Definition<Return> {

    @Override
    public String getName() {
        return "return";
    }

    @Override
    public Return parse(final String value) {
        switch (value) {
            case "minimal":
                return Return.MINIMAL;
            case "representation":
                return Return.REPRESENTATION;
            default:
                return null;
        }
    }

    @Override
    public String render(final Return value) {
        return getName() + "=" + value.name().toLowerCase(Locale.ROOT);
    }

}
