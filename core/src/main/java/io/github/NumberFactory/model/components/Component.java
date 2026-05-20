package io.github.NumberFactory.model.components;

import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.utils.Debug;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Component {
    public final int inputSize;
    public final int outputSize;
    private final PortType[]   ports;
    private final Component[]  adjacent;

    public Component(int inputSize, int outputSize) {
        Debug.msg("Component is created... " + this.getClass().getSimpleName());
        this.inputSize  = inputSize;
        this.outputSize = outputSize;
        this.ports      = new PortType[4];
        this.adjacent   = new Component[4];
        Arrays.fill(this.ports, PortType.CLOSED);
    }

    public void setPort(Directions direction, PortType port) {
        Debug.msg(getClass().getSimpleName() + ": setting port " + direction + " -> " + port);
        ports[direction.ordinal()] = port;
    }

    public PortType getPort(Directions direction) {
        return ports[direction.ordinal()];
    }

    public ArrayList<PortType> getPorts() {
        ArrayList<PortType> ports = new ArrayList<>();
        for (Directions d : Directions.values()) {
            ports.add(getPort(d));
        }
        return ports;
    }

    public void connect(Directions direction, Component component) {
        if (Debug.DEBUG) {
            String neighbor = component == null ? "null" : component.getClass().getSimpleName();
            Debug.msg(getClass().getSimpleName() + ": connecting " + direction + " -> " + neighbor);
        }
        adjacent[direction.ordinal()] = component;
    }

    protected Component getAdjacent(Directions direction) {
        return adjacent[direction.ordinal()];
    }

    // Called by a neighbor pushing an item — fromDir is the direction from MY perspective
    public boolean canReceive(Directions fromDir) { return false; }
    public boolean receive(Directions fromDir, Item item) { return false; }

    public List<Item> getHeldItems() { return Collections.emptyList(); }

    public abstract void tick();
    public abstract boolean checkValidity();
    public abstract void reset();

    public boolean isValid() {
        if (!checkValidity()) return false;
        for (Directions d : Directions.values()) {
            Component neighbor = getAdjacent(d);
            if (neighbor == null) continue;
            if (!PortType.areCompatible(getPort(d), neighbor.getPort(d.opposite()))) return false;
        }
        return true;
    }

    public boolean isStrictlyValid() {
        if (!isValid()) return false;
        for (Directions d : Directions.values()) {
            if (getAdjacent(d) == null && getPort(d) != PortType.CLOSED) return false;
        }
        return true;
    }
}
