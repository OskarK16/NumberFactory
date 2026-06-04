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
    private final TextButton startBtn;
    private final TextButton pauseBtn;
    private final TextButton resetBtn;
    private final TextButton restartBtn;
    private final TextButton saveBtn;
    private final TextButton menuBtn;

    public TopBar(GameController game, Skin skin, Runnable onSave, Runnable onReturnToMenu) {
        this.game = game;

        startBtn = new TextButton("START", skin);
        pauseBtn = new TextButton("PAUSE", skin);
        resetBtn = new TextButton("RESET", skin);
        restartBtn = new TextButton("RESTART", skin);
        saveBtn   = new TextButton("SAVE", skin);
        menuBtn = new TextButton("MENU", skin);

        startBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                game.start();
            }
        });

        pauseBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (game.getSimulationState() == SimulationState.PAUSED) {
                    game.resume();
                }
                else {
                    game.pause();
                }
            }
        });

        resetBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                game.reset();
            }
        });

        restartBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                game.restart();
            }
        });

        menuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if (onReturnToMenu != null) {
                    onReturnToMenu.run();
                }
            }
        });

        saveBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                onSave.run();
            }
        });

        setBackground(skin.getDrawable("panel"));
        pad(6);
        add(startBtn).width(110).height(36).padRight(8);
        add(pauseBtn).width(110).height(36).padRight(8);
        add(resetBtn).width(110).height(36).padRight(8);
        add(restartBtn).width(110).height(36).padRight(32);
        add(saveBtn).width(110).height(36).padRight(8);
        add(menuBtn).width(110).height(36);
    }

    public void update() {
        SimulationState s = game.getSimulationState();
        boolean running = s == SimulationState.RUNNING || s == SimulationState.PAUSED;

        pauseBtn.setDisabled(!running);
        pauseBtn.setText(s == SimulationState.PAUSED ? "RESUME" : "PAUSE");
        resetBtn.setDisabled(s == SimulationState.IDLE);
        saveBtn.setDisabled(running);
    }
}
