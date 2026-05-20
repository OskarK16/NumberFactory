package io.github.NumberFactory.model.components.logic;

import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.model.Item;
//import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

//import javax.sound.sampled.Port;
import java.util.Map;

public abstract class LogicComponent extends Component {
    private Item slotA, slotB;
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
    public void tick() {
        if (slotA == null || slotB == null) {
            return;
        }

        boolean isTrue = evaluate(slotA.getValue(), slotB.getValue());
        PortType outPort;

        if (isTrue) {
            outPort = PortType.OUTPUT_A;
        }
        else {
            outPort = PortType.OUTPUT_B;
        }

        for (Directions dir : Directions.values()) {
            if (getPort(dir) == outPort) {
                Component adjacent = getAdjacent(dir);

                if (adjacent != null && adjacent.canReceive(dir.opposite())) {
                    adjacent.receive(dir.opposite(), new Item(slotA.getValue()));

                    slotA = null;
                    slotB = null;
                    return;
                }
            }
        }
    }

    @Override
    public boolean checkValidity() {
        return PortType.check(getPorts(), Map.of(
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
    }

    @Override
    public java.util.List<Item> getHeldItems() {
        java.util.List<Item> items = new java.util.ArrayList<>(2);
        if (slotA != null) items.add(slotA);
        if (slotB != null) items.add(slotB);
        return items;
    }
}
