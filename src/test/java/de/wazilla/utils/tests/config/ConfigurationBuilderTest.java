package de.wazilla.utils.tests.config;

import de.wazilla.utils.config.*;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigurationBuilderTest {

    @Test
    void testWithInterface() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("stringValue", "testuser");
        configMap.put("longValue", "123");
        configMap.put("primitiveLongValue", "456");
        configMap.put("renamed", "foobar");
        configMap.put("numbers", "1, 2, 3");
        configMap.put("faulty", "ignored");
        PropertySource propertySource = new MapPropertySource(configMap);
        TestConfiguration testConfiguration = new ConfigurationBuilder().withPropertySource(propertySource).build(TestConfiguration.class);
        assertNotNull(testConfiguration.getKeys());
        assertFalse(testConfiguration.getKeys().isEmpty());
        assertEquals("testuser", testConfiguration.getStringValue());
        assertEquals("foobar", testConfiguration.getRenamedKey());
        assertEquals(Arrays.asList(1, 2, 3), testConfiguration.getNumbers());
        assertEquals(Long.valueOf(123), testConfiguration.getLongValue());
        assertEquals(456, testConfiguration.getPrimitiveLongValue());
        assertNull(testConfiguration.getNonExistingValue());
        assertEquals(-1, testConfiguration.getNonExistingIntValue());
        assertFalse(testConfiguration.getNonExistingBooleanValue());
        assertEquals(-1F, testConfiguration.getNonExistingFloatValue());
        assertThrows(ConfigurationRuntimeException.class, testConfiguration::getValueWithFaultyConverter);
        assertThrows(ConfigurationRuntimeException.class, testConfiguration::getValueWithFaultyCtorConverter);
    }

    interface TestConfiguration {

        String getStringValue();
        @ConfigurationKey("renamed")
        String getRenamedKey();

        @ConfigurationConverter(NumberListPropertyConverter.class)
        List<Integer> getNumbers();

        Long getLongValue();

        long getPrimitiveLongValue();

        String getNonExistingValue();
        int getNonExistingIntValue();
        char getNonExistingCharValue();
        boolean getNonExistingBooleanValue();
        float getNonExistingFloatValue();

        Set<String> getKeys();

        @ConfigurationKey("faulty")
        @ConfigurationConverter(FaultyPropertyConverter.class)
        String getValueWithFaultyConverter();
        @ConfigurationKey("faulty")
        @ConfigurationConverter(FaultyCtorPropertyConverter.class)
        String getValueWithFaultyCtorConverter();
    }

    public static class NumberListPropertyConverter implements PropertyConverter<List<Integer>> {

        @Override
        public List<Integer> convert(String value) throws Exception {
            if (value == null || value.isEmpty()) return new ArrayList<>();
            return Arrays.stream(value.split(","))
                    .map(String::trim)
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());
        }
    }

    public static class FaultyPropertyConverter implements PropertyConverter<String> {

        @Override
        public String convert(String value) throws Exception {
            throw new DateTimeException("This Exception is thrown intentionally");
        }
    }

    public static class FaultyCtorPropertyConverter implements PropertyConverter<Void> {

        public FaultyCtorPropertyConverter(String value) {
            // This class has no default constructor, so we should get an InstantiationException
        }

        @Override
        public Void convert(String value) throws Exception {
            throw new IllegalStateException("We should not get here");
        }
    }
}
