package io.github.NumberFactory.view.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import io.github.NumberFactory.controller.GameController;
import io.github.NumberFactory.model.SimulationState;

public class TopBar extends Table {

    private final GameController game;
    private final TextButton start;
    private final TextButton pause;
    private final TextButton reset;
    private final TextButton restart;

    public TopBar(GameController game, Skin skin) {
        this.game = game;

        start = new TextButton("START",skin);
        pause = new TextButton("PAUSE",skin);
        reset = new TextButton("RESET",skin);
        restart = new TextButton("RESTART",skin);

        start.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) { game.start();}});

        pause.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.getSimulationState() == SimulationState.PAUSED) game.resume();
                else game.pause();
            }
        });
        reset.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) { game.reset(); }
        });
        restart.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) { game.restart(); }
        });

        setBackground(skin.getDrawable("panel"));
        pad(6);
        add(start).width(110).height(36).padRight(8);
        add(pause).width(110).height(36).padRight(8);
        add(reset).width(110).height(36).padRight(8);
        add(restart).width(110).height(36);
    }

    public void update() {
        SimulationState s = game.getSimulationState();
        pause.setDisabled(s != SimulationState.RUNNING && s != SimulationState.PAUSED);
        pause.setText(s == SimulationState.PAUSED ? "RESUME" : "PAUSE");
        reset.setDisabled(s == SimulationState.IDLE);
    }
}
