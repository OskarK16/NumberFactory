package io.github.NumberFactory.model.board;

import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.CellType;

public class Cell {
    public final CellType type;
    private Component component;
    private boolean inEdit = false;
    private boolean valid = false;

    public Cell(CellType type) {
        this.type = type;
    }

    public Component getComponent() { return component; }
    public void setComponent(Component component) {
        this.component = component;
    }

    public boolean isEmpty()  { return component == null; }
    public boolean isInEdit() { return inEdit; }
    public boolean isValid()  { return valid; }

    void setValid(boolean valid) {
        this.valid = valid;
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

    public boolean tick() {
        if (isEmpty()) return false;
        this.component.tick();
        return true;
    }

    public void reset() {
        if (isEmpty()) return;
        this.component.reset();
    }

    public void clear() {
        this.component = null;
        this.inEdit = false;
        this.valid = false;
    }
}
