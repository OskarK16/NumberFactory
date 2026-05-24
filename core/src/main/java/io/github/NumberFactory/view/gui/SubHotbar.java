package io.github.NumberFactory.view.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import io.github.NumberFactory.controller.ComponentPaletteSelection;
import io.github.NumberFactory.controller.HotbarController;
import io.github.NumberFactory.view.render.TextureRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

public class SubHotbar extends Table {

    private static final int SLOT_SIZE = 48;

    private final HotbarController hotbar;
    private final List<Slot> arithmeticSlots = new ArrayList<>();
    private final List<Slot> logicSlots = new ArrayList<>();
    private final Table arithmeticRow;
    private final Table logicRow;

    public SubHotbar(HotbarController hotbar, TextureRegistry textures, Skin skin) {
        this.hotbar = hotbar;

        setBackground(skin.getDrawable("panel"));
        pad(6);

        arithmeticRow = buildRow(hotbar.getArithmetic(), textures, arithmeticSlots,
            hotbar::selectFromArithmetic);
        logicRow = buildRow(hotbar.getLogic(), textures, logicSlots,
            hotbar::selectFromLogic);

        add(arithmeticRow);
        add(logicRow);
    }

    private static Table buildRow(ComponentPaletteSelection palette, TextureRegistry textures,
                                  List<Slot> slotsOut, IntConsumer onClick) {
        Table row = new Table();
        for (int i = 0; i < palette.getEntries().size(); i++) {
            final int index = i;
            ComponentPaletteSelection.Entry entry = palette.getEntries().get(i);
            Slot slot = new Slot(entry, textures);
            slot.addListener(new ClickListener() {
                @Override public void clicked(InputEvent event, float x, float y) {
                    onClick.accept(index);
                }
            });
            row.add(slot).size(SLOT_SIZE).padRight(4);
            slotsOut.add(slot);
        }
        return row;
    }

    public void update() {
        HotbarController.SubCategory open = hotbar.getOpenSub();
        setVisible(open != null);
        arithmeticRow.setVisible(open == HotbarController.SubCategory.ARITHMETIC);
        logicRow.setVisible(open == HotbarController.SubCategory.LOGIC);

        boolean arithActive = hotbar.getActivePalette() == hotbar.getArithmetic();
        int arithIdx = hotbar.getArithmetic().getSelectedIndex();
        for (int i = 0; i < arithmeticSlots.size(); i++) {
            arithmeticSlots.get(i).setSelected(arithActive && i == arithIdx);
        }
        boolean logicActive = hotbar.getActivePalette() == hotbar.getLogic();
        int logicIdx = hotbar.getLogic().getSelectedIndex();
        for (int i = 0; i < logicSlots.size(); i++) {
            logicSlots.get(i).setSelected(logicActive && i == logicIdx);
        }
    }

    static class Slot extends Table {
        private final Image selectedOverlay;
        Slot(ComponentPaletteSelection.Entry entry, TextureRegistry textures) {
            Stack stack = new Stack();
            Texture block = textures.getBlock(entry.type());
            if (block != null) stack.add(new Image(new TextureRegionDrawable(block)));
            Texture label = textures.getLabel(entry.type());
            if (label != null) stack.add(new Image(new TextureRegionDrawable(label)));
            selectedOverlay = new Image(new TextureRegionDrawable(textures.getStateSelected()));
            selectedOverlay.setVisible(false);
            stack.add(selectedOverlay);
            add(stack).grow();
        }

        void setSelected(boolean selected) {
            selectedOverlay.setVisible(selected);
        }
    }
}
