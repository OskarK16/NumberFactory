package io.github.NumberFactory.model.components.logic;

public class NotEqualsComponent extends LogicComponent {
    @Override
    public boolean evaluate(Integer a, Integer b) {
        return !a.equals(b);
    }

    @Override
    protected String getConditionSymbol() {
        return "≠";
    }
}
