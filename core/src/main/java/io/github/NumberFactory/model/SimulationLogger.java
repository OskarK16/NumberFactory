package io.github.NumberFactory.model;

import java.util.LinkedList;
import java.util.List;

public class SimulationLogger {
    private final LinkedList<String> logs = new LinkedList<>();
    private static final int MAX_LOGS = 15;
    private int modificationCount = 0;

    public void log(String message) {
        logs.addLast(message);
        modificationCount++;

        if (logs.size() > MAX_LOGS) {
            logs.removeFirst();
        }
    }

    public List<String> getLogs() {
        return logs;
    }

    public int getModificationCount() {
        return modificationCount;
    }

    public void clear() {
        logs.clear();
        modificationCount++;
    }

}
