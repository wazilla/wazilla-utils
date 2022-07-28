package de.wazilla.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class Maps {

    private static final Class<? extends Map> DEFAULT_MAP_IMPLEMENTATION_CLASS = LinkedHashMap.class;

    private Maps() {
        // Utility class
    }

    public static <K, V> MapBuilder<K, V> builder(Class<? extends Map> mapImplementationClass) {
        Map<K, V> map = createMap(mapImplementationClass);
        return new MapBuilder<>(map);
    }

    public static <K, V> Map<K, V> singleEntryMap(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
    public static <K, V> Map<K, V> createMap(K[] keys, V[] values) {
        return createMap(keys, values, DEFAULT_MAP_IMPLEMENTATION_CLASS);
    }

    public static <K, V> Map<K, V> createMap(K[] keys, V[] values, Class<? extends Map> mapImplementationClass) {
        Objects.requireNonNull(keys, "keys == null");
        Objects.requireNonNull(values, "values == null");
        if (keys.length != values.length) throw new IllegalArgumentException("keys.length != values.length");
        Map<K, V> map = createMap(mapImplementationClass);
        for(int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    private static <K, V> Map<K, V> createMap(Class<? extends Map> mapImplementationClass) {
        try {
            return mapImplementationClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Error creating " + mapImplementationClass, ex);
        }
    }

    public static class MapBuilder<K, V> {

        private Map<K, V> map;

        private MapBuilder(Map<K, V> map) {
            this.map = map;
        }

        public MapBuilder<K, V> put(K key, V value) {
            this.map.put(key, value);
            return this;
        }

        public MapBuilder<K, V> putAll(Map<K, V> map) {
            this.map.putAll(map);
            return this;
        }

        public Map<K, V> build() {
            return this.map;
        }

    }

}
