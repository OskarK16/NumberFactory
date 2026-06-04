package io.github.NumberFactory.model.components.arithmetic;

import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.utils.Directions;

public class DivideComponent extends ArithmeticComponent {
    public DivideComponent() {
        super();
    }

    @Override
    protected Integer compute(Integer a, Integer b) {
        if (b == 0) return null;
        return a / b;
    }

    @Override
    protected String getOperationName() {
        return "Divided";
    }

    @Override
    protected String getOperationSymbol() {
        return ":";
    }
}
