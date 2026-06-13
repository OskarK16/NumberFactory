package io.github.NumberFactory.model;

import io.github.NumberFactory.model.components.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Inventory {
    private final boolean unlimited;
    private final Map<Class<? extends Component>, Integer> limits;
    private final Map<Class<? extends Component>, Integer> used;

    private Inventory(boolean unlimited,
                      Map<Class<? extends Component>, Integer> limits,
                      Map<Class<? extends Component>, Integer> used) {
        this.unlimited = unlimited;
        this.limits    = limits;
        this.used      = used;
    }

    public static Inventory unlimited() {
        return new Inventory(true, new HashMap<>(), new HashMap<>());
    }

    public static Inventory of(Map<Class<? extends Component>, Integer> limits) {
        return new Inventory(false, new HashMap<>(limits), new HashMap<>());
    }

    public boolean isUnlimited() { return unlimited; }

    public int limitOf(Class<? extends Component> type) {
        if (unlimited) return -1;
        return limits.getOrDefault(type, 0);
    }

    public int usedOf(Class<? extends Component> type) {
        return used.getOrDefault(type, 0);
    }

    public int countOf(Class<? extends Component> type) {
        if (unlimited) return Integer.MAX_VALUE;
        int limit = limits.getOrDefault(type, 0);
        int u = used.getOrDefault(type, 0);
        return Math.max(0, limit - u);
    }

    public boolean take(Class<? extends Component> type) {
        if (!unlimited && countOf(type) <= 0) return false;
        used.merge(type, 1, Integer::sum);
        return true;
    }

    public void give(Class<? extends Component> type) {
        int u = used.getOrDefault(type, 0);
        if (u > 0) used.put(type, u - 1);
    }

    public Map<Class<? extends Component>, Integer> getLimits() {
        return Collections.unmodifiableMap(limits);
    }

    public Map<Class<? extends Component>, Integer> getUsed() {
        return Collections.unmodifiableMap(used);
    }
}
