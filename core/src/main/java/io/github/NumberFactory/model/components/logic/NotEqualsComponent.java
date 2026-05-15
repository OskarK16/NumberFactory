package io.github.NumberFactory.model.components.logic;

//public class NotEqualsComponent {
//    //TODO - NOT-EQUAL-COMPONENT
//}

public class NotEqualsComponent extends LogicComponent {
    @Override
    public boolean evaluate(Integer a, Integer b) {
        return !a.equals(b);
    }
}
