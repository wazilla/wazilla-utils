package de.wazilla.utils.config;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NormalizingEnvironmentVariablesPropertySource implements PropertySource {

    private static final Predicate<String> DEFAULT_FILTER = key -> true;
    private static final Function<String, String> DEFAULT_NORMALIZER = key -> {
        StringBuilder sb = new StringBuilder();
        boolean upperCase = true;
        for(int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            if (ch == '_') {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(ch));
                upperCase = false;
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    };

    private Predicate<String> filter;
    private Function<String, String> normalizer;
    private Map<String, String> environment;

    public NormalizingEnvironmentVariablesPropertySource() {
        this(DEFAULT_FILTER);
    }

    public NormalizingEnvironmentVariablesPropertySource(Predicate<String> filter) {
        this(filter, DEFAULT_NORMALIZER);
    }

    public NormalizingEnvironmentVariablesPropertySource(Predicate<String> filter, Function<String, String> normalizer) {
        this.filter = filter;
        this.normalizer = normalizer;
        this.environment = System.getenv();
    }

    @Override
    public String getPropertyValue(String key) {
        for(Map.Entry<String, String> entry : this.environment.entrySet()) {
            if (filter.test(entry.getKey())) {
                String normalizedKey = this.normalizer.apply(entry.getKey());
                if (normalizedKey.equals(key)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public Set<String> getKeys() {
        return this.environment.keySet().stream()
                .filter(this.filter)
                .map(this.normalizer)
                .collect(Collectors.toSet());
    }
}
