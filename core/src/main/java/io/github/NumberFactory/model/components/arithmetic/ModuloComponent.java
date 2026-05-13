package io.github.NumberFactory.model.components.arithmetic;

public class ModuloComponent extends ArithmeticComponent {
    public ModuloComponent() {
        super();
    }

    @Override
    protected Integer compute(Integer a, Integer b) {
        if (b == 0) return null;
        return a % b;
    }
}
