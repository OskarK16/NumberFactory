package io.github.NumberFactory.controller;

import io.github.NumberFactory.model.components.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class ComponentPaletteSelection {

    public record Entry(Class<? extends Component> type, Supplier<? extends Component> factory) {}

    private Component selectedComponent = null;
    private final List<Entry> entries = new ArrayList<>();
    private int selectedIndex = -1;

    public void add(Class<? extends Component> type, Supplier<? extends Component> factory) {
        entries.add(new Entry(type, factory));
        if (selectedIndex < 0) {
            selectedIndex = 0;
        }
    }

    public List<Entry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public int size() {
        return entries.size();
    }

    public Component getSelectedComponent() {
        return selectedComponent;
    }

    public Entry getSelected() {
        if (selectedIndex < 0) {
            return null;
        }
        return entries.get(selectedIndex);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public boolean selectByIndex(int index) {
        if (index < 0 || index >= entries.size()) {
            return false;
        }
        selectedIndex = index;
        selectedComponent = createSelected();
        return true;
    }


    public Component createSelected() {
        Entry e = getSelected();
        return e == null ? null : e.factory().get();
    }
}
