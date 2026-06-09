package io.github.NumberFactory.view.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.NumberFactory.controller.GameController;
import io.github.NumberFactory.controller.HotbarController;
import io.github.NumberFactory.model.Level;
import io.github.NumberFactory.view.render.TextureRegistry;
import io.github.NumberFactory.view.render.UiSkin;

public class LevelHud {

    private static final float PANEL_MARGIN = 8f;

    private final Stage stage;
    private final Table root;
    private final Skin skin;
    private final TopBar topBar;
    private final Hotbar hotbarView;
    private final SubHotbar subHotbarView;
    private final ComponentGuidePanel guidePanel;
    private final TextButton guideHandle;
    private final TextButton logHandle;
    private final Table activeSidePanel;


    public LevelHud(GameController game, HotbarController hotbar, Level level, TextureRegistry textures, Runnable onSave,
                    Runnable onReturnToMenu) {
        skin = UiSkin.build();
        stage = new Stage(new ScreenViewport());

        guidePanel = new ComponentGuidePanel(textures, skin);
        guidePanel.setVisible(false);
        guidePanel.pack();

        guideHandle = new TextButton("GUIDE", skin);
        guideHandle.setStyle(UiSkin.accentButton(skin, UiSkin.PURPLE));
        guideHandle.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) { guidePanel.toggle(); }
        });

        if (game.getCampaignGoal() != null) {
            TaskPanel taskPanel = new TaskPanel(game.getCampaignGoal(), skin);
            taskPanel.setVisible(false);
            taskPanel.pack();
            activeSidePanel = taskPanel;
            logHandle = new TextButton("TASK", skin);
            logHandle.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent event, Actor actor) { taskPanel.toggle(); }
            });
        }
        else {
            Sidebar logPanel = new Sidebar(game, skin);
            logPanel.setVisible(false);
            logPanel.pack();
            activeSidePanel = logPanel;
            logHandle = new TextButton("LOGS", skin);
            logHandle.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent event, Actor actor) { logPanel.toggle(); }
            });
        }

        logHandle.setStyle(UiSkin.accentButton(skin, UiSkin.BLUE));

        topBar = new TopBar(game, skin, onSave, onReturnToMenu, logHandle, guideHandle);
        hotbarView = new Hotbar(hotbar, level.getInventory(), textures, skin);
        subHotbarView = new SubHotbar(hotbar, level.getInventory(), textures, skin);

        root = new Table();
        root.setFillParent(true);
        root.top();

        root.add(topBar).growX().colspan(2).row();
        root.add().expand().row();

        root.add(subHotbarView).colspan(2).padBottom(4).row();
        root.add(hotbarView).colspan(2).padBottom(8).row();
        stage.addActor(root);

        stage.addActor(guidePanel);
        stage.addActor(activeSidePanel);
    }

    public Stage getStage() {
        return stage;
    }

    public void closePanels() {
        guidePanel.setVisible(false);
        activeSidePanel.setVisible(false);
    }

    public void update() {
        topBar.update();
        hotbarView.update();
        subHotbarView.update();

        if (activeSidePanel instanceof Sidebar) {
            ((Sidebar) activeSidePanel).update();
        }
        else if (activeSidePanel instanceof TaskPanel) {
            ((TaskPanel) activeSidePanel).update();
        }
    }

    private void positionLogUi() {
        if (!activeSidePanel.isVisible()) {
            return;
        }
        float x = PANEL_MARGIN;
        float y = (stage.getHeight() - activeSidePanel.getHeight()) / 2f;
        activeSidePanel.setPosition(x, y);
    }

    public void act(float dt) {
        stage.act(dt);
        positionGuideUi();
        positionLogUi();
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
        if (!guidePanel.isVisible()) return;
        guidePanel.layoutFor(stage.getWidth(), stage.getHeight());
        float x = stage.getWidth() - guidePanel.getWidth() - PANEL_MARGIN;
        float y = (stage.getHeight() - guidePanel.getHeight()) / 2f;
        guidePanel.setPosition(x, y);
    }

}
