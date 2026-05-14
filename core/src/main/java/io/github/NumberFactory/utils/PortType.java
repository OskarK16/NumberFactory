package io.github.NumberFactory.utils;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public enum PortType {
    CLOSED,
    INPUT_A,
    INPUT_B,
    OUTPUT_A,
    OUTPUT_B;

    public static boolean check(List<PortType> ports, Map<PortType, Integer> expected) {
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
