package de.wazilla.utils.config;

import java.util.Set;

public interface PropertySource {

    String getPropertyValue(String key);

    Set<String> getKeys();

}
