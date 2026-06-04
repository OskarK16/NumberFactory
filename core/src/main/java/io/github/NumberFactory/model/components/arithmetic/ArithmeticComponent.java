package io.github.NumberFactory.model.components.arithmetic;

import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;
import io.github.NumberFactory.model.SimulationLogger;

import java.util.Map;

public abstract class ArithmeticComponent extends Component {
    private Item slotA;
    private Item slotB;
    private Directions pendingDir;

    protected ArithmeticComponent() {
        super(2, 1);
    }

    @Override
    public boolean canReceive(Directions fromDir) {
        PortType port = getPort(fromDir);
        if (port == PortType.INPUT_A) {
            return slotA == null;
        }
        if (port == PortType.INPUT_B) {
            return slotB == null;
        }
        return false;
    }

    @Override
    public boolean receive(Directions fromDir, Item item) {
        PortType port = getPort(fromDir);
        if (port == PortType.INPUT_A && slotA == null) {
            slotA = item; return true;
        }
        if (port == PortType.INPUT_B && slotB == null) {
            slotB = item; return true;
        }
        return false;
    }

    @Override
    public void computeTick(SimulationLogger logger) {
        pendingDir = null;
        if (slotA == null || slotB == null) {
            return;
        }
        for (Directions dir : Directions.values()) {
            if (getPort(dir) == PortType.OUTPUT_A) {
                pendingDir = dir;
                return;
            }
        }
    }

    @Override
    public void applyTick(SimulationLogger logger) {
        if (pendingDir == null || slotA == null || slotB == null) {
            return;
        }
        Component neighbor = getAdjacent(pendingDir);
        if (neighbor != null) {

            int valA = slotA.getValue();
            int valB = slotB.getValue();

            Integer result = compute(valA, valB);
            if (result != null && neighbor.receive(pendingDir.opposite(), new Item(result))) {

                if (logger != null) {
                    logger.log(getOperationName() + " " + valA + " " + getOperationSymbol() + " " + valB + " = " + result);
                }

                slotA = null;
                slotB = null;
            }
        }
        pendingDir = null;
    }

    @Override
    public boolean checkValidity() {
        return PortType.check(getPorts(), Map.of(
            PortType.INPUT_A, 1,
            PortType.INPUT_B, 1,
            PortType.OUTPUT_A, 1,
            PortType.CLOSED, 1
        ));
    }

    @Override
    public void reset() {
        this.slotA = null;
        this.slotB = null;
        this.pendingDir = null;
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

    protected abstract Integer compute(Integer a, Integer b);
    protected abstract String getOperationName();
    protected abstract String getOperationSymbol();
}
