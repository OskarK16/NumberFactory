package io.github.NumberFactory.view.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.NumberFactory.controller.EditController;
import io.github.NumberFactory.controller.GameController;
import io.github.NumberFactory.controller.HotbarController;
import io.github.NumberFactory.model.Level;
import io.github.NumberFactory.view.render.TextureRegistry;
import io.github.NumberFactory.view.render.UiSkin;

public class LevelHud {

    private static final float PANEL_MARGIN = 8f;
    private static final float PANEL_GAP = 8f;

    private final Stage stage;
    private final Table root;
    private final Skin skin;
    private final TopBar topBar;
    private final Hotbar hotbarView;
    private final SubHotbar subHotbarView;
    private final EditPanel editPanel;
    private final Sidebar sidebar;
    private final ComponentGuidePanel guidePanel;
    private final TextButton guideHandle;

    public LevelHud(GameController game, EditController edit, HotbarController hotbar, Level level, TextureRegistry textures) {
        skin = UiSkin.build();
        stage = new Stage(new ScreenViewport());

        guidePanel = new ComponentGuidePanel(textures, skin);
        topBar = new TopBar(game, skin);
        hotbarView = new Hotbar(hotbar, level.getInventory(), textures, skin);
        subHotbarView = new SubHotbar(hotbar, textures, skin);
        editPanel = new EditPanel(edit, game, skin);
        sidebar = new Sidebar(game, skin);

        root = new Table();
        root.setFillParent(true);
        root.top();

        root.add(topBar).growX().colspan(2).row();
        root.add(sidebar).top().left().width(180).padTop(8).padLeft(8);
        root.add().expand().row();

        root.add(subHotbarView).colspan(2).padBottom(4).row();
        root.add(hotbarView).colspan(2).padBottom(8).row();
        stage.addActor(root);

        editPanel.setVisible(false);
        editPanel.pack();
        stage.addActor(editPanel);

        guidePanel.setVisible(false);
        guidePanel.pack();
        stage.addActor(guidePanel);

        guideHandle = new TextButton("GUIDE", skin);
        guideHandle.setSize(90, 40);
        guideHandle.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) { guidePanel.toggle(); }
        });
        stage.addActor(guideHandle);
    }

    public Stage getStage() {
        return stage;
    }

    public void update() {
        topBar.update();
        hotbarView.update();
        subHotbarView.update();
        editPanel.update();
        sidebar.update();
    }

    public void act(float dt) {
        stage.act(dt);
        positionEditPanel();
        positionGuideUi();
    }

    public void draw() {
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    private void positionGuideUi() {
        float handleX = stage.getWidth() - guideHandle.getWidth();
        float handleY = (stage.getHeight() - guideHandle.getHeight()) / 2f;
        guideHandle.setPosition(handleX, handleY);

        if (!guidePanel.isVisible()) return;
        guidePanel.layoutFor(stage.getWidth(), stage.getHeight());
        float x = stage.getWidth() - guidePanel.getWidth() - guideHandle.getWidth() - PANEL_MARGIN;
        float y = (stage.getHeight() - guidePanel.getHeight()) / 2f;
        guidePanel.setPosition(x, y);
    }

    private void positionEditPanel() {
        if (!editPanel.isVisible()) return;
        root.validate();
        editPanel.pack();
        float x = stage.getWidth() - editPanel.getWidth() - PANEL_MARGIN;
        float y = sidebar.getY() - PANEL_GAP - editPanel.getHeight();
        editPanel.setPosition(x, y);
    }
}
