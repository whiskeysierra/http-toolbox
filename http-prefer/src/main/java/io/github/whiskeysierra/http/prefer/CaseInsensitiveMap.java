package io.github.whiskeysierra.http.prefer;

import javax.annotation.Nonnull;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;

final class CaseInsensitiveMap<V> extends AbstractMap<String, V> {

    private final Map<UncasedString, V> map = new LinkedHashMap<>();

    @Override
    public boolean containsKey(final Object key) {
        final String keyString = (String) key;
        return map.containsKey(new UncasedString(keyString));
    }

    @Override
    public V put(final String key, final V value) {
        return map.put(new UncasedString(key), value);
    }

    @Override
    public void putAll(final Map<? extends String, ? extends V> m) {
        m.forEach((k, v) -> map.put(new UncasedString(k), v));
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean containsValue(final Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(final Object key) {
        final String keyString = (String) key;
        return map.get(new UncasedString(keyString));
    }

    @Override
    public V merge(final String key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        final V oldValue = get(key);
        final V newValue = (oldValue == null) ? value :
                remappingFunction.apply(oldValue, value);
        if(newValue == null) {
            remove(key);
        } else {
            put(key, newValue);
        }
        return newValue;
    }

    @Override
    public V remove(final Object key) {
        return map.remove(new UncasedString((String) key));
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    @Nonnull
    public Set<String> keySet() {
        return new KeySet();
    }

    private class KeySet extends AbstractSet<String> {

        @Override
        public boolean contains(final Object o) {
            return containsKey(o);
        }

        @Override
        public int size() {
            return map.size();
        }

        @Override
        public boolean isEmpty() {
            return map.isEmpty();
        }

        @Override
        @Nonnull
        public Iterator<String> iterator() {
            final Iterator<UncasedString> iterator = map.keySet().iterator();
            return new Iterator<String>() {

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public String next() {
                    return iterator.next().toString();
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

    }

    @Override
    public Collection<V> values() {
        return map.values();
    }
    @Override
    @Nonnull
    public Set<Entry<String, V>> entrySet() {
        return new EntrySet();
    }

    private final class EntrySet extends AbstractSet<Entry<String, V>> {

        @Override
        public boolean contains(final Object o) {
            @SuppressWarnings("unchecked") final Entry<String, V> entry = (Entry) o;
            final UncasedString key = new UncasedString(entry.getKey());
            return map.containsKey(key) && Objects.equals(map.get(key), entry.getValue());
        }

        @Override
        public int size() {
            return map.size();
        }

        @Override
        public boolean isEmpty() {
            return map.isEmpty();
        }

        @Override
        @Nonnull
        public Iterator<Entry<String, V>> iterator() {
            final Iterator<Entry<UncasedString, V>> iterator = map.entrySet().iterator();
            return new Iterator<Entry<String, V>>() {

                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public Entry<String, V> next() {
                    final Entry<UncasedString, V> entry = iterator.next();
                    return new SimpleEntry<>(entry.getKey().toString(), entry.getValue());
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

    }
    private static final class UncasedString {

        private final String value;
        private int hash;

        private UncasedString(final String value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            if (hash == 0) {
                this.hash = value.toLowerCase(Locale.ROOT).hashCode() ^ 17; // TODO is that ok?
            }
            return hash;
        }

        @Override
        public boolean equals(final Object that) {
            if (this == that) {
                return true;
            } else if (that instanceof UncasedString) {
                final UncasedString other = (UncasedString) that;
                return value.equalsIgnoreCase(other.value);
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return value;
        }

    }

    @Override
    public String toString() {
        return map.toString();
    }

}