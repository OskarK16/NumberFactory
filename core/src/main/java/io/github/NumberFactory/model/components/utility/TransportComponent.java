package io.github.NumberFactory.model.components.utility;

import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.model.SimulationLogger;
import io.github.NumberFactory.utils.PortType;

import java.util.Map;

public class TransportComponent extends Component {
    private Item slotA;
    private Item slotB;
    private Directions pendingDirA;
    private Directions pendingDirB;

    public TransportComponent() {
        super(2, 2);
    }

    public boolean hasItem()  { return slotA != null || slotB != null; }
    public Item    getSlotA() { return slotA; }
    public Item    getSlotB() { return slotB; }

    @Override
    public boolean canReceive(Directions fromDir) {
        PortType port = getPort(fromDir);
        if (port == PortType.INPUT_A) return slotA == null;
        if (port == PortType.INPUT_B) return slotB == null;
        return false;
    }

    @Override
    public boolean receive(Directions fromDir, Item item) {
        PortType port = getPort(fromDir);
        if (port == PortType.INPUT_A && slotA == null) { slotA = item; return true; }
        if (port == PortType.INPUT_B && slotB == null) { slotB = item; return true; }
        return false;
    }

    @Override
    public void computeTick(SimulationLogger logger) {
        pendingDirA = null;
        pendingDirB = null;
        Directions outA = findPort(PortType.OUTPUT_A);
        Directions outB = findPort(PortType.OUTPUT_B);
        if (slotA != null) pendingDirA = outA;
        if (slotB != null) pendingDirB = (outB != null) ? outB : outA;
    }

    @Override
    public void applyTick(SimulationLogger logger) {
        if (pendingDirA != null && slotA != null) {
            Component neighbor = getAdjacent(pendingDirA);
            if (neighbor != null && neighbor.receive(pendingDirA.opposite(), slotA)) {slotA = null;}
        }
        if (pendingDirB != null && slotB != null) {
            Component neighbor = getAdjacent(pendingDirB);
            if (neighbor != null && neighbor.receive(pendingDirB.opposite(), slotB)) {slotB = null;}
        }
        pendingDirA = null;
        pendingDirB = null;
    }

    @Override
    public boolean checkValidity() {
        return PortType.check(getPorts().values(), Map.of(
            PortType.INPUT_A, 1,
            PortType.INPUT_B, 0,
            PortType.OUTPUT_A, 1,
            PortType.OUTPUT_B, 0,
            PortType.CLOSED, 2
        )) || PortType.check(getPorts().values(), Map.of(
            PortType.INPUT_A, 1,
            PortType.INPUT_B, 1,
            PortType.OUTPUT_A, 1,
            PortType.OUTPUT_B, 1,
            PortType.CLOSED, 0
        )) || PortType.check(getPorts().values(), Map.of(
            PortType.INPUT_A, 1,
            PortType.INPUT_B, 1,
            PortType.OUTPUT_A, 1,
            PortType.CLOSED, 1
        ));
    }

    @Override
    public void reset() {
        slotA = null;
        slotB = null;
        pendingDirA = null;
        pendingDirB = null;
    }

    @Override
    public java.util.List<Item> getHeldItems() {
        java.util.List<Item> items = new java.util.ArrayList<>(2);
        if (slotA != null) items.add(slotA);
        if (slotB != null) items.add(slotB);
        return items;
    }

    private Directions findPort(PortType type) {
        for (Directions dir : Directions.values()) {
            if (getPort(dir) == type) return dir;
        }
        return null;
    }
}
