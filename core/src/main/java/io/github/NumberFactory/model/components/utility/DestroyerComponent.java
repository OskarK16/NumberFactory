package io.github.NumberFactory.model.components.utility;
import io.github.NumberFactory.model.SimulationLogger;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

import java.util.*;

public class DestroyerComponent extends Component {
    private Item slotA;

    public DestroyerComponent() {
        super(1, 0);
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
        if (slotA != null) {
            if (logger != null) {
                logger.log("Destoryed " + slotA.getValue());
            }

            slotA = null;
        }
    }

    @Override
    public void applyTick(SimulationLogger logger) {

    }

    @Override
    public boolean checkValidity() {
        return PortType.check(getPorts(), Map.of(
            PortType.INPUT_A, 1,
            PortType.CLOSED, 3
        ));
    }

    @Override
    public void reset() {
        slotA = null;
    }

    @Override
    public java.util.List<Item> getHeldItems() {
        return slotA == null ? Collections.emptyList() : List.of(slotA);
    }
}
