package io.github.NumberFactory.model.components.utility;
import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;
import io.github.NumberFactory.model.SimulationLogger;

import java.util.Map;

public class CopyComponent extends Component {
    private Item slotA;
    private Directions pendingA, pendingB;

    public CopyComponent() {
        super(1, 2);
    }

    @Override
    public boolean canReceive(Directions fromDir) {
        return getPort(fromDir) == PortType.INPUT_A && slotA == null;
    }

    @Override
    public boolean receive(Directions fromDir, Item item) {
        if (canReceive(fromDir)) {
            slotA = item;
            return true;
        }
        return false;
    }

    @Override
    public void computeTick(SimulationLogger logger) {
        pendingA = null;
        pendingB = null;
        if (slotA == null) {
            return;
        }

        for (Directions dir : Directions.values()) {
            if (getPort(dir) == PortType.OUTPUT_A) {
                pendingA = dir;
            }
            else if (getPort(dir) == PortType.OUTPUT_B) {
                pendingB = dir;
            }
        }
    }

    @Override
    public void applyTick(SimulationLogger logger) {
        if (slotA != null && pendingA != null && pendingB != null) {
            Component adjacentA = getAdjacent(pendingA);
            Component adjacentB = getAdjacent(pendingB);
            boolean canA = adjacentA != null && adjacentA.canReceive(pendingA.opposite());
            boolean canB = adjacentB != null && adjacentB.canReceive(pendingB.opposite());
            if (canA && canB) {
                adjacentA.receive(pendingA.opposite(), new Item(slotA.getValue()));
                adjacentB.receive(pendingB.opposite(), new Item(slotA.getValue()));

                if (logger != null) {
                    logger.log("Copied " + slotA.getValue());
                }

                slotA = null;
            }
        }
        pendingA = null;
        pendingB = null;
    }

    @Override
    public boolean checkValidity() {
        return PortType.check(getPorts().values(), Map.of(
            PortType.INPUT_A, 1,
            PortType.OUTPUT_A, 1,
            PortType.OUTPUT_B, 1,
            PortType.CLOSED, 1
        ));
    }

    @Override
    public void reset() {
        this.slotA = null;
        this.pendingA = null;
        this.pendingB = null;
    }

    @Override
    public java.util.List<Item> getHeldItems() {
        return slotA == null ? java.util.Collections.emptyList() : java.util.List.of(slotA);
    }
}
