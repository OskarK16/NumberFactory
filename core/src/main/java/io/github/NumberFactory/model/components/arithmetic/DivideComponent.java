package io.github.NumberFactory.model.components.arithmetic;

public class DivideComponent extends ArithmeticComponent {
    public DivideComponent() {
        super();
    }

    @Override
    protected Integer compute(Integer a, Integer b) {
        //placeholder
        if (b == 0) return 4566769;
        return a / b;
    }
}
