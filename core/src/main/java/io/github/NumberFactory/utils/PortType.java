package io.github.NumberFactory.utils;

import java.util.*;

public enum PortType {
    CLOSED,
    INPUT_A,
    INPUT_B,
    OUTPUT_A,
    OUTPUT_B;

    public boolean isInput()  { return this == INPUT_A  || this == INPUT_B; }
    public boolean isOutput() { return this == OUTPUT_A || this == OUTPUT_B; }
    public boolean isClosed() { return this == CLOSED; }

    public static boolean areCompatible(PortType mine, PortType neighbor) {
        if (mine == null || neighbor == null) return false;
        if (mine.isClosed() && neighbor.isClosed()) return true;
        if (mine.isOutput() && neighbor.isInput())  return true;
        if (mine.isInput()  && neighbor.isOutput()) return true;
        return false;
    }

    public static boolean check(Collection<PortType> ports, Map<PortType, Integer> expected) {
        Map<PortType, Integer> counts = new EnumMap<>(PortType.class);
        for (PortType port : ports) {
            counts.put(port, counts.getOrDefault(port, 0) + 1);
        }

        for (PortType type : values()) {
            int current = counts.getOrDefault(type, 0);
            int req = expected.getOrDefault(type, 0);
            if (current != req) return false;
        }
        return true;
    }
}
