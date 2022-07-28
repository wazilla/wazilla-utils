package de.hzd.commons.text;

/**
 * <p>
 * Ein {@link VariableResolver} löst Platzhalter in einem String auf. Die Werte zu den Platzhaltern muessen ueber eine
 * Implementierung von {@link Lookup} bereitgestellt werden.
 * </p>
 * <p>
 * Diese Interface ermöglicht neben dem einfachen {@link RegexVariableResolver} auch eine Umsetzung mit Hilfe "mächtigerer"
 * Fremdbibliotheken, wie z.B. dem StringSubstitutor aus Apache commons-text oder einer vollwertigen Template-Engine wie z.B.
 * Apache Velocity.
 * </p>
 * 
 * @author Ralf Lang
 * 
 */
public interface VariableResolver {

	/**
	 * Ersetzt alle Platzhalter im übergebenen String mit den über {@link Lookup} bereitgestellten Werten. Wird <code>null</code>
	 * als Template übergeben, wird <code>nul</code> als Ergebis geliefert. Wird kein Lookup übergeben, erfolgt keine Ersetzung der
	 * Platzhalter. Ebenfalls werden Platzhalter, für die kein Wert bereitgestellt werden kann, nicht ersetzt.
	 *
	 * @param template die Vorlage mit Platzhaltern als {@link String}
	 * @param lookup {@link Lookup} zum Bereitstellen der Werte
	 * @return den String mit dem Ergebnis der Interpolation oder <code>null</code>
	 */
	public String resolve(String template, Lookup lookup);

}
