package io.github.NumberFactory.model.components.utility;

import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.PortType;

import java.util.Map;

public class NeutralComponent extends Component {

    public NeutralComponent() {
        super(0, 0);
    }

    @Override
    public boolean checkValidity() {
        return PortType.check(getPorts(), Map.of(PortType.CLOSED, 4));
    }

    @Override
    public void reset() {}
}
