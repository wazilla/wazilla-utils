package de.wazilla.utils.config;

import de.wazilla.utils.Strings;
import de.wazilla.utils.text.Lookup;
import de.wazilla.utils.text.StandardVariableResolver;
import de.wazilla.utils.text.VariableResolver;

import java.net.URL;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class AbstractConfiguration {

    protected final List<PropertySource> propertySources;
    protected final Map<Class<?>, PropertyConverter<?>> propertyConverterMap;
    protected final Map<Class<?>, Supplier<?>> substituteValueSupplierMap;
    protected final VariableResolver variableResolver;


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
        this.substituteValueSupplierMap = new HashMap<>();
        this.substituteValueSupplierMap.put(boolean.class, () -> false);
        this.substituteValueSupplierMap.put(char.class, () -> 0);
        this.substituteValueSupplierMap.put(int.class, () -> -1);
        this.substituteValueSupplierMap.put(double.class, () -> -1D);
        this.substituteValueSupplierMap.put(float.class, () -> -1F);
        this.substituteValueSupplierMap.put(short.class, () -> (short) -1);
        this.substituteValueSupplierMap.put(byte.class, () -> (byte) -1);
        this.variableResolver = new StandardVariableResolver();
    }

    public Set<String> getKeys() {
        return this.propertySources.stream()
                .map(PropertySource::getKeys)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    protected <T> T getValue(String key, Class<T> type) {
        PropertyConverter<T> propertyConverter = (PropertyConverter<T>) this.propertyConverterMap.get(type);
        return getValue(key, propertyConverter, type);
    }

    @SuppressWarnings("unchecked")
    protected <T> T getValue(String key, PropertyConverter<T> propertyConverter, Class<T> returnType) {
        Objects.requireNonNull(key, "key is null");
        Objects.requireNonNull(propertyConverter, "propertyConverter is null for key " + key);
        String resolvedValue = getResolvedValue(key);
        if (resolvedValue == null) {
            if (returnType.isPrimitive()) {
                Supplier<?> supplier = this.substituteValueSupplierMap.get(returnType);
                return (T) supplier.get();
            } else {
                return null;
            }
        }
        try {
            return propertyConverter.convert(resolvedValue);
        } catch (Exception ex) {
            throw new ConfigurationRuntimeException("Error converting '" + resolvedValue + "' with " + propertyConverter.getClass().getName(), ex);
        }
    }

    private String getResolvedValue(String key) {
        String value = getPropertyValue(key);
        Lookup lookup = this::getPropertyValue;
        return variableResolver.resolve(value, lookup);
    }

    private String getPropertyValue(String key) {
        return this.propertySources.stream()
                .map(ps -> ps.getPropertyValue(key))
                .filter(Strings::isNotNullOrEmpty)
                .findFirst()
                .orElse(null);
    }

}
