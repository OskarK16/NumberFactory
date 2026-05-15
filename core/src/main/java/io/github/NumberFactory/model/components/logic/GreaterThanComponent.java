package io.github.NumberFactory.model.components.logic;

//public class GreaterThanComponent {
////     TODO - GREATER THAN COMPONENT
//
//}

public class GreaterThanComponent extends LogicComponent {
    @Override
    public boolean evaluate(Integer a, Integer b) {
        return a > b;
    }
}
