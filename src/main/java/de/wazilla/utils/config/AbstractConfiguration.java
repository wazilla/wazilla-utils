package de.wazilla.utils.config;

import de.wazilla.utils.Strings;

import java.util.List;
import java.util.Map;

public abstract class AbstractConfiguration {

    private List<PropertySource> propertySources;
    private Map<Class<?>, PropertyConverter> propertyConverterMap;

    protected AbstractConfiguration(List<PropertySource> propertySources) {
        this.propertySources = propertySources;
    }

    protected <T> T getPropertyValue(String key, Class<T> type) {
        String value = getResolvedValue(key);
        PropertyConverter<T> propertyConverter = this.propertyConverterMap.get(key);
        if (propertyConverter != null) {
            return propertyConverter.convert(value);
        } else {
            throw new IllegalStateException("TODO"); // TODO
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
