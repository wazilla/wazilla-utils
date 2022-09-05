package de.wazilla.utils.text;

/**
 * Ein {@link Lookup} wird im Rahmen eines {@link VariableResolver} verwendet, um den Wert zu einem gefundenen Key bereit zu
 * stellen.
 * 
 * @author Ralf Lang
 * 
 */
@FunctionalInterface
public interface Lookup {

	/**
	 * Gibt den Wert zum übergebene Key zurück. Wenn der Key <code>null</code> ist oder wenn kein Wert zu diesem Key vorhanden ist,
	 * wird <code>null</code> zurückgegeben.
	 * 
	 * @param key der Key zum gesuchten Wert
	 * @return der Wert als {@link String} oder <code>null</code>
	 */
	String lookup(String key);

}
