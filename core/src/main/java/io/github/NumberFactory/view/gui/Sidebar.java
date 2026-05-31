package io.github.NumberFactory.view.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import io.github.NumberFactory.controller.GameController;
import io.github.NumberFactory.model.Goal;

import java.util.List;

public class Sidebar extends Table {

    private final GameController game;
    private final Label progressLabel;

    private static final int MAX_HISTORY = 10;

    public Sidebar(GameController game, Skin skin) {
        this.game = game;
        setBackground(skin.getDrawable("panel"));
        pad(8);
        top().left();

        Label title = new Label("Outputu HISTORY", skin);
        add(title).padBottom(8).left().row();

        progressLabel = new Label("", skin);
        progressLabel.setAlignment(Align.topLeft);
        add(progressLabel).expand().fill().top().left();
    }

    public void update() {
        Goal goal = game.getGoal();
        List<?> history = game.getAggregated();

        if (goal == null) {
            if (history == null || history.isEmpty()) {
                progressLabel.setText("TEMP."); //TODO - ZAIMPLEMENTOWAC
                return;
            }

            StringBuilder sb = new StringBuilder();

            int start = Math.max(0, history.size() - MAX_HISTORY);

            for (int i = start; i < history.size(); i++) {
                sb.append("> ").append(history.get(i).toString()).append("\n");
            }

            progressLabel.setText(sb.toString());
        }
        else {
            progressLabel.setText(history.toString());
        }
    }
}
