package io.github.NumberFactory.model.components;

import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.model.SimulationLogger;
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
    private final boolean[]    portReadOnly;

    public Component(int inputSize, int outputSize) {
        Debug.msg("Component is created... " + this.getClass().getSimpleName());
        this.inputSize    = inputSize;
        this.outputSize   = outputSize;
        this.ports        = new PortType[4];
        this.adjacent     = new Component[4];
        this.portReadOnly = new boolean[4];
        Arrays.fill(this.ports, PortType.CLOSED);
    }

    public boolean setPort(Directions direction, PortType port) {
        if (isPortReadOnly(direction)) return false;
        Debug.msg(getClass().getSimpleName() + ": setting port " + direction + " -> " + port);
        ports[direction.ordinal()] = port;
        return true;
    }

    public void setPortReadOnly(Directions direction, boolean readOnly) {
        portReadOnly[direction.ordinal()] = readOnly;
    }

    public boolean isPortReadOnly(Directions direction) {
        return portReadOnly[direction.ordinal()];
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


    public List<PortType> allowedPorts() {
        List<PortType> allowed = new ArrayList<>(5);
        allowed.add(PortType.CLOSED);
        if (inputSize  >= 1) allowed.add(PortType.INPUT_A);
        if (inputSize  >= 2) allowed.add(PortType.INPUT_B);
        if (outputSize >= 1) allowed.add(PortType.OUTPUT_A);
        if (outputSize >= 2) allowed.add(PortType.OUTPUT_B);
        return allowed;
    }

    public List<PortType> availablePorts(Directions dir) {
        Component neighbor = getAdjacent(dir);
        PortType facing = PortType.CLOSED;
        if (neighbor != null) facing = neighbor.getPort(dir.opposite());

        List<PortType> available = new ArrayList<>(5);
        for (PortType p : allowedPorts()) {
            if (p != PortType.CLOSED && isUsed(p, dir)) continue;
            if (!facing.isClosed() && !PortType.areCompatible(p, facing)) continue;
            available.add(p);
        }
        return available;
    }

    private boolean isUsed(PortType port, Directions dir) {
        for (Directions d : Directions.values()) {
            if (d != dir && getPort(d) == port) return true;
        }
        return false;
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

    public boolean canReceive(Directions fromDir) { return false; }
    public boolean receive(Directions fromDir, Item item) { return false; }

    public List<Item> getHeldItems() { return Collections.emptyList(); }

    public void computeTick(SimulationLogger logger) {}
    public void applyTick(SimulationLogger logger) {}

    public boolean isExhausted() { return true; }

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
