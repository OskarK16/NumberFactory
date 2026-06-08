package io.github.NumberFactory.view.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import io.github.NumberFactory.Main;
import io.github.NumberFactory.config.ConfigManager;
import io.github.NumberFactory.controller.LevelController;
import io.github.NumberFactory.save.SaveManager;
import io.github.NumberFactory.save.data.LevelDefinitionData;
import io.github.NumberFactory.save.data.LevelSaveData;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class LevelSelectScreen extends MenuScreen {

    private static final int COLUMNS = 2;
    private static final float CARD_WIDTH = 500f;
    private static final float CARD_MIN_HEIGHT = 190f;

    private static final List<String> LEVEL_ORDER = Arrays.asList(
        "ones", "powers", "countdown", "fibonacci", "collatz", "euclid", "catalan", "secret"
    );

    public LevelSelectScreen(Main app) {
        super(app);
    }

    @Override
    protected void build() {
        String alias = new ConfigManager().load().currentPlayer;
        List<String> levels = SaveManager.listDefinitions();
        levels.sort(Comparator.comparingInt(LevelSelectScreen::orderIndex)
            .thenComparing(Comparator.naturalOrder()));

        Actor title = theme.title("CAMPAIGN", 2f, 48);

        Table grid = new Table();
        grid.top();
        int col = 0;
        for (int i = 0; i < levels.size(); i++) {
            grid.add(buildLevelCard(levels.get(i), i + 1, alias))
                .width(CARD_WIDTH).minHeight(CARD_MIN_HEIGHT).top().pad(14);
            if (++col % COLUMNS == 0) grid.row();

        }

        ScrollPane pane = theme.scrollPane(grid);
        pane.setScrollingDisabled(true, false);
        stage.setScrollFocus(pane);
        pane.addListener(new InputListener() {
            @Override public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1) stage.setScrollFocus(pane);
            }
        });

        TextButton back = theme.menuButton("BACK TO MENU", theme.accent, false);
        back.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                app.setScreen(new MainMenuScreen(app));
            }
        });

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.add(title).padTop(30).padBottom(28).row();
        root.add(pane).expand().fill().pad(0, 40, 10, 40).row();
        root.add(back).width(260).height(54).padTop(8).padBottom(22).row();
        stage.addActor(root);
    }

    private static int orderIndex(String levelName) {
        int idx = LEVEL_ORDER.indexOf(levelName);
        return idx < 0 ? Integer.MAX_VALUE : idx;
    }

    private Table buildLevelCard(String levelName, int levelNumber, String alias) {
        LevelDefinitionData definition = SaveManager.loadDefinition(levelName);
        String displayTitle = levelName.toUpperCase();
        String description = "";
        if (definition != null && definition.metadata != null) {
            if (definition.metadata.name != null && !definition.metadata.name.isEmpty()) {
                displayTitle = definition.metadata.name.toUpperCase();
            }
            description = definition.metadata.description == null ? "" : definition.metadata.description;
        }

        String saveId = "camp_" + alias + "_" + levelName;
        boolean started = SaveManager.hasSave(saveId);
        boolean completed = false;
        if (started) {
            LevelSaveData data = SaveManager.loadData(saveId);
            completed = data != null && data.completed;
        }

        String statusText;
        Color statusColor;
        String buttonText;
        Color buttonColor;
        final boolean resume;
        if (!started) {
            statusText = "not started";
            statusColor = theme.green;
            buttonText = "START";
            buttonColor = theme.green;
            resume = false;
        }
        else if (!completed) {
            statusText = "not finished";
            statusColor = theme.accent;
            buttonText = "CONTINUE";
            buttonColor = theme.accent;
            resume = true;
        }
        else {
            statusText = "completed";
            statusColor = theme.blue;
            buttonText = "PLAY AGAIN";
            buttonColor = theme.blue;
            resume = false;
        }

        Table card = new Table();
        card.setBackground(theme.skin().getDrawable("panel"));
        card.pad(24, 30, 24, 30);
        card.top();

        Label title = new Label(displayTitle, theme.skin(), "default");
        title.setFontScale(1.2f);
        title.setWrap(true);
        title.setAlignment(Align.center);
        card.add(title).width(CARD_WIDTH - 60).center().padBottom(14).row();

        Label descriptionLabel = new Label(description, theme.skin(), "dim");
        descriptionLabel.setWrap(true);
        descriptionLabel.setFontScale(0.9f);
        card.add(descriptionLabel).width(CARD_WIDTH - 60).left().top().growX().padBottom(20).row();

        Label status = new Label(statusText, theme.skin(), "default");
        status.setColor(statusColor);
        status.setFontScale(0.95f);

        TextButton action = theme.menuButton(buttonText, buttonColor, false);
        action.getLabel().setFontScale(0.85f);
        action.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                LevelController controller = new LevelController(false, levelNumber, levelName, alias, null);
                app.setScreen(new LevelScreen(app, controller, resume));
            }
        });

        Table footer = new Table();
        footer.add(status).left().expandX();
        footer.add(action).width(160).height(50).right();
        card.add(footer).fillX().expandX().bottom();

        return card;
    }
}
