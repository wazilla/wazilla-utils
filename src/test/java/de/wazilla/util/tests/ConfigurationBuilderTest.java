package de.wazilla.util.tests;

import de.wazilla.utils.config.ConfigurationBuilder;
import de.wazilla.utils.config.ConfigurationKey;
import de.wazilla.utils.config.MapPropertySource;
import de.wazilla.utils.config.PropertySource;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurationBuilderTest {

    @Test
    void todo() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("stringValue", "testuser");
        configMap.put("longValue", "123");
        configMap.put("primitiveLongValue", "456");
        configMap.put("renamed", "foobar");
        PropertySource propertySource = new MapPropertySource(configMap);
        TestConfiguration testConfiguration = new ConfigurationBuilder().withPropertySource(propertySource).build(TestConfiguration.class);
        assertEquals(configMap.get("stringValue"), testConfiguration.getStringValue());
        assertEquals(configMap.get("renamed"), testConfiguration.getRenamedKey());
        assertEquals(Long.valueOf(configMap.get("longValue")), testConfiguration.getLongValue());
        assertEquals(Long.parseLong(configMap.get("primitiveLongValue")), testConfiguration.getPrimitiveLongValue());
    }

    interface TestConfiguration {

        String getStringValue();
        @ConfigurationKey("renamed")
        String getRenamedKey();

        Long getLongValue();

        long getPrimitiveLongValue();

    }

}
