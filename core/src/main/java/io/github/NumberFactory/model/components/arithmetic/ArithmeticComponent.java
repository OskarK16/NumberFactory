package io.github.NumberFactory.model.components.arithmetic;

import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.model.components.Component;

public abstract class ArithmeticComponent extends Component {
    protected ArithmeticComponent() {
        super(2, 1);
    }

    @Override
    public void generateOutput() {
        if (input[0] == null || input[1] == null) return;
        Item a = input[0].c().passOutput(input[0].index());
        Item b = input[1].c().passOutput(input[1].index());
        if (a == null || b == null) return;
        output[0] = new Item(compute(a.getValue(), b.getValue()));
    }

    protected abstract Integer compute(Integer a, Integer b);
}
