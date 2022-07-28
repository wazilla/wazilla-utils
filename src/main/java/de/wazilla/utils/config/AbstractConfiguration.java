package de.wazilla.utils.config;

import de.wazilla.utils.Strings;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractConfiguration {

    protected List<PropertySource> propertySources;
    protected Map<Class<?>, PropertyConverter> propertyConverterMap;

    protected AbstractConfiguration(List<PropertySource> propertySources) {
        this.propertySources = propertySources;
        this.propertyConverterMap = new HashMap<>();
        this.propertyConverterMap.put(double.class, Double::parseDouble);
        this.propertyConverterMap.put(float.class, Float::parseFloat);
        this.propertyConverterMap.put(int.class, Integer::parseInt);
        this.propertyConverterMap.put(long.class, Long::parseLong);
        this.propertyConverterMap.put(boolean.class, Boolean::parseBoolean);
        this.propertyConverterMap.put(byte.class, Byte::parseByte);
        this.propertyConverterMap.put(Double.class, Double::valueOf);
        this.propertyConverterMap.put(Float.class, Float::valueOf);
        this.propertyConverterMap.put(Integer.class, Integer::valueOf);
        this.propertyConverterMap.put(Long.class, Long::valueOf);
        this.propertyConverterMap.put(String.class, s -> s);
        this.propertyConverterMap.put(URL.class, s -> new URL(s));
    }

    public Set<String> getKeys() {
        return this.propertySources.stream()
                .map(PropertySource::getKeys)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    protected <T> T getValue(String key, Class<T> type) {
        PropertyConverter<T> propertyConverter = this.propertyConverterMap.get(type);
        return getValue(key, propertyConverter);
    }

    protected <T> T getValue(String key, PropertyConverter<T> propertyConverter) {
        try {
            String resolvedValue = getResolvedValue(key);
            return propertyConverter.convert(resolvedValue);
        } catch (Exception e) {
            throw new RuntimeException(e); // TODO
        }
    }

    private String getResolvedValue(String key) {
        return getPropertyValue(key); // TODO resolve placeholders
    }

    private String getPropertyValue(String key) {
        return this.propertySources.stream()
                .map(ps -> ps.getPropertyValue(key))
                .filter(Strings::isNotNullOrEmpty)
                .findFirst()
                .orElse(null);
    }

}
