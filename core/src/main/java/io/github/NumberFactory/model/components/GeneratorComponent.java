package io.github.NumberFactory.model.components;

import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

public class GeneratorComponent extends Component {
    private final int seedValue;
    private boolean emitted = false;

    public GeneratorComponent(int seedValue) {
        super(0, 1);
        this.seedValue = seedValue;
    }

    @Override
    public void tick() {
        if (emitted) return;
        for (Directions dir : Directions.values()) {
            if (getPort(dir) == PortType.OUTPUT_A) {
                Component neighbor = getAdjacent(dir);
                if (neighbor != null && neighbor.canReceive(dir.opposite())) {
                    neighbor.receive(dir.opposite(), new Item(seedValue));
                    emitted = true;
                    return;
                }
            }
        }
    }

    @Override
    public boolean checkValidity() {
        int outputs = 0, closed = 0;
        for (Directions dir : Directions.values()) {
            PortType port = getPort(dir);
            if (port == PortType.OUTPUT_A)   outputs++;
            else if (port == PortType.CLOSED) closed++;
        }
        return outputs == 1 && closed == 3;
    }
}
