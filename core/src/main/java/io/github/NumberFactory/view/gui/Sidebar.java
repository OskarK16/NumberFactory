package io.github.NumberFactory.view.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import io.github.NumberFactory.controller.GameController;
import io.github.NumberFactory.model.Goal;
import io.github.NumberFactory.model.SimulationLogger;

import java.util.List;

public class Sidebar extends Table {

    private final GameController game;
    private final Label progressLabel;
    private int lastModCount = -1;

    public Sidebar(GameController game, Skin skin) {
        this.game = game;
        setBackground(skin.getDrawable("panel"));
        pad(24);

        Label title = new Label("ACTION LOG", skin, "title");
        add(title).padBottom(16).center().row();

        progressLabel = new Label("Awaiting input...", skin, "default");
        progressLabel.setAlignment(Align.topLeft);
        progressLabel.setWrap(true);
        progressLabel.getStyle().font.getData().markupEnabled = true;

        ScrollPane scrollPane = new ScrollPane(progressLabel, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        add(scrollPane).width(360).height(400).padBottom(20).row();

        TextButton closeBtn = new TextButton("CLOSE", skin);
        closeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toggle();
            }
        });

        add(closeBtn).width(120).height(40).center();
    }

    public void toggle() {
        setVisible(!isVisible());
    }

    public void update() {
        SimulationLogger logger = game.getActionLogger();
        if (logger == null) {
            return;
        }

        if (logger.getModificationCount() == lastModCount) {
            return;
        }
        lastModCount = logger.getModificationCount();

        List<String> logs = logger.getLogs();
        if (logs.isEmpty()) {
            progressLabel.setText("Awaiting input...");
            return;
        }

        Goal goal = game.getGoal();
        StringBuilder sbuilder = new StringBuilder();

        if (goal != null) {
            sbuilder.append("[#FFFFFF]Target: ").append(goal.toString()).append("[]\n\n");
        }

        for (String logMsg : logs) {
            sbuilder.append("> ").append(colorizeLog(logMsg)).append("\n");
        }

        progressLabel.setText(sbuilder.toString());
    }

    private String colorizeLog(String logMsg) {
        String colorHex = "[#FFFFFF]";

        if (logMsg.startsWith("Added") || logMsg.startsWith("Subtracted") || logMsg.startsWith("Multiplied") || logMsg.startsWith("Divided") || logMsg.startsWith("Modulo")) {
            colorHex = "[#B300FF]";
        }
        else if (logMsg.startsWith("Copied")) {
            colorHex = "[#00FF37]";
        }
        else if (logMsg.startsWith("Generated")) {
            colorHex = "[#0044FF]";
        }
        else if (logMsg.startsWith("Destroyed")) {
            colorHex = "[#FF0000]";
        }
        else if (logMsg.startsWith("Compared")) {
            colorHex = "[#FF9900]";
        }
        else if (logMsg.startsWith("Returned")) {
            colorHex = "[#FFD900]";
        }

        return colorHex + logMsg + "[]";
    }
}
