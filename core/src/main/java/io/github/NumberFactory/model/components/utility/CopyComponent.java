package io.github.NumberFactory.model.components.utility;
import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

import java.util.Map;

public class CopyComponent extends Component {
    private Item slotA;

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
    public void tick() {
        if (slotA == null) {
            return;
        }

        Component adjacentA = null, adjacentB = null;
        Directions dirA = null, dirB = null;

        for (Directions dir : Directions.values()) {
            if (getPort(dir) == PortType.OUTPUT_A) {
                adjacentA = getAdjacent(dir);
                dirA = dir;
            }
            else if (getPort(dir) == PortType.OUTPUT_B) {
                adjacentB = getAdjacent(dir);
                dirB= dir;
            }
        }

        boolean validateA = adjacentA != null && adjacentA.canReceive(dirA.opposite());
        boolean validateB = adjacentB != null && adjacentB.canReceive(dirB.opposite());

        if (validateA && validateB) {
            adjacentA.receive(dirA.opposite(), new Item(slotA.getValue()));
            adjacentB.receive(dirB.opposite(), new Item(slotA.getValue()));

            slotA= null;
        }
    }

    @Override
    public boolean checkValidity() {
        return PortType.check(getPorts(), Map.of(
            PortType.INPUT_A, 1,
//            PortType.INPUT_B, 1,
            PortType.OUTPUT_A, 1,
            PortType.OUTPUT_B, 1,
            PortType.CLOSED, 1
        ));
    }

    @Override
    public void reset() {
        this.slotA = null;
    }

    @Override
    public java.util.List<Item> getHeldItems() {
        return slotA == null ? java.util.Collections.emptyList() : java.util.List.of(slotA);
    }
}
