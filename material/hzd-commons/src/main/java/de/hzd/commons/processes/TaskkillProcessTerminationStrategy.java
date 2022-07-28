package de.hzd.commons.processes;

/**
 * Eine {@link ProcessTerminationStrategy} welche zum Terminieren die Anwendung
 * TASKKILL.EXE von Windows aufruft.
 *
 * @author Ralf Lang
 */
public class TaskkillProcessTerminationStrategy implements ProcessTerminationStrategy {

    @Override
    public void terminate(long pid) throws Exception {
        Process process = Runtime.getRuntime().exec("cmd.exe /c taskkille /F /PID " + pid);
        process.waitFor();
    }

}
