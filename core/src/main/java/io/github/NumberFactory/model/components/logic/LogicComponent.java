package io.github.NumberFactory.model.components.logic;

import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;
import io.github.NumberFactory.model.SimulationLogger;

import java.util.Map;

public abstract class LogicComponent extends Component {
    private Item slotA, slotB;
    private Directions pendingDir;
    private Item pendingItem;
    private Boolean evalResult;

    LogicComponent() {
        super(2, 2);
    }

    @Override
    public boolean canReceive(Directions fromDir) {
        PortType port = getPort(fromDir);
        if (port == PortType.INPUT_A) {
            return this.slotA == null;
        }
        if (port == PortType.INPUT_B) {
            return this.slotB == null;
        }

        return false;
    }

    @Override
    public boolean receive(Directions fromDir, Item item) {
        PortType port = getPort(fromDir);
        if (port == PortType.INPUT_A && slotA == null) {
            slotA = item;
            return true;
        }
        if (port == PortType.INPUT_B && slotB == null) {
            slotB = item;
            return true;
        }

        return false;
    }

    public abstract boolean evaluate(Integer a, Integer b);
    // to be implemented..

    @Override
    public void computeTick(SimulationLogger logger) {
        pendingDir = null;
        pendingItem = null;
        evalResult = null;

        if (slotA == null || slotB == null) {
            return;
        }

        evalResult = evaluate(slotA.getValue(), slotB.getValue());
//        boolean isTrue = evaluate(slotA.getValue(), slotB.getValue());
        PortType outPort = evalResult ? PortType.OUTPUT_A : PortType.OUTPUT_B;

        for (Directions dir : Directions.values()) {
            if (getPort(dir) == outPort) {
                pendingDir = dir;
                pendingItem = new Item(slotA.getValue());
                return;
            }
        }
    }

    @Override
    public void applyTick(SimulationLogger logger) {
        if (pendingDir == null) {
            return;
        }

        Component adjacent = getAdjacent(pendingDir);
        if (adjacent != null && adjacent.receive(pendingDir.opposite(), pendingItem)) {

            if (logger != null) {
                logger.log("Compared " + slotA.getValue() + " " + getConditionSymbol() + " " + slotB.getValue() + " -> " + evalResult.toString().toUpperCase());
            }

            slotA = null;
            slotB = null;
            evalResult = null;

        }
        pendingDir = null;
        pendingItem = null;
    }

    @Override
    public boolean checkValidity() {
        return PortType.check(getPorts().values(), Map.of(
            PortType.INPUT_A, 1,
            PortType.INPUT_B, 1,
            PortType.OUTPUT_A, 1,
            PortType.OUTPUT_B, 1
        ));
    }

    @Override
    public void reset() {
        this.slotA = null;
        this.slotB = null;
        this.pendingDir = null;
        this.pendingItem = null;
        this.evalResult = null;
    }

    @Override
    public java.util.List<Item> getHeldItems() {
        java.util.List<Item> items = new java.util.ArrayList<>(2);
        if (slotA != null) {
            items.add(slotA);
        }
        if (slotB != null) {
            items.add(slotB);
        }
        return items;
    }

    protected abstract String getConditionSymbol();
}
