package io.github.NumberFactory.view.gui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import io.github.NumberFactory.controller.ComponentPaletteSelection;
import io.github.NumberFactory.controller.HotbarController;
import io.github.NumberFactory.model.Inventory;
import io.github.NumberFactory.view.render.TextureRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

public class SubHotbar extends Table {

    private static final int SLOT_SIZE = 60;

    private final HotbarController hotbar;
    private final Inventory inventory;
    private final List<Slot> arithmeticSlots = new ArrayList<>();
    private final List<Slot> logicSlots = new ArrayList<>();
    private final Table arithmeticRow;
    private final Table logicRow;

    public SubHotbar(HotbarController hotbar, Inventory inventory, TextureRegistry textures, Skin skin) {
        this.hotbar = hotbar;
        this.inventory = inventory;

        setBackground(skin.getDrawable("panel"));
        pad(6);

        center();

        arithmeticRow = buildRow(hotbar.getArithmetic(), textures, arithmeticSlots,
            hotbar::selectFromArithmetic, skin);
        logicRow = buildRow(hotbar.getLogic(), textures, logicSlots,
            hotbar::selectFromLogic, skin);

        Stack contentStack = new Stack();
        contentStack.add(arithmeticRow);
        contentStack.add(logicRow);

        add(contentStack).center();
    }

    private static Table buildRow(ComponentPaletteSelection palette, TextureRegistry textures, List<Slot> slotsOut, IntConsumer onClick, Skin skin) {
        Table row = new Table();

        row.center();

        for (int i = 0; i < palette.getEntries().size(); i++) {
            final int index = i;
            ComponentPaletteSelection.Entry entry = palette.getEntries().get(i);
            Slot slot = new Slot(entry, textures, skin);
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
            arithmeticSlots.get(i).refreshCount(inventory);
        }

        boolean logicActive = hotbar.getActivePalette() == hotbar.getLogic();
        int logicIdx = hotbar.getLogic().getSelectedIndex();

        for (int i = 0; i < logicSlots.size(); i++) {
            logicSlots.get(i).setSelected(logicActive && i == logicIdx);
            logicSlots.get(i).refreshCount(inventory);
        }

    }

    static class Slot extends Table {
        private final ComponentPaletteSelection.Entry entry;
        private final Image selectedOverlay;
        private final Label countLabel;

        Slot(ComponentPaletteSelection.Entry entry, TextureRegistry textures, Skin skin) {
            this.entry = entry;
            Stack stack = new Stack();
            TextureRegion block = textures.getBlock(entry.type());

            if (block != null) {
                stack.add(new Image(new TextureRegionDrawable(block)));
            }

            TextureRegion label = textures.getLabel(entry.type());

            if (label != null) {
                stack.add(new Image(new TextureRegionDrawable(label)));
            }

            Image hoverOverlay = new Image(new TextureRegionDrawable(textures.getState()));
            hoverOverlay.setColor(1f, 1f, 1f, 0.45f);
            hoverOverlay.setVisible(false);
            stack.add(hoverOverlay);

            selectedOverlay = new Image(new TextureRegionDrawable(textures.getState()));
            selectedOverlay.setVisible(false);
            stack.add(selectedOverlay);

            countLabel = new Label("", skin);
            Table corner = new Table();
            corner.bottom().right();
            corner.add(countLabel).pad(2);
            stack.add(corner);

            add(stack).grow();
            installHover(this, hoverOverlay);
        }

        void setSelected(boolean selected) {
            selectedOverlay.setVisible(selected);
        }

        void refreshCount(Inventory inventory) {
            countLabel.setText(Integer.toString(inventory.usedOf(entry.type())));
        }
    }

    private static void installHover(final Table slot, final Image hoverOverlay) {
        slot.setTransform(true);
        slot.addListener(new ClickListener() {
            @Override public void enter(InputEvent event, float x, float y, int pointer, Actor from) {
                if (pointer != -1) return;
                hoverOverlay.setVisible(true);
                slot.setOrigin(Align.center);
                slot.addAction(Actions.scaleTo(1.08f, 1.08f, 0.07f));
            }
            @Override public void exit(InputEvent event, float x, float y, int pointer, Actor to) {
                if (pointer != -1) return;
                hoverOverlay.setVisible(false);
                slot.addAction(Actions.scaleTo(1f, 1f, 0.07f));
            }
        });
    }
}
