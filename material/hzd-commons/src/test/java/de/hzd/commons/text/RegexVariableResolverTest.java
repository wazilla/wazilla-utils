package de.hzd.commons.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class RegexVariableResolverTest {

	private VariableResolver variableResolver = new RegexVariableResolver();

	@Test
	public void resolve_givenNullAsTemplate_shouldReturnNull() {
		String result = variableResolver.resolve(null, null);
		assertNull(result);
	}
	
	@Test
	public void resolve_givenTemplateAndLookup_shouldReturnResolvedText() {
		Map<String, String> valueMap = new HashMap<>();
		valueMap.put("foo", "bar");
		valueMap.put("abc", "xyz");
		Lookup lookup = new MapLookup(valueMap);
		String result = variableResolver.resolve("Der Wert von ${foo} ist ${abc}", lookup);
		assertNotNull(result);
		assertEquals("Der Wert von bar ist xyz", result);
	}
	
	@Test
	public void resolve_givenTemplateAndLookupWithMissingValue_shouldReturnResolvedTextWithUnresolvedPlaceholder() {
		Map<String, String> valueMap = new HashMap<>();
		valueMap.put("foo", "bar");
		Lookup lookup = new MapLookup(valueMap);
		String result = variableResolver.resolve("Der Wert von ${foo} ist ${abc}", lookup);
		assertNotNull(result);
		assertEquals("Der Wert von bar ist ${abc}", result);
	}
	
	@Test
	public void resolve_givenTemplateAndNullAsLookup_shouldReturnTemplateWithUnresolvedPlaceholders() {
		String template = "Der Wert von ${foo} ist ${abc}";
		String result = variableResolver.resolve(template, null);
		assertNotNull(result);
		assertEquals(template, result);
	}
	
}
