package io.github.NumberFactory.model.components.arithmetic;

import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

public class AddComponent extends ArithmeticComponent {
    public AddComponent() {
        super();
    }

    @Override
    protected Integer compute(Integer a, Integer b) {
        return a + b;
    }

    public static void main(String[] s) {
        Component init1 = new AddComponent();
        Component init2 = new AddComponent();
        Component adder = new AddComponent();

        init1.setPort(Directions.NORTH, PortType.INPUT_B);
        init1.setPort(Directions.EAST, PortType.OUTPUT_A);
        init1.setPort(Directions.SOUTH, PortType.CLOSED);
        init1.setPort(Directions.WEST, PortType.INPUT_A);

        init1.components[Directions.EAST.ordinal()] = adder;

        init2.setPort(Directions.NORTH, PortType.INPUT_A);
        init2.setPort(Directions.EAST, PortType.CLOSED);
        init2.setPort(Directions.SOUTH, PortType.OUTPUT_A);
        init2.setPort(Directions.WEST, PortType.INPUT_B);

        init2.components[Directions.NORTH.ordinal()] = adder;

        adder.setPort(Directions.NORTH, PortType.CLOSED);
        adder.setPort(Directions.EAST, PortType.OUTPUT_A);
        adder.setPort(Directions.SOUTH, PortType.INPUT_A);
        adder.setPort(Directions.WEST, PortType.INPUT_B);

        adder.components[Directions.WEST.ordinal()] = init1;
        adder.components[Directions.SOUTH.ordinal()] = init2;

        init1.outStorage[0] = new Item(5);
        init2.outStorage[0] = new Item(7);

        adder.process();
    }
}


