package de.wazilla.util.tests;

import de.wazilla.utils.config.ConfigurationBuilder;
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
        configMap.put("username", "wazilla");
        PropertySource propertySource = new MapPropertySource(configMap);
        TestConfiguration testConfiguration = new ConfigurationBuilder().withPropertySource(propertySource).build(TestConfiguration.class);
        assertEquals("wazilla", testConfiguration.getUsername());
    }

    interface TestConfiguration {

        String getUsername();

    }

}
