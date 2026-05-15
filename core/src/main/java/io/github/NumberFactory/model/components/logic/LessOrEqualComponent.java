package io.github.NumberFactory.model.components.logic;

//public class LessOrEqualComponent {
//    // TODO - LESS THAN OR EQUAL COMPONENT
//}


public class LessOrEqualComponent extends LogicComponent {
    @Override
    public boolean evaluate(Integer a, Integer b) {
        return a <= b;
    }
}
