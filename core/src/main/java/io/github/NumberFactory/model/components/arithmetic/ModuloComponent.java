package io.github.NumberFactory.model.components.arithmetic;

public class ModuloComponent extends ArithmeticComponent {
    public ModuloComponent() {
        super();
    }

    @Override
    protected Integer compute(Integer a, Integer b) {
        //placeholder
        if (b == 0) return 4566769;
        return a % b;
    }
}
