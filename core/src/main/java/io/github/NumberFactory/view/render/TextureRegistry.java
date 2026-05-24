package io.github.NumberFactory.view.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

import io.github.NumberFactory.model.components.Component;
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
import io.github.NumberFactory.model.components.utility.OutputComponent;
import io.github.NumberFactory.model.components.utility.TransportComponent;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextureRegistry implements Disposable {

    private static final String DIR = "textures/32/";

    private final Map<Class<? extends Component>, Texture> blocksByComponent = new HashMap<>();
    private final Map<Class<? extends Component>, Texture> labelsByComponent = new HashMap<>();
    private final Map<PortType, Map<Directions, Texture>> portTextures = new EnumMap<>(PortType.class);

    private Texture blockEmpty;
    private Texture templateArithmetic;
    private Texture templateLogic;
    private Texture labelAbArithmetic;
    private Texture labelAbLogic;

    private Texture stateEdit;
    private Texture stateValid;
    private Texture stateInvalid;
    private Texture stateSelected;

    public TextureRegistry() {
        loadCommon();
        loadComponentBlocks();
        loadComponentLabels();
        loadPorts();
    }

    private void loadCommon() {
        blockEmpty = load("block.png");
        templateArithmetic = load("block_template_arythmetic.png");
        templateLogic = load("block_template_logic.png");
        labelAbArithmetic = load("labels/label_ab_arythmetic.png");
        labelAbLogic = load("labels/label_ab_logic.png");

        stateEdit = load("block_states/block_edit_state.png");
        stateValid = load("block_states/block_valid_state.png");
        stateInvalid = load("block_states/block_invalid_state.png");
        stateSelected = load("block_states/block_selected_state.png");
    }

    private void loadComponentBlocks() {
        blocksByComponent.put(TransportComponent.class, load("block_transport.png"));
        blocksByComponent.put(GeneratorComponent.class, load("block_generator.png"));
        blocksByComponent.put(OutputComponent.class, load("block_output.png"));
        blocksByComponent.put(DestroyerComponent.class, load("block_destroyer.png"));
        blocksByComponent.put(CopyComponent.class, load("block_copy.png"));

        blocksByComponent.put(AddComponent.class, templateArithmetic);
        blocksByComponent.put(SubtractComponent.class, templateArithmetic);
        blocksByComponent.put(MultiplyComponent.class, templateArithmetic);
        blocksByComponent.put(ModuloComponent.class, templateArithmetic);
        blocksByComponent.put(DivideComponent.class, load("block_div.png"));

        blocksByComponent.put(EqualsComponent.class, templateLogic);
        blocksByComponent.put(NotEqualsComponent.class, templateLogic);
        blocksByComponent.put(GreaterThanComponent.class, templateLogic);
        blocksByComponent.put(GreaterOrEqualComponent.class, templateLogic);
        blocksByComponent.put(LessThanComponent.class, templateLogic);
        blocksByComponent.put(LessOrEqualComponent.class, templateLogic);
    }

    private void loadComponentLabels() {
        labelsByComponent.put(AddComponent.class, load("labels/label_add.png"));
        labelsByComponent.put(SubtractComponent.class, load("labels/label_sub.png"));
        labelsByComponent.put(MultiplyComponent.class, load("labels/label_mul.png"));
        labelsByComponent.put(ModuloComponent.class, load("labels/label_mod.png"));

        labelsByComponent.put(EqualsComponent.class, load("labels/label_eq.png"));
        labelsByComponent.put(NotEqualsComponent.class, load("labels/label_neq.png"));
        labelsByComponent.put(GreaterThanComponent.class, load("labels/label_gt.png"));
        labelsByComponent.put(GreaterOrEqualComponent.class, load("labels/label_ge.png"));
        labelsByComponent.put(LessThanComponent.class, load("labels/label_lt.png"));
        labelsByComponent.put(LessOrEqualComponent.class, load("labels/label_le.png"));
    }

    private void loadPorts() {
        for (PortType type : List.of(PortType.INPUT_A, PortType.INPUT_B, PortType.OUTPUT_A, PortType.OUTPUT_B)) {
            Map<Directions, Texture> byDir = new EnumMap<>(Directions.class);
            for (Directions dir : Directions.values()) {
                byDir.put(dir, load("ports/port_" + type.name() + "_" + letter(dir) + ".png"));
            }
            portTextures.put(type, byDir);
        }
    }

    private Texture load(String path) {
        return new Texture(Gdx.files.internal(DIR + path));
    }

    private static String letter(Directions dir) {
        return switch (dir) {
            case NORTH -> "N";
            case EAST  -> "E";
            case SOUTH -> "S";
            case WEST  -> "W";
        };
    }

    public Texture getBlock(Class<? extends Component> type) {
        return blocksByComponent.get(type);
    }
    public Texture getBlock(Component c) {
        return c == null ? null : blocksByComponent.get(c.getClass());
    }
    public Texture getLabel(Class<? extends Component> type) {
        return labelsByComponent.get(type);
    }
    public Texture getLabel(Component c) {
        return c == null ? null : labelsByComponent.get(c.getClass());
    }

    public Texture getPort(PortType type, Directions dir) {
        if (type == null || type == PortType.CLOSED) return null;
        Map<Directions, Texture> byDir = portTextures.get(type);
        return byDir == null ? null : byDir.get(dir);
    }

    public Texture getEmptyBlock()         {
        return blockEmpty;
    }
    public Texture getTemplateArithmetic() {
        return templateArithmetic;
    }
    public Texture getTemplateLogic()      {
        return templateLogic;
    }
    public Texture getLabelAbArithmetic()  {
        return labelAbArithmetic;
    }
    public Texture getLabelAbLogic()       {
        return labelAbLogic;
    }

    public Texture getStateEdit()     {
        return stateEdit;
    }
    public Texture getStateValid()    {
        return stateValid;
    }
    public Texture getStateInvalid()  {
        return stateInvalid;
    }
    public Texture getStateSelected() {
        return stateSelected;
    }

    @Override
    public void dispose() {
        java.util.Set<Texture> unique = new java.util.HashSet<>();
        unique.addAll(blocksByComponent.values());
        unique.addAll(labelsByComponent.values());
        for (Map<Directions, Texture> m : portTextures.values()) {
            unique.addAll(m.values());
        }
        unique.add(blockEmpty);
        unique.add(templateArithmetic);
        unique.add(templateLogic);
        unique.add(labelAbArithmetic);
        unique.add(labelAbLogic);
        unique.add(stateEdit);
        unique.add(stateValid);
        unique.add(stateInvalid);
        unique.add(stateSelected);
        for (Texture t : unique) {
            if (t != null) t.dispose();
        }
    }
}
