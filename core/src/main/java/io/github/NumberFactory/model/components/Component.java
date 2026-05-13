package io.github.NumberFactory.model.components;

import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.utils.PortType;
import io.github.NumberFactory.utils.Directions;

import java.util.ArrayList;

public abstract class Component {
    public final int inputSize;
    public final int outputSize;
    protected PortType[] ports;
    public Component[] components;
    public Item[] outStorage;

    public Component(int inputSize, int outputSize) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.ports = new PortType[4];
        this.components = new Component[4];
        this.outStorage = new Item[outputSize];
    }

    public void setPort(Directions direction, PortType port) {
        ports[direction.ordinal()] = port;
    }

    public void updateComponent(Directions direction, Component component) {
        components[direction.ordinal()] = component;
    }

    public abstract Item passOutput(Directions requestedFrom);
    public abstract void process();
    public abstract boolean checkValidity();
}
