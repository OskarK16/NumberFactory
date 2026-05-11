package io.github.NumberFactory.model.components;

import io.github.NumberFactory.model.Item;

public abstract class Component {
    public final int inputSize;
    public final int outputSize;
    protected InputType[] input;
    protected Item[] output;

    public Component(int inputSize, int outputSize) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.input = new InputType[inputSize];
        this.output = new Item[outputSize];
    }

    public void connectInput(int inputIndex, int index, Component c) {
        if (inputIndex >= inputSize) throw new RuntimeException();
        input[inputIndex] = new InputType(index, c);
    }

    public void disconnectInput(int inputIndex) {
        if (inputIndex >= inputSize) throw new RuntimeException();
        input[inputIndex] = null;
    }

    public Item passOutput(int outputIndex) {
        if (outputIndex >= outputSize) throw new RuntimeException();
        return output[outputIndex];
    }

    public void generateOutput() {

    }

    public Item runMachine() {
        //TODO
        return null;
    }
}
