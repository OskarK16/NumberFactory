package io.github.NumberFactory.controller;

import io.github.NumberFactory.model.components.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class ComponentPaletteSelection {

    public record Entry(Class<? extends Component> type, Supplier<? extends Component> factory) {}

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

    public Entry getSelected() {
        if (selectedIndex < 0) {
            return null;
        }
        return entries.get(selectedIndex);
    }

    public Class<? extends Component> getSelectedType() {
        Entry e = getSelected();
        return e == null ? null : e.type();
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public boolean selectByType(Class<? extends Component> type) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).type().equals(type)) {
                selectedIndex = i;
                return true;
            }
        }
        return false;
    }

    public boolean selectByIndex(int index) {
        if (index < 0 || index >= entries.size()) {
            return false;
        }
        selectedIndex = index;
        return true;
    }

    public void selectNext() {
        if (entries.isEmpty()) {
            return;
        }
        selectedIndex = (selectedIndex + 1) % entries.size();
    }

    public void selectPrev() {
        if (entries.isEmpty()) {
            return;
        }
        selectedIndex = (selectedIndex - 1 + entries.size()) % entries.size();
    }

    public Component createSelected() {
        Entry e = getSelected();
        return e == null ? null : e.factory().get();
    }
}
