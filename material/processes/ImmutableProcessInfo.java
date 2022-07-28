package de.wazilla.utils.processes;

/**
 * Unveränderliche Implementierung von {@link ProcessInfo}.
 *
 * @author Ralf Lang
 */
public class ImmutableProcessInfo implements ProcessInfo {

    private long pid;
    private String name;
    private String commandLine;

    // package-friendly: for internal user only!
    ImmutableProcessInfo(long pid, String name, String commandLine) {
        this.pid = pid;
        this.name = name;
        this.commandLine = commandLine;
    }

    @Override
    public long getPid() {
        return pid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCommandLine() {
        return commandLine;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append("[pid=").append(pid);
        sb.append(", name='").append(name).append('\'');
        sb.append(", commandLine='").append(commandLine).append('\'');
        sb.append(']');
        return sb.toString();
    }
}
