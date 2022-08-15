package de.wazilla.utils.tests.text;

import de.wazilla.utils.text.Lookup;
import de.wazilla.utils.text.MapLookup;
import de.wazilla.utils.text.VariableResolver;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractVariableResolverTest {

    protected abstract VariableResolver getVariableResolver();

    @Test
    public void resolve_givenNullAsTemplate_shouldReturnNull() {
        String result = getVariableResolver().resolve(null, null);
        assertNull(result);
    }

    @Test
    public void resolve_givenTemplateAndLookup_shouldReturnResolvedText() {
        Map<String, String> valueMap = new HashMap<>();
        valueMap.put("foo", "bar");
        valueMap.put("abc", "xyz");
        Lookup lookup = new MapLookup(valueMap);
        String result = getVariableResolver().resolve("Der Wert von ${foo} ist ${abc}", lookup);
        assertNotNull(result);
        assertEquals("Der Wert von bar ist xyz", result);
    }

    @Test
    public void resolve_givenTemplateAndLookupWithMissingValue_shouldReturnResolvedTextWithUnresolvedPlaceholder() {
        Map<String, String> valueMap = new HashMap<>();
        valueMap.put("foo", "bar");
        Lookup lookup = new MapLookup(valueMap);
        String result = getVariableResolver().resolve("Der Wert von ${foo} ist ${abc}", lookup);
        assertNotNull(result);
        assertEquals("Der Wert von bar ist ${abc}", result);
    }

    @Test
    public void resolve_givenTemplateAndNullAsLookup_shouldReturnTemplateWithUnresolvedPlaceholders() {
        String template = "Der Wert von ${foo} ist ${abc}";
        String result = getVariableResolver().resolve(template, null);
        assertNotNull(result);
        assertEquals(template, result);
    }

}
