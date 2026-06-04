package io.github.NumberFactory.model.components.logic;

public class LessThanComponent extends LogicComponent {
    @Override
    public boolean evaluate(Integer a, Integer b) {
        return a < b;
    }

    @Override
    protected String getConditionSymbol() {
        return "<";
    }
}
