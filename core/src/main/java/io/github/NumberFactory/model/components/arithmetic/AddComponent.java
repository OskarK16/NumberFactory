package io.github.NumberFactory.model.components.arithmetic;

public class AddComponent extends ArithmeticComponent {
    public AddComponent() {
        super();
    }

    @Override
    protected Integer compute(Integer a, Integer b) {
        return a + b;
    }

    @Override
    protected String getOperationName() {
        return "Added";
    }

    @Override
    protected String getOperationSymbol() {
        return "+";
    }
}
