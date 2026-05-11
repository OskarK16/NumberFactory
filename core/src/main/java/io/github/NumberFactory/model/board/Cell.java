package io.github.NumberFactory.model.board;

import io.github.NumberFactory.model.components.Component;

public class Cell {
    private Component component;

    public Component getComponent() { return component; }
    public void setComponent(Component component) { this.component = component; }
    public boolean isEmpty() { return component == null; }
}
