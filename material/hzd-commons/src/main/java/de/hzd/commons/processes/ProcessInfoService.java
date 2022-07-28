package de.hzd.commons.processes;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Dieser Dienst ermöglicht es, eine Liste von derzeit laufenden Prozessen des Betriebssystems
 * zu ermitteln. Die List kann dabei gefiltert werden. Im Gegensatz zu {@link ProcessHandle}
 * wird hier zu jedem Prozess auch die Kommandozeile zurückgegeben.
 *
 * @author Ralf Lang
 */
public class ProcessInfoService {

    public static final String SCRIPT_FILENAME = "process-list.vbs";
    public static final String OUTPUT_FILENAME = "process-list.txt";

    // Oder File.separator oder System.getProperty("line.separator")?
    private static final String LINE_SEPARATOR = "\n";
    // Als Default sind einfach alle Prozesse inkludiert
    private static final Predicate<ProcessInfo> DEFAULT_INCLUDE_FILTER = pi -> true;
    // Typischerweise nutzt man diese Klasse, wenn man die CommandLine auswerten will, ansonsten sollte man ProcessHandle benutzen
    // Daher werden per Default alle Process ausgefiltert, die keine Angabe zur CommandLine haben
    private static final Predicate<ProcessInfo> DEFAULT_EXCLUDE_FILTER = pi -> pi.getCommandLine().isBlank();
    private static final String DEFAULT_PROCESS_TERMINATION_STRATEGY_CLASSNAME = JavaProcessTerminationStrategy.class.getName();

    private File tempFolder;
    private Predicate<ProcessInfo> includeFilter;
    private Predicate<ProcessInfo> excludeFilter;

    /**
     * Erzeugt eine neue Instanz, bei der temporäre Dateien im TEMP-Ordner erstellt werden.
     * Der Filter wird zudem so eingestellt, dass alle Prozesse mit einer CommandLine
     * zurückgegeben werden.
     */
    public ProcessInfoService() {
        this.tempFolder = new File("java.io.tmpdir");
        this.includeFilter = DEFAULT_INCLUDE_FILTER;
        this.excludeFilter = DEFAULT_EXCLUDE_FILTER;
    }

    /**
     * Ändert den Ordner, in dem temporäre Dateien erstellt werden.
     *
     * @param tempFolder Ordner für temp. Dateien
     */
    public void setTempFolder(File tempFolder) {
        this.tempFolder = tempFolder;
    }

    /**
     * Ermöglicht das Filter der Prozesse. Dieses {@link Predicate} muss "true"
     * für jeden Prozess zurückgegen, der in der Liste enthalten sein soll.
     * Zudem darf der Prozess aber auch nicht Excluded sein, siehe {@link #setExcludeFilter(Predicate)}
     *
     * @param includeFilter der Include-Filter als {@link Predicate}
     */
    public void setIncludeFilter(Predicate<ProcessInfo> includeFilter) {
        this.includeFilter = includeFilter;
    }

    /**
     * Ermöglicht es, bestimmt Prozesse NICHT zur Liste hinzuzufügen. Ein Exclude wird dabei vor einem Include
     * gerprüft (Blacklist)!
     *
     * @param excludeFilter der Exclude-Filter als {@link Predicate}
     */
    public void setExcludeFilter(Predicate<ProcessInfo> excludeFilter) {
        this.excludeFilter = excludeFilter;
    }

    /**
     * Erstellt eine Liste alles Prozesse unter Berücksichtigung der eingestellten Filer.
     *
     * @return alle {@link ProcessInfo Prozess Infos} die im Filter enthalten waren.
     * @throws Exception bei Fehlern
     */
    public Collection<ProcessInfo> getProcessInfos() throws Exception {
        File scriptFile = createScript();
        File outputFile = new File(tempFolder, OUTPUT_FILENAME);
        List<String> command = new ArrayList<>();
        command.add("cscript.exe");
        command.add(scriptFile.getAbsolutePath());
        command.add("//nologo");
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(command);
        pb.redirectOutput(outputFile);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        int returnCode = process.waitFor();
        String output = readOuput(outputFile);
        if (returnCode != 0) throw new IllegalStateException("Returncode: " + returnCode + "\n" + output);
        return parseOutput(output);
    }

    public void terminate(ProcessInfo processInfo) throws Exception {
        terminate(processInfo.getPid());
    }

    /**
     * Beendet (killt) den Prozess mit der übergebenen PID
     *
     * @param pid die PID des Prozesses
     * @throws Exception bei Fehlern
     */
    public void terminate(long pid) throws Exception {
        String className = System.getProperty(JavaProcessTerminationStrategy.class.getName(), DEFAULT_PROCESS_TERMINATION_STRATEGY_CLASSNAME);
        Class<?> strategyClass = Class.forName(className);
        ProcessTerminationStrategy strategy = null;
        try {
            Constructor<?> constructor = strategyClass.getConstructor(File.class);
            strategy = (ProcessTerminationStrategy) constructor.newInstance(this.tempFolder);
        } catch (NoSuchMethodException ex) {
            strategy = (ProcessTerminationStrategy) strategyClass.getConstructor().newInstance();
        }
        strategy.terminate(pid);
    }

    private File createScript() throws Exception {
        File scriptFile = new File(this.tempFolder, SCRIPT_FILENAME);
        try (PrintWriter pw = new PrintWriter(new FileWriter(scriptFile))) {
            pw.println("Set objWMIService = GetObject(\"winmgmts:{impersonationLevel=impersonate}!\\\\.\\root\\cimv2\")");
            // pw.println("Set objProcesses = objWMIService.ExecQuery (\"SELECT * FROM Win32_Process WHERE NAME='java.exe' OR NAME='javaw.exe'\")\n");
            pw.println("Set objProcesses = objWMIService.ExecQuery (\"SELECT * FROM Win32_Process\")\n");
            pw.println("For Each objProcess In objProcesses");
            pw.println("    WSCript.Echo objProcess.ProcessId & \";\" & objProcess.Name & \";\" & objProcess.CommandLine");
            pw.println("Next");
        }
        return scriptFile;
    }

    private String readOuput(File file) throws Exception {
        List<String> lines = Files.readAllLines(file.toPath());
        return lines.stream().collect(Collectors.joining(LINE_SEPARATOR));
    }

    private Collection<ProcessInfo> parseOutput(String output) {
        Collection<ProcessInfo> processInfos = new LinkedHashSet<>();
        String[] lines = output.split(LINE_SEPARATOR);
        for (String line : lines) {
            // Die CommandLine kann auch Semikolons beinhalten (z.B. im Classpath bei Java-Prozessen)!
            // Daher können wir nicht einfach mit Hilfe von #String.split() arbeiten
            // Wir suchen daher nur die ersten beiden Semikolons und nehmen anschließend den Rest
            int indexOfFirstSeparator = line.indexOf(';');
            int indexOfSecondSeparator = line.indexOf(';', indexOfFirstSeparator + 1);
            if (indexOfFirstSeparator < 0 || indexOfSecondSeparator < 0) throw new IllegalStateException("Error parsing " + line);
            long pid = Long.parseLong(line.substring(0, indexOfFirstSeparator));
            String name = line.substring(indexOfFirstSeparator + 1, indexOfSecondSeparator);
            String commandLine = line.substring(indexOfSecondSeparator + 1);
            ProcessInfo processInfo = new ImmutableProcessInfo(pid, name, commandLine);
            // test for included and not excluded
            if (this.includeFilter.test(processInfo) && !this.excludeFilter.test(processInfo)) {
                processInfos.add(processInfo);
            }
        }
        return processInfos;
    }

}
