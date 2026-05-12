package io.github.NumberFactory.model.components.arithmetic;

public class SubtractComponent extends ArithmeticComponent {
    public SubtractComponent() {
        super();
    }
    @Override
    protected Integer compute(Integer a, Integer b) {
        return a - b;
    }
}
