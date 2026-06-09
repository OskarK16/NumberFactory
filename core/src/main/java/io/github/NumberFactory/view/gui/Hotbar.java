package io.github.NumberFactory.view.gui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import io.github.NumberFactory.controller.ComponentPaletteSelection;
import io.github.NumberFactory.controller.HotbarController;
import io.github.NumberFactory.model.Inventory;
import io.github.NumberFactory.view.render.TextureRegistry;

import java.util.ArrayList;
import java.util.List;

public class Hotbar extends Table {

    private static final int SLOT_SIZE = 100;

    private final HotbarController hotbar;
    private final Inventory inventory;
    private final List<UtilitySlot> utilitySlots = new ArrayList<>();
    private CategorySlot arithmeticSlot;
    private CategorySlot logicSlot;

    public Hotbar(HotbarController hotbar, Inventory inventory, TextureRegistry textures, Skin skin) {
        this.hotbar = hotbar;
        this.inventory = inventory;

        setBackground(skin.getDrawable("panel"));
        pad(6);

        ComponentPaletteSelection utility = hotbar.getUtility();
        for (int i = 0; i < utility.getEntries().size(); i++) {
            final int index = i;
            ComponentPaletteSelection.Entry entry = utility.getEntries().get(i);
            UtilitySlot slot = new UtilitySlot(entry, textures, skin);
            slot.addListener(new ClickListener() {
                @Override public void clicked(InputEvent event, float x, float y) {
                    hotbar.selectFromUtility(index);
                }
            });
            utilitySlots.add(slot);
            add(slot).size(SLOT_SIZE).padRight(4);
        }

        arithmeticSlot = new CategorySlot(textures.getTemplateArithmetic(),
            textures.getLabelArithmetic(),
            textures);
        arithmeticSlot.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                if (hotbar.getOpenSub() == HotbarController.SubCategory.ARITHMETIC) hotbar.closeSub();
                else hotbar.openSub(HotbarController.SubCategory.ARITHMETIC);
            }
        });
        add(arithmeticSlot).size(SLOT_SIZE).padLeft(12).padRight(4);

        logicSlot = new CategorySlot(textures.getTemplateLogic(),
            textures.getLabelLogic(),
            textures);
        logicSlot.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                if (hotbar.getOpenSub() == HotbarController.SubCategory.LOGIC) hotbar.closeSub();
                else hotbar.openSub(HotbarController.SubCategory.LOGIC);
            }
        });
        add(logicSlot).size(SLOT_SIZE);
    }

    public void update() {
        boolean utilityActive = hotbar.getActivePalette() == hotbar.getUtility();
        int selectedIndex = hotbar.getUtility().getSelectedIndex();
        for (int i = 0; i < utilitySlots.size(); i++) {
            UtilitySlot slot = utilitySlots.get(i);
            slot.setSelected(utilityActive && i == selectedIndex);
            slot.refreshCount(inventory);
        }
        arithmeticSlot.setSelected(hotbar.getOpenSub() == HotbarController.SubCategory.ARITHMETIC);
        logicSlot.setSelected(hotbar.getOpenSub() == HotbarController.SubCategory.LOGIC);
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

    static class UtilitySlot extends Table {
        private final ComponentPaletteSelection.Entry entry;
        private final Image selectedOverlay;
        private final Label countLabel;

        UtilitySlot(ComponentPaletteSelection.Entry entry, TextureRegistry textures, Skin skin) {
            this.entry = entry;
            Stack stack = new Stack();

            TextureRegion block = textures.getBlock(entry.type());
            if (block != null) stack.add(new Image(new TextureRegionDrawable(block)));

            TextureRegion label = textures.getLabel(entry.type());
            if (label != null) stack.add(new Image(new TextureRegionDrawable(label)));

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

    static class CategorySlot extends Table {
        private final Image selectedOverlay;

        CategorySlot(TextureRegion background, TextureRegion label, TextureRegistry textures) {
            Stack stack = new Stack();
            if (background != null) stack.add(new Image(new TextureRegionDrawable(background)));
            if (label != null)      stack.add(new Image(new TextureRegionDrawable(label)));

            Image hoverOverlay = new Image(new TextureRegionDrawable(textures.getState()));
            hoverOverlay.setColor(1f, 1f, 1f, 0.45f);
            hoverOverlay.setVisible(false);
            stack.add(hoverOverlay);

            selectedOverlay = new Image(new TextureRegionDrawable(textures.getState()));
            selectedOverlay.setVisible(false);
            stack.add(selectedOverlay);
            add(stack).grow();
            installHover(this, hoverOverlay);
        }

        void setSelected(boolean selected) {
            selectedOverlay.setVisible(selected);
        }
    }
}
