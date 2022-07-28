package de.wazilla.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Diese Klasse bietet Hilfsmethoden für {@link Throwable} an.
 *
 * @author Ralf Lang
 *
 */
public final class Throwables {

    private Throwables() {
        // Utilityklasse!
    }

    /**
     * Gib den Stacktrace des übergebenen {@link Throwable} als String zurueck.
     * Gibt <code>null</code> zurück, wenn kein {@link Throwable} übergeben wurde.
     *
     * @param thrown das aufgetretene {@link Throwable}
     * @return den Stracktrace als {@link String}
     */
    public static String toString(Throwable thrown) {
        if (thrown == null)
            return null;
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            thrown.printStackTrace(pw);
        }
        return sw.toString();
    }

    /**
     * Gibt innerhalb der Exception-Hierachie das ursprüngliche aufgetretene
     * {@link Throwable} zurück. Gibt <code>null</code> zurück, wenn kein
     * {@link Throwable} übergeben wurde.
     *
     * @param thrown das {@link Throwable}
     * @return ein {@link Throwable} mit dem untersten Fehler innerhalb der
     *         Hierachie.
     */
    public static Throwable getRootCause(Throwable thrown) {
        if (thrown == null) return null;
        Throwable rootCause = thrown;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

}
