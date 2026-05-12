package io.github.NumberFactory.model.components.arithmetic;

public class AddComponent extends ArithmeticComponent {
    public AddComponent() {
        super();
    }

    @Override
    protected Integer compute(Integer a, Integer b) {
        return a + b;
    }
}


