package de.hzd.commons.xml;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.LinkedHashSet;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * SAX {@link ErrorHandler}, welcher eine konfigurierbare Anzahl (Default: Integer.MAX_VALUE) an {@link SAXParseException}
 * sammelt. Somit wird die Verarbeitung zunächst nicht bei ersten Fehler mit einer {@link SAXException} abgebrochen. Er wenn
 * maximale Anzahl an Exceptions erreicht wird von dieser Klasse eine {@link SAXException} geworfen.
 *
 * @author Ralf Lang
 */
public class CollectingErrorHandler implements ErrorHandler {

	private static final int DEFAULT_MAX_ERRORS = Integer.getInteger(CollectingErrorHandler.class.getName() + ".maxErrors", Integer.MAX_VALUE);
	private static final boolean DEFAULT_INCLUDE_WARNINGS = Boolean.parseBoolean(System.getProperty(CollectingErrorHandler.class.getName() + ".includeWarnings", Boolean.TRUE.toString()));

	private int maxErrors;
	private boolean includeWarnings;
	private Collection<SAXParseException> exceptions;

	/**
	 * Erzeugt einen neuen Handler. Dieser sammelt per Default alle Fehler, inklusive Warnings.
	 */
	public CollectingErrorHandler() {
		this(DEFAULT_MAX_ERRORS, DEFAULT_INCLUDE_WARNINGS);
	}

	/**
	 * Erzeugt einen neuen Handler, mit der angegebenen, maximalen Anzahl an Fehlern. Warnings werden dabei (als Default) als Fehler
	 * betrachtet.
	 * 
	 * @param maxErrors maximale Anzahl an Fehlern
	 */
	public CollectingErrorHandler(int maxErrors) {
		this(maxErrors, DEFAULT_INCLUDE_WARNINGS);
	}

	/**
	 * Erzeugt einen neuen Handler, der alle Fehler sammelt.
	 * 
	 * @param includeWarnings <code>true</code>, wenn Warnungen ebenfalls gesammelt werden sollen, sonst <code>false</code>
	 */
	public CollectingErrorHandler(boolean includeWarnings) {
		this(DEFAULT_MAX_ERRORS, includeWarnings);
	}

	/**
	 * Erzeugt einen neuen Handler. Die maximale Anzahl an zu sammelnden Exceptions und ob Warnungen ebenfalls als Fehler angesehen
	 * werden kann über die mitgegebenen Parameter gesteuert werden.
	 * 
	 * @param maxErrors       max. Anzahl an Fehler
	 * @param includeWarnings <code>true</code>, wenn Warnungen ebenfalls gesammelt werden sollen, sonst <code>false</code>
	 */
	public CollectingErrorHandler(int maxErrors, boolean includeWarnings) {
		this.maxErrors = maxErrors;
		this.includeWarnings = includeWarnings;
		this.exceptions = new LinkedHashSet<SAXParseException>();
	}

	@Override
	public void warning(SAXParseException exception) throws SAXException {
		if (includeWarnings) {
			collect(exception);
		}
	}

	@Override
	public void error(SAXParseException exception) throws SAXException {
		collect(exception);
	}

	@Override
	public void fatalError(SAXParseException exception) throws SAXException {
		// TODO: gibt es Fälle, wo es sinnvoll wäre, diese fatalen Fehler ebenfalls zu sammeln?
		throw new SAXException("Schwerwiegender Fehler beim Verarbeiten des XML!", exception);
	}

	/**
	 * @return die gesammelten {@link SAXParseException}
	 */
	public Collection<SAXParseException> getExceptions() {
		return exceptions;
	}

	/**
	 * Gibt einen String zurück, welche eine Zeile pro gesammelter {@link SAXParseException} beinhaltet. Sofern Positionsangaben
	 * vorhanden (Zeile, Spalte) vorhanden sind, werden diese in Klammern hinter der Meldung ergänzt.
	 * 
	 * @return die aufbereiteten {@link SAXParseException} Meldungen
	 */
	public String getExceptionMessage() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		for (SAXParseException ex : getExceptions()) {
			pw.print(ex.getMessage());
			int lineNumber = ex.getLineNumber();
			int columnNumber = ex.getColumnNumber();
			if (lineNumber != -1 || columnNumber != -1) {
				pw.print(" (Zeile: " + ex.getLineNumber() + ", Spalte: " + ex.getColumnNumber() + ")");
			}
			pw.println();
		}
		pw.close();
		return sw.toString();
	}

	/**
	 * Über diese Methode kann abgfragt werden, ob Fehler aufgetreten sind, oder nicht.
	 * 
	 * @return <code>true</code>, wenn Fehler gesammelt wurden, sonst <code>false</code>.
	 */
	public boolean hasExceptions() {
		return exceptions.size() > 0;
	}

	private void collect(SAXParseException exception) throws SAXException {
		this.exceptions.add(exception);
		if (this.exceptions.size() >= maxErrors) {
			throw new SAXException("Die maximale Anzahl (" + maxErrors + ") an Validierungsfehlern wurde erreicht!\n" + getExceptionMessage());
		}
	}

}
