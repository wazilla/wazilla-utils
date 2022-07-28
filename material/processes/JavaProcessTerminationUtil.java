package de.wazilla.utils.processes;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Eine Hilfsklasse, welche er vereinfacht, alle anderen (außer dem eigenen)
 * Java-Prozess zu beenden.
 *
 * @author Ralf Lang
 */
public class JavaProcessTerminationUtil {

    /**
     * Beendet alle Java-Prozesse mit der gleichen Main-Klasse aber anderen
     * PIDs als der aktuelle Prozess.
     *
     * @throws Exception
     */
    public void terminateOtherJavaProcesses() throws Exception {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String mainClass = stackTrace[stackTrace.length - 1].getClassName();
        long pid = ProcessHandle.current().pid();
        terminateJavaProcesses(pid, mainClass);
    }

    /**
     * Beendet alle Java-Prozesse mit den übergebenen Main-Klassen, außer dem Prozess
     * mit der angegebenen PID (typischerweise der eigene Prozess).
     *
     * @param excludedPid    PID des Prozesses, welcher NICHT terminiert werden soll
     * @param mainClassNames Namen der Main-Klassen der Java-Prozesse
     * @throws Exception bei Fehlern
     */
    public void terminateJavaProcesses(long excludedPid, String... mainClassNames) throws Exception {
        terminateJavaProcesses(List.of(mainClassNames), List.of(excludedPid));
    }


    /**
     * Beendet alle Java-Prozesse mit den übergebenen Main-Klassen, außer den Prozessen
     * mit der angegebenen PID.
     *
     * @param mainClassNames Namen der Main-Klassen der Java-Prozesse
     * @param excludedPids   PIDs der Prozesse, welche NICHT terminiert werden soll
     * @throws Exception bei Fehlern
     */
    public void terminateJavaProcesses(List<String> mainClassNames, List<Long> excludedPids) throws Exception {
        ProcessInfoService service = new ProcessInfoService();
        service.setIncludeFilter(pi -> mainClassNames.stream().anyMatch(className -> pi.getCommandLine().contains(className)));
        service.setExcludeFilter(pi -> excludedPids.stream().anyMatch(pid -> pi.getPid() == pid));
        Collection<ProcessInfo> processInfos = service.getProcessInfos();
        for (ProcessInfo pi : processInfos) {
            service.terminate(pi.getPid());
        }
    }

}
