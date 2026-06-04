package io.github.NumberFactory.controller;

import io.github.NumberFactory.model.components.arithmetic.AddComponent;
import io.github.NumberFactory.model.components.arithmetic.DivideComponent;
import io.github.NumberFactory.model.components.arithmetic.ModuloComponent;
import io.github.NumberFactory.model.components.arithmetic.MultiplyComponent;
import io.github.NumberFactory.model.components.arithmetic.SubtractComponent;
import io.github.NumberFactory.model.components.logic.EqualsComponent;
import io.github.NumberFactory.model.components.logic.GreaterOrEqualComponent;
import io.github.NumberFactory.model.components.logic.GreaterThanComponent;
import io.github.NumberFactory.model.components.logic.LessOrEqualComponent;
import io.github.NumberFactory.model.components.logic.LessThanComponent;
import io.github.NumberFactory.model.components.logic.NotEqualsComponent;
import io.github.NumberFactory.model.components.utility.CopyComponent;
import io.github.NumberFactory.model.components.utility.DestroyerComponent;
import io.github.NumberFactory.model.components.utility.GeneratorComponent;
import io.github.NumberFactory.model.components.utility.NeutralComponent;
import io.github.NumberFactory.model.components.utility.OutputComponent;
import io.github.NumberFactory.model.components.utility.TransportComponent;

public final class HotbarFactory {

    private HotbarFactory() {
    }

    public static HotbarController buildDefault() {
        ComponentPaletteSelection utility = new ComponentPaletteSelection();
        utility.add(TransportComponent.class, TransportComponent::new);
        utility.add(GeneratorComponent.class, () -> new GeneratorComponent(1));
        utility.add(DestroyerComponent.class, DestroyerComponent::new);
        utility.add(OutputComponent.class, OutputComponent::new);
        utility.add(CopyComponent.class, CopyComponent::new);
        utility.add(NeutralComponent.class, NeutralComponent::new);

        ComponentPaletteSelection arithmetic = new ComponentPaletteSelection();
        arithmetic.add(AddComponent.class, AddComponent::new);
        arithmetic.add(SubtractComponent.class, SubtractComponent::new);
        arithmetic.add(MultiplyComponent.class, MultiplyComponent::new);
        arithmetic.add(DivideComponent.class, DivideComponent::new);
        arithmetic.add(ModuloComponent.class, ModuloComponent::new);

        ComponentPaletteSelection logic = new ComponentPaletteSelection();
        logic.add(EqualsComponent.class, EqualsComponent::new);
        logic.add(NotEqualsComponent.class, NotEqualsComponent::new);
        logic.add(GreaterThanComponent.class, GreaterThanComponent::new);
        logic.add(GreaterOrEqualComponent.class, GreaterOrEqualComponent::new);
        logic.add(LessThanComponent.class, LessThanComponent::new);
        logic.add(LessOrEqualComponent.class, LessOrEqualComponent::new);

        return new HotbarController(utility, arithmetic, logic);
    }
}
