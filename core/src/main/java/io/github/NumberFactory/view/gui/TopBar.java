package io.github.NumberFactory.view.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

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

    public TopBar(GameController game, Skin skin, Runnable onSave, Runnable onReturnToMenu,
                  TextButton logButton, TextButton guideButton) {
        this.game = game;

        startBtn = new TextButton("START", skin);
        pauseBtn = new TextButton("PAUSE", skin);
        resetBtn = new TextButton("RESET", skin);
        restartBtn = new TextButton("RESTART", skin);
        saveBtn   = new TextButton("SAVE", skin);
        menuBtn = new TextButton("MENU", skin);


        for (final TextButton btn : new TextButton[]{logButton, startBtn, pauseBtn, resetBtn, restartBtn, saveBtn, menuBtn, guideButton}) {
            btn.getLabelCell().padLeft(14).padRight(14);
            btn.setTransform(true);
            btn.addListener(new ClickListener() {
                @Override public void enter(InputEvent e, float x, float y, int pointer, Actor from) {
                    if (pointer == -1 && !btn.isDisabled()) {
                        btn.setOrigin(Align.center);
                        btn.addAction(Actions.scaleTo(1.06f, 1.06f, 0.08f));
                    }
                }
                @Override public void exit(InputEvent e, float x, float y, int pointer, Actor to) {
                    if (pointer == -1) btn.addAction(Actions.scaleTo(1f, 1f, 0.08f));
                }
            });
        }

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
        pad(10);
        add(logButton).minWidth(128).height(48).padRight(12);
        add(startBtn).minWidth(128).height(48).padRight(10);
        add(pauseBtn).minWidth(128).height(48).padRight(10);
        add(resetBtn).minWidth(128).height(48).padRight(10);
        add(restartBtn).minWidth(128).height(48).padRight(36);
        add(saveBtn).minWidth(128).height(48).padRight(10);
        add(menuBtn).minWidth(128).height(48).padRight(12);
        add(guideButton).minWidth(128).height(48);
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
