package de.wazilla.utils.processes;

/**
 * Dieses Interface ermöglicht es, die Art wie ein Prozess beendet wird, zu steuern.
 * Neben {@link ProcessHandle#destroy()} sind auch Implementierungen vorstellbar, die
 * "kill" oder "taskkill" aufrufen oder auch die per JNI entsprechende Aufrufe an das
 * Betriebssystem absetzen.
 *
 * @author Ralf Lang
 */
public interface ProcessTerminationStrategy {

    void terminate(long pid) throws Exception;

}
