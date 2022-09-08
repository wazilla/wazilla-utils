package de.wazilla.utils.tests;

import de.wazilla.utils.Props;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PropsTest {

    @Test
    void toKeySet_PropertiesGiven_ShouldReturnSetOfKeys() {
        Properties properties = new Properties();
        properties.setProperty("foo", "abc");
        properties.setProperty("bar", "xyz");
        Set<String> keys = Props.toKeySet(properties);
        assertEquals(2, keys.size());
        assertTrue(keys.contains("foo"));
        assertTrue(keys.contains("bar"));
    }

    @Test
    void toKeySet_NullGiven_ShouldReturnEmptySet() {
        assertTrue(Props.toKeySet(null).isEmpty());
    }

    @Test
    void toMap_NullGiven_ShouldReturnEmptyMap() {
        assertTrue(Props.toMap(null).isEmpty());
    }

    @Test
    void toMap_PropertiesGiven_ShouldReturnMap() {
        Properties properties = new Properties();
        properties.setProperty("foo", "abc");
        properties.setProperty("bar", "xyz");
        Map<String, String> map = Props.toMap(properties);
        assertEquals(2, map.size());
        assertTrue(map.containsKey("foo"));
        assertTrue(map.containsKey("bar"));
        assertEquals("abc", map.get("foo"));
        assertEquals("xyz", map.get("bar"));
    }



}