package io.github.NumberFactory.view.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import io.github.NumberFactory.controller.GameController;
import io.github.NumberFactory.model.Goal;
//in progress
public class Sidebar extends Table {

    private final GameController game;
    private final Label progressLabel;

    public Sidebar(GameController game, Skin skin) {
        this.game = game;
        setBackground(skin.getDrawable("panel"));
        pad(8);
        top();
        add(new Label("Output", skin)).padBottom(8).row();
        progressLabel = new Label("", skin);
        add(progressLabel).left();
    }

    public void update() {
        Goal goal = game.getGoal();
        if (goal == null) {
            progressLabel.setText("(sandbox)");
            return;
        }
        progressLabel.setText(game.getAggregated().toString());
    }
}
