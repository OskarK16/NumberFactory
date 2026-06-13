package io.github.NumberFactory.utils;

import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.model.components.arithmetic.*;
import io.github.NumberFactory.model.components.logic.*;
import io.github.NumberFactory.model.components.utility.*;

import java.util.function.IntFunction;

public enum ComponentType {
    GENERATOR (GeneratorComponent.class, GeneratorComponent::new),
    DESTROYER (DestroyerComponent.class, v -> new DestroyerComponent()),
    TRANSPORT (TransportComponent.class, v -> new TransportComponent()),
    NEUTRAL (NeutralComponent.class, v -> new NeutralComponent()),
    COPY (CopyComponent.class, v -> new CopyComponent()),
    ADD (AddComponent.class, v -> new AddComponent()),
    SUBTRACT (SubtractComponent.class, v -> new SubtractComponent()),
    MULTIPLY (MultiplyComponent.class, v -> new MultiplyComponent()),
    DIVIDE (DivideComponent.class, v -> new DivideComponent()),
    MODULO (ModuloComponent.class, v -> new ModuloComponent()),
    EQUALS (EqualsComponent.class, v -> new EqualsComponent()),
    NOT_EQUALS (NotEqualsComponent.class, v -> new NotEqualsComponent()),
    LESS_THAN (LessThanComponent.class, v -> new LessThanComponent()),
    LESS_OR_EQUAL (LessOrEqualComponent.class, v -> new LessOrEqualComponent()),
    GREATER_THAN (GreaterThanComponent.class, v -> new GreaterThanComponent()),
    GREATER_OR_EQUAL (GreaterOrEqualComponent.class, v -> new GreaterOrEqualComponent()),
    OUTPUT (OutputComponent.class, v -> new OutputComponent());

    private final Class<? extends Component> cls;
    private final IntFunction<Component> factory;

    ComponentType(Class<? extends Component> cls, IntFunction<Component> factory) {
        this.cls = cls;
        this.factory = factory;
    }

    public Class<? extends Component> componentClass() { return cls; }

    public Component create(int value) { return factory.apply(value); }


    public static ComponentType of(Component c) {
        return of(c.getClass());
    }

    public static ComponentType of(Class<? extends Component> cls) {
        for (ComponentType t : values()) {
            if (t.cls == cls) return t;
        }
        return null;
    }
}
