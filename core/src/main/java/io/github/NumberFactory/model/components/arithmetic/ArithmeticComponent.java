package io.github.NumberFactory.model.components.arithmetic;

import io.github.NumberFactory.model.Item;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

public abstract class ArithmeticComponent extends Component {
    protected ArithmeticComponent() {
        super(2, 1);
    }

    @Override
    public void process() {
        System.out.println("Checking validity");
//        if (!checkValidity()) return;
        System.out.println("VALID");
        int[] in = new int[2];
        in[0] = -1;
        for (int i = 0; i < 4; i++) {
            if (this.ports[i] == PortType.INPUT_A && in[0] == -1) in[0] = i;
            else if (this.ports[i] == PortType.INPUT_B || this.ports[i] == PortType.INPUT_A) in[1] = i;
        }

        Item a = this.components[in[0]].passOutput(Directions.values()[in[0]]);
        Item b = this.components[in[1]].passOutput(Directions.values()[in[1]]);
        if (a == null || b == null) return;
        System.out.println("Computing...");
        System.out.println(a.getValue());
        System.out.println(b.getValue());
        this.outStorage[0] = new Item(compute(a.getValue(), b.getValue()));
        System.out.println("Computed value: ");
        System.out.println(passOutput(null).getValue());
    }

    @Override
    public Item passOutput(Directions requestedFrom) {
        return this.outStorage[0];
    }

    @Override
    public boolean checkValidity() {
        int inputs = 0;
        int outputs = 0;
        for (PortType port : this.ports) {
            if (port == PortType.INPUT_A || port == PortType.INPUT_B) inputs++;
            else if (port == PortType.OUTPUT_A || port == PortType.OUTPUT_B) outputs++;
        }
        return inputs == 2 && outputs == 1;
    }

    protected abstract Integer compute(Integer a, Integer b);
}
