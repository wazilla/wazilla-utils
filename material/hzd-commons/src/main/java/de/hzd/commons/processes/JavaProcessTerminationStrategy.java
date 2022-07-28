package de.hzd.commons.processes;

import java.util.Optional;

class JavaProcessTerminationStrategy implements ProcessTerminationStrategy {

    @Override
    public void terminate(long pid) throws Exception {
        Optional<ProcessHandle> processHandle = ProcessHandle.allProcesses().filter(ph -> ph.pid() == pid).findFirst();
        if (processHandle.isPresent()) {
            boolean success = processHandle.get().destroy();
            if (!success) {
                if (!processHandle.get().destroyForcibly()) throw new IllegalStateException("unable to kill process with id " + pid);
            }
        }
    }

}
