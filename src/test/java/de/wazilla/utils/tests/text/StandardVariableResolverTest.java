package de.wazilla.utils.tests.text;

import de.wazilla.utils.text.Lookup;
import de.wazilla.utils.text.MapLookup;
import de.wazilla.utils.text.StandardVariableResolver;
import de.wazilla.utils.text.VariableResolver;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StandardVariableResolverTest extends AbstractVariableResolverTest {

    private VariableResolver variableResolver = new StandardVariableResolver();

    @Override
    protected VariableResolver getVariableResolver() {
        return variableResolver;
    }

    @Test
    public void resolve_givenTemplateWithNestedVariablesAndLookup_shouldReturnResolvedText() {
        Map<String, String> valueMap = new HashMap<>();
        valueMap.put("foo", "abc");
        valueMap.put("key-abc", "xyz");
        Lookup lookup = new MapLookup(valueMap);
        String result = getVariableResolver().resolve("Der Wert ist ${key-${foo}}!", lookup);
        assertNotNull(result);
        assertEquals("Der Wert ist xyz!", result);
    }

}
