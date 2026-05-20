package io.github.NumberFactory.model;

import java.util.Collections;
import java.util.List;

public record Goal(List<Integer> expected) {
    public Goal(List<Integer> expected) {
        this.expected = List.copyOf(expected);
    }

    public int size() {
        return expected.size();
    }

    public boolean isPrefix(List<Integer> aggregated) {
        for (int i = 0; i < aggregated.size(); i++) {
            if (!aggregated.get(i).equals(expected.get(i))) return false;
        }
        return true;
    }

    public boolean isMet(List<Integer> aggregated) {
        return aggregated.equals(expected);
    }
}
