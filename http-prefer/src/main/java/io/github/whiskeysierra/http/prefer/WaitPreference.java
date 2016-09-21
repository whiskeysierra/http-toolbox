package io.github.whiskeysierra.http.prefer;

final class WaitPreference implements Preference<Integer> {

    @Override
    public String getName() {
        return "wait";
    }

    @Override
    public Integer parse(final String value) {
        return Integer.valueOf(value);
    }

}
