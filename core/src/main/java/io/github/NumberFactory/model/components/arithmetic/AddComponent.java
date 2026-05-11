package io.github.NumberFactory.model.components.arithmetic;

import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.model.components.GeneratorComponent;

public class AddComponent extends Component {
    public AddComponent() {
        super(2, 1);
    }

    public void generateOutput() {
        if (input[0] == null || input[1] == null) return;
        Item a = input[0].c().passOutput(input[0].index());
        Item b = input[1].c().passOutput(input[1].index());
        System.out.print("Adding: ");
        System.out.print(a.getValue());
        System.out.print(", ");
        System.out.println(b.getValue());
        this.output[0] = new Item(a.getValue() + b.getValue());
    }

    public static void main(String[] s) {
        GeneratorComponent one = new GeneratorComponent(1);
//        GeneratorComponent two = new GeneratorComponent(2);
//        AddComponent adder = new AddComponent();
//
//        adder.connectInput(0, 0, one);
//        adder.connectInput(1, 0, two);
//        adder.generateOutput();
//
//        AddComponent adder2 = new AddComponent();
//        adder2.connectInput(0, 0, one);
//        adder2.connectInput(1, 0, adder);
//        adder2.generateOutput();
//
//        System.out.println(adder2.passOutput(0).getValue());
        AddComponent adder = new AddComponent();
        adder.connectInput(0, 0, one);
        adder.connectInput(1, 0, one);
        for (int i = 0; i < 10; i++) {
            adder.generateOutput();
            adder.connectInput(i%2, 0, adder);
        }
    }
}
