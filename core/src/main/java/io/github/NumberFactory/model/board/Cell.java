package io.github.NumberFactory.model.board;

import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.CellStates;
import io.github.NumberFactory.utils.ComponentType;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class Cell {
    private Component component;
    private boolean inEdit   = false;
    private boolean valid    = false;
    private boolean invalid  = false;
    private boolean readOnly = false;
    private Set<ComponentType> componentFilter = EnumSet.noneOf(ComponentType.class);
    private boolean blacklist = true;

    public Cell() {}

    public Component getComponent() { return component; }

    public boolean setComponent(Component component) {
        if (readOnly) return false;
        if (component != null && this.component != null) return false;
        if (component != null && !canAccept(ComponentType.of(component))) return false;
        this.component = component;
        return true;
    }

    public boolean isEmpty()    { return component == null; }
    public boolean isInEdit()   { return inEdit; }
    public boolean isValid()    { return valid; }
    public boolean isReadOnly() { return readOnly; }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public void setFilter(Set<ComponentType> filter, boolean blacklist) {
        this.componentFilter = filter.isEmpty()
            ? EnumSet.noneOf(ComponentType.class)
            : EnumSet.copyOf(filter);
        this.blacklist = blacklist;
    }

    boolean canAccept(ComponentType type) {
        if (componentFilter.isEmpty()) return true;
        boolean inSet = componentFilter.contains(type);
        return blacklist != inSet;
    }

    public Set<ComponentType> getComponentFilter() {
        return Collections.unmodifiableSet(componentFilter);
    }

    public boolean isBlacklist() { return blacklist; }

    void setValid(boolean valid) {
        this.valid = valid;
        this.invalid = !valid;
    }

    boolean tryCommitEdit() {
        if (component == null) return false;
        if (!valid) return false;
        this.inEdit = false;
        return true;
    }

    void enterEdit() {
        if (component == null) return;
        this.inEdit = true;
    }

    public void reset() {
        if (isEmpty()) return;
        this.component.reset();
    }

    public boolean clear() {
        if (readOnly) return false;
        this.component = null;
        this.inEdit = false;
        this.valid = false;
        return true;
    }

    boolean cyclePort(Directions dir) {
        if (component == null) return false;
        java.util.List<PortType> available = component.availablePorts(dir);
        if (available.isEmpty()) return false;

        int next = (available.indexOf(component.getPort(dir)) + 1) % available.size();
        PortType target = available.get(next);
        if (target == component.getPort(dir)) return false;
        component.setPort(dir, target);
        return true;
    }

    public CellStates getState() {
        if (inEdit) return CellStates.EDIT;
        if (valid) return CellStates.VALID;
        if (invalid) return CellStates.INVALID;
        return CellStates.NULL;
    }
}
