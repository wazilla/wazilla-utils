package de.hzd.commons.text;

import java.util.Map;

/**
 * Eine einfach Implementierung von {@link Lookup} auf Basis einer {@link Map}.
 * 
 * @author Ralf Lang
 *
 */
public class MapLookup implements Lookup {

	private Map<String, String> valueMap;

	/**
	 * Erzeugte eine Instanz, welche die Wert aus der übergebenen {@link Map} bereitstellt.
	 * 
	 * @param valueMap die Map mit den Werten
	 */
	public MapLookup(Map<String, String> valueMap) {
		this.valueMap = valueMap;
	}

	@Override
	public String lookup(String key) {
		if (key == null) return null;
		return valueMap.get(key);
	}

}
