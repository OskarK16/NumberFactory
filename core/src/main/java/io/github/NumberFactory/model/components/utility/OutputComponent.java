package io.github.NumberFactory.model.components.utility;

import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

import java.util.ArrayList;
import java.util.List;

public class OutputComponent extends Component {
    private final List<Item> collected = new ArrayList<>();
    private List<Item> freshlyReceived = new ArrayList<>();

    private List<Item> itemsToLog = new ArrayList<>();

    public OutputComponent() {
        super(1, 0);
    }

    @Override
    public boolean canReceive(Directions fromDir) {
        return getPort(fromDir) == PortType.INPUT_A;
    }

    @Override
    public boolean receive(Directions fromDir, Item item) {
        if (getPort(fromDir) != PortType.INPUT_A) {
            return false;
        }

        collected.add(item);
        freshlyReceived.add(item);
        itemsToLog.add(item);
        return true;
    }

    public List<Item> getCollected() {
        return collected;
    }

    public List<Item> takeFresh() {
        List<Item> out = freshlyReceived;
        freshlyReceived = new ArrayList<>();
        return out;
    }

    public List<Item> takeLogs() {
        List<Item> out = itemsToLog;
        itemsToLog = new ArrayList<>();
        return out;
    }

    @Override
    public boolean checkValidity() {
        int inputs = 0, closed = 0;
        for (Directions dir : Directions.values()) {
            PortType port = getPort(dir);
            if (port == PortType.INPUT_A)    inputs++;
            else if (port == PortType.CLOSED) closed++;
        }
        return inputs == 1 && closed == 3;
    }

    @Override
    public void reset() {
        this.collected.clear();
        this.freshlyReceived = new ArrayList<>();
        this.itemsToLog = new ArrayList<>();
    }
}
