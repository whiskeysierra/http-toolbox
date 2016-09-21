package io.github.whiskeysierra.http.prefer;

final class ReturnPreference implements Preference<Return> {

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
                return null; // TODO throw?
        }
    }

}
