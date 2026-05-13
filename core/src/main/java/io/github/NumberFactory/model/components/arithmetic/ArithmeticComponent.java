package io.github.NumberFactory.model.components.arithmetic;

import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

public abstract class ArithmeticComponent extends Component {
    private Item slotA;
    private Item slotB;

    protected ArithmeticComponent() {
        super(2, 1);
    }

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
    public void tick() {
        if (slotA == null || slotB == null) return;
        for (Directions dir : Directions.values()) {
            if (getPort(dir) == PortType.OUTPUT_A) {
                Component neighbor = getAdjacent(dir);
                if (neighbor != null && neighbor.canReceive(dir.opposite())) {
                    Integer result = compute(slotA.getValue(), slotB.getValue());
                    if (result != null) {
                        neighbor.receive(dir.opposite(), new Item(result));
                        slotA = null;
                        slotB = null;
                    }
                    return;
                }
            }
        }
    }

    @Override
    public boolean checkValidity() {
        int inputA = 0, inputB = 0, outputs = 0,closed=0;
        for (Directions dir : Directions.values()) {
            PortType port = getPort(dir);
            if (port == PortType.INPUT_A)       inputA++;
            else if (port == PortType.INPUT_B)  inputB++;
            else if (port == PortType.OUTPUT_A) outputs++;
            else if (port == PortType.CLOSED)    closed++;
        }
        return inputA == 1 && inputB == 1 && outputs == 1 && closed == 1;
    }

    protected abstract Integer compute(Integer a, Integer b);
}
