package de.wazilla.utils.config;

import de.wazilla.utils.Props;

import java.util.Properties;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SystemPropertiesPropertySource implements PropertySource {

    private Predicate<String> filter;

    public SystemPropertiesPropertySource() {
        this(key -> true);
    }

    public SystemPropertiesPropertySource(String prefix) {
        this(key -> key.startsWith(prefix));
    }
    public SystemPropertiesPropertySource(Predicate<String> filter) {
        this.filter = filter;
    }

    @Override
    public String getPropertyValue(String key) {
        if (filter.test(key)) return null;
        return System.getProperty(key);
    }

    @Override
    public Set<String> getKeys() {
        Properties properties = System.getProperties();
        Set<String> keys = Props.toKeySet(properties);
        return keys.stream().filter(this.filter).collect(Collectors.toSet());
    }
}
