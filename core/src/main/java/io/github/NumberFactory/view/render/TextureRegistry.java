package io.github.NumberFactory.view.render;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.model.components.arithmetic.*;
import io.github.NumberFactory.model.components.logic.*;
import io.github.NumberFactory.model.components.utility.*;

import java.util.HashMap;
import java.util.Map;

// After changing textures see build.gradle lines 10 and 70
public class TextureRegistry implements Disposable {

    private final TextureAtlas atlas;

    private final Map<Class<? extends Component>, TextureRegion> blocksByComponent = new HashMap<>();
    private final Map<Class<? extends Component>, TextureRegion> labelsByComponent = new HashMap<>();

    private TextureRegion portInput;
    private TextureRegion portOutput;
    private TextureRegion portTransportInput;
    private TextureRegion portTransportOutput;

    private TextureRegion blockEmpty;
    private TextureRegion floorTile;
    private TextureRegion templateArithmetic;
    private TextureRegion templateLogic;
    private TextureRegion labelArithmetic;
    private TextureRegion labelLogic;

    private TextureRegion blockState;
    private TextureRegion itemFrame;

    public TextureRegistry() {
        atlas = new TextureAtlas("textures/atlas.atlas");
        loadCommon();
        loadComponentBlocks();
        loadComponentLabels();
    }

    private void loadCommon() {
        blockEmpty = atlas.findRegion("block");
        floorTile = atlas.findRegion("floor_tile");
        templateArithmetic = atlas.findRegion("block_template_arythmetic");
        templateLogic = atlas.findRegion("block_template_logic");
        labelArithmetic = atlas.findRegion("label_arith");
        labelLogic = atlas.findRegion("label_logic");

        blockState = atlas.findRegion("block_state");
        itemFrame = atlas.findRegion("item_frame");

        portInput = atlas.findRegion("port_INPUT");
        portOutput = atlas.findRegion("port_OUTPUT");
        portTransportInput = atlas.findRegion("port_INPUT_t");
        portTransportOutput = atlas.findRegion("port_OUTPUT_t");
    }

    private void loadComponentBlocks() {
        blocksByComponent.put(TransportComponent.class, atlas.findRegion("block_transport"));
        blocksByComponent.put(GeneratorComponent.class, atlas.findRegion("block_generator"));
        blocksByComponent.put(OutputComponent.class, atlas.findRegion("block_output"));
        blocksByComponent.put(DestroyerComponent.class, atlas.findRegion("block_destroyer"));
        blocksByComponent.put(CopyComponent.class, atlas.findRegion("block_copy"));
        blocksByComponent.put(NeutralComponent.class, atlas.findRegion("block_neutral"));

        blocksByComponent.put(ArithmeticComponent.class, templateArithmetic);
        blocksByComponent.put(LogicComponent.class, templateLogic);
    }

    private void loadComponentLabels() {
        labelsByComponent.put(AddComponent.class, atlas.findRegion("label_add"));
        labelsByComponent.put(SubtractComponent.class, atlas.findRegion("label_sub"));
        labelsByComponent.put(MultiplyComponent.class, atlas.findRegion("label_mul"));
        labelsByComponent.put(DivideComponent.class, atlas.findRegion("label_div"));
        labelsByComponent.put(ModuloComponent.class, atlas.findRegion("label_mod"));

        labelsByComponent.put(EqualsComponent.class, atlas.findRegion("label_eq"));
        labelsByComponent.put(NotEqualsComponent.class, atlas.findRegion("label_neq"));
        labelsByComponent.put(GreaterThanComponent.class, atlas.findRegion("label_gt"));
        labelsByComponent.put(GreaterOrEqualComponent.class, atlas.findRegion("label_ge"));
        labelsByComponent.put(LessThanComponent.class, atlas.findRegion("label_lt"));
        labelsByComponent.put(LessOrEqualComponent.class, atlas.findRegion("label_le"));
    }

    public TextureRegion getBlock(Class<? extends Component> type) {
        for (Class<?> c = type; Component.class.isAssignableFrom(c); c = c.getSuperclass()) {
            TextureRegion region = blocksByComponent.get(c);
            if (region != null) return region;
        }
        return null;
    }
    public TextureRegion getBlock(Component c)                     { return c == null ? null : getBlock(c.getClass()); }
    public TextureRegion getLabel(Class<? extends Component> type) { return labelsByComponent.get(type); }
    public TextureRegion getLabel(Component c)                     { return c == null ? null : labelsByComponent.get(c.getClass()); }
    public TextureRegion getPort(boolean INPUT, boolean TRANSPORT) {
        if (TRANSPORT) {
            TextureRegion t = INPUT ? portTransportInput : portTransportOutput;
            if (t != null) return t;
        }
        return INPUT ? portInput : portOutput;
    }
    public TextureRegion getPort(boolean INPUT) { return getPort(INPUT, false); }

    public TextureRegion getEmptyBlock() { return blockEmpty; }
    public TextureRegion getFloorTile() { return floorTile != null ? floorTile : blockEmpty; }
    public TextureRegion getTemplateArithmetic() { return templateArithmetic; }
    public TextureRegion getTemplateLogic() { return templateLogic; }
    public TextureRegion getLabelArithmetic() { return labelArithmetic; }
    public TextureRegion getLabelLogic() { return labelLogic; }

    public TextureRegion getState() { return blockState; }
    public TextureRegion getItemFrame() { return itemFrame; }

    @Override
    public void dispose() {
        atlas.dispose();
    }
}
