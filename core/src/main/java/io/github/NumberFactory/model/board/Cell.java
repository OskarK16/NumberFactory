package io.github.NumberFactory.model.board;

import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.CellType;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

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

    public void reset() {
        if (isEmpty()) return;
        this.component.reset();
    }

    public void clear() {
        this.component = null;
        this.inEdit = false;
        this.valid = false;
    }

    // IN_A → IN_B → OUT_A → OUT_B → CLOSED → IN_A …
    boolean cyclePort(Directions dir) {
        if (component == null) return false;
        PortType current = component.getPort(dir);
        PortType next = switch (current) {
            case CLOSED   -> PortType.INPUT_A;
            case INPUT_A  -> PortType.INPUT_B;
            case INPUT_B  -> PortType.OUTPUT_A;
            case OUTPUT_A -> PortType.OUTPUT_B;
            case OUTPUT_B -> PortType.CLOSED;
        };
        component.setPort(dir, next);
        return true;
    }

    // IN_A → CLOSED → OUT_B → OUT_A → IN_B → IN_A …
    boolean cyclePortPrev(Directions dir) {
        if (component == null) return false;
        PortType current = component.getPort(dir);
        PortType next = switch (current) {
            case CLOSED   -> PortType.OUTPUT_B;
            case INPUT_A  -> PortType.CLOSED;
            case INPUT_B  -> PortType.INPUT_A;
            case OUTPUT_A -> PortType.INPUT_B;
            case OUTPUT_B -> PortType.OUTPUT_A;
        };
        component.setPort(dir, next);
        return true;
    }
}
