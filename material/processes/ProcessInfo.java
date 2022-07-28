package de.wazilla.utils.processes;

/**
 * Ein einfache Schnittstelle mit Informationen zu einem Prozess des Betriebssystems.
 *
 * @author Ralf Lang
 */
public interface ProcessInfo {

    /**
     * Die PID ist die eindeutige Id eines Prozesses.
     *
     * @return die PID als long
     */
    long getPid();

    /**
     * Liefert den Namen des ausgeführten Prozesses, z.B. "java.exe"
     *
     * @return den Namen des Prozesses
     */
    String getName();

    /**
     * Liefert den kompletten Kommandozeilen-Aufruf der zum Starten dieses Prozesses
     * vorwendet wurde.
     *
     * @return die Kommandozeile als {@link String}
     */
    String getCommandLine();

}
