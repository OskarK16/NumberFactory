package io.github.NumberFactory.view.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import io.github.NumberFactory.controller.GameController;
import io.github.NumberFactory.model.Goal;
import io.github.NumberFactory.model.SimulationLogger;

import java.util.List;

public class Sidebar extends Table {

    private final GameController game;
    private final Label progressLabel;

    private static final int MAX_HISTORY = 10;
    private int lastHistorySize = -1; // TODO - opisac

    public Sidebar(GameController game, Skin skin) {
        this.game = game;
        setBackground(skin.getDrawable("panel"));
        pad(12);
        top().left();

        Label title = new Label("OUTPUT HISTORY", skin);
        add(title).padBottom(8).left().row();

        progressLabel = new Label("Awaiting..", skin);
        progressLabel.setAlignment(Align.topLeft);
        add(progressLabel).expand().fill().top().left();
    }

    public void update() {
//        Goal goal = game.getGoal();
//        List<?> history = game.getAggregated();
        SimulationLogger logger = game.getActionLogger();

        if (logger == null) {
            return;
        }

        if (logger.getModificationCount() == lastHistorySize) {
            return;
        }

        lastHistorySize = logger.getModificationCount();
        List<String> logs = logger.getLogs();

        if (logs.isEmpty()) {
            progressLabel.setText("Awaiting2"); //TODO - zmienic
            return;
        }

        Goal goal = game.getGoal();
        StringBuilder sbuilder = new StringBuilder();

        if (goal != null) {
            sbuilder.append("Target: ").append(goal.toString()).append("\n\n");
        }

        int start = Math.max(0, logs.size() - MAX_HISTORY);

        for (int i = start; i < logs.size(); i++) {
            sbuilder.append(">> ").append(logs.get(i).toString()).append("\n");
        }

        progressLabel.setText(sbuilder.toString());
    }
}
