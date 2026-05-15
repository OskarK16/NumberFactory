package io.github.NumberFactory.model.components.logic;

public class GreaterOrEqualComponent extends LogicComponent {
    @Override
    public boolean evaluate(Integer a, Integer b) {
        return a >= b;
    }
}
