package io.github.NumberFactory.model.components.logic;

//public class LessThanComponent {
//    // TODO - LESS THAN COMPONENT
//}

public class LessThanComponent extends LogicComponent {
    @Override
    public boolean evaluate(Integer a, Integer b) {
        return a < b;
    }
}
