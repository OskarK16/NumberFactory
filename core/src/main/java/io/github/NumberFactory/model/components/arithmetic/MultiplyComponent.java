package io.github.NumberFactory.model.components.arithmetic;

public class MultiplyComponent extends ArithmeticComponent {
    public MultiplyComponent() {
        super();
    }

    @Override
    protected Integer compute(Integer a, Integer b) {
        return a * b;
    }

    @Override
    protected String getOperationName() {
        return "Multiplied";
    }

    @Override
    protected String getOperationSymbol() {
        return "x";
    }
}
