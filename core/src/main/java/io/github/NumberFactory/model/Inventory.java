package io.github.NumberFactory.model;

import io.github.NumberFactory.model.components.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Inventory {
    private final Map<Class<? extends Component>, Integer> stock;
    private final boolean unlimited;

    private Inventory(Map<Class<? extends Component>, Integer> stock, boolean unlimited) {
        this.stock = stock;
        this.unlimited = unlimited;
    }

    public static Inventory unlimited() {
        return new Inventory(new HashMap<>(), true);
    }

    public static Inventory of(Map<Class<? extends Component>, Integer> initial) {
        return new Inventory(new HashMap<>(initial), false);
    }

    public boolean isUnlimited() { return unlimited; }

    public int countOf(Class<? extends Component> type) {
        if (unlimited) return Integer.MAX_VALUE;
        return stock.getOrDefault(type, 0);
    }

    public boolean has(Class<? extends Component> type) {
        if (unlimited) return true;
        return stock.getOrDefault(type, 0) > 0;
    }

    public boolean take(Class<? extends Component> type) {
        if (unlimited) return true;
        int n = stock.getOrDefault(type, 0);
        if (n <= 0) return false;
        stock.put(type, n - 1);
        return true;
    }

    public void give(Class<? extends Component> type) {
        if (unlimited) return;
        stock.merge(type, 1, Integer::sum);
    }
}
