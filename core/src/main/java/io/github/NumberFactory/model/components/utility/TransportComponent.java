package io.github.NumberFactory.model.components.utility;

import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

import java.util.Map;

public class TransportComponent extends Component {
    private Item item;

    public TransportComponent() {
        super(1, 1);
    }

    public boolean hasItem() { return item != null; }

    @Override
    public boolean canReceive(Directions fromDir) {
        return item == null;
    }

    @Override
    public boolean receive(Directions fromDir, Item item) {
        if (this.item != null) return false;
        this.item = item;
        return true;
    }

    @Override
    public void tick() {
        if (item == null) return;
        for (Directions dir : Directions.values()) {
            if (getPort(dir) == PortType.OUTPUT_A) {
                Component neighbor = getAdjacent(dir);
                if (neighbor != null && neighbor.canReceive(dir.opposite())) {
                    neighbor.receive(dir.opposite(), item);
                    item = null;
                    return;
                }
            }
        }
    }

    @Override
    public boolean checkValidity() {
        return PortType.check(getPorts(), Map.of(
            PortType.INPUT_A, 1,
            PortType.OUTPUT_A, 1,
            PortType.CLOSED, 2
        ));
    }

    @Override
    public void reset() {
        this.item = null;
    }

    @Override
    public java.util.List<Item> getHeldItems() {
        return item == null ? java.util.Collections.emptyList() : java.util.List.of(item);
    }
}
