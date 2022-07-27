package de.wazilla.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public final class Props {

    private Props() {
        // Utility class
    }

    public static Set<String> toKeySet(Properties properties) {
        if (properties == null) return Collections.emptySet();
        Set<String> keys = new LinkedHashSet<>();
        Enumeration<?> propertyNames = properties.propertyNames();
        while(propertyNames.hasMoreElements()) {
            String key = (String) propertyNames.nextElement();
            keys.add(key);
        }
        return keys;
    }

    public static Properties load(InputStream in) throws IOException {
        Properties properties = new Properties();
        if (in == null) return properties;
        try {
            properties.load(in);
            return properties;
        } finally {
            Streams.close(in);
        }
    }

}
