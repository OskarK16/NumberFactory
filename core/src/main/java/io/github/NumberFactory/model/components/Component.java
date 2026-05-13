package io.github.NumberFactory.model.components;

import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

import java.util.Arrays;

public abstract class Component {
    public final int inputSize;
    public final int outputSize;
    private final PortType[]   ports;
    private final Component[]  adjacent;

    public Component(int inputSize, int outputSize) {
        this.inputSize  = inputSize;
        this.outputSize = outputSize;
        this.ports      = new PortType[4];
        this.adjacent   = new Component[4];
        Arrays.fill(this.ports, PortType.CLOSED);
    }

    public void setPort(Directions direction, PortType port) {
        ports[direction.ordinal()] = port;
    }

    public PortType getPort(Directions direction) {
        return ports[direction.ordinal()];
    }

    public void connect(Directions direction, Component component) {
        adjacent[direction.ordinal()] = component;
    }

    protected Component getAdjacent(Directions direction) {
        return adjacent[direction.ordinal()];
    }

    // Called by a neighbor pushing an item — fromDir is the direction from MY perspective
    public boolean canReceive(Directions fromDir) { return false; }
    public boolean receive(Directions fromDir, Item item) { return false; }

    public abstract void tick();
    public abstract boolean checkValidity();
}
