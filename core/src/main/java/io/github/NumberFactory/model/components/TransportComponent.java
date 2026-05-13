package io.github.NumberFactory.model.components;

import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

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
        int inputs = 0, outputs = 0;
        for (Directions dir : Directions.values()) {
            PortType port = getPort(dir);
            if (port == PortType.INPUT_A)  inputs++;
            else if (port == PortType.OUTPUT_A) outputs++;
        }
        return inputs == 1 && outputs == 1;
    }
}
