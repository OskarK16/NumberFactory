package io.github.NumberFactory.model.components;
import io.github.NumberFactory.model.Item;

public class GeneratorComponent extends Component {
    public GeneratorComponent(Integer value) {
        super(0, 1);
        this.output[0] = new Item(value);
    }

    @Override
    public Item passOutput(int outputIndex) {
        System.out.print("Generating: ");
        System.out.println(this.output[0].getValue());
        return super.passOutput(outputIndex);
    }
}
