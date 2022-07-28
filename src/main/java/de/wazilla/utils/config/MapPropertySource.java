package de.wazilla.utils.config;

import java.util.Map;
import java.util.Set;

public class MapPropertySource implements PropertySource {

    private Map<String, String> map;

    public MapPropertySource(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public String getPropertyValue(String key) {
        return this.map.get(key);
    }

    @Override
    public Set<String> getKeys() {
        return this.map.keySet();
    }
}
