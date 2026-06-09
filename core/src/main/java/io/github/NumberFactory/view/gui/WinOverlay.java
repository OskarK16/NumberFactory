package io.github.NumberFactory.view.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.NumberFactory.model.SequenceGoal;
import io.github.NumberFactory.view.render.MenuTheme;

public class WinOverlay implements Disposable {

    private final Stage stage;
    private final MenuTheme theme;

    public WinOverlay(SequenceGoal goal, Runnable onContinue, Runnable onPlayAgain,
                      Runnable onLevelSelect, Runnable onMainMenu) {
        theme = new MenuTheme();
        stage = new Stage(new ScreenViewport());
        stage.addActor(theme.dim());
        build(goal, onContinue, onPlayAgain, onLevelSelect, onMainMenu);
    }

    private void build(SequenceGoal goal, Runnable onContinue, Runnable onPlayAgain,
                       Runnable onLevelSelect, Runnable onMainMenu) {
        Actor titleLine = theme.title("LEVEL COMPLETED", 2.5f, 52);
        Actor cheerLine = theme.title("SJUPER!!!", 2f, 40);

        Label solved = new Label(goal.getTitle() + "  ·  solved", theme.skin(), "default");
        solved.setColor(theme.green);
        solved.setAlignment(Align.center);

        TextButton continueBtn = theme.menuButton("CONTINUE", theme.blue, false);
        TextButton playAgain = theme.menuButton("PLAY AGAIN", theme.green, false);
        TextButton levelSelect = theme.menuButton("LEVEL SELECT", theme.accent, false);
        TextButton mainMenu = theme.menuButton("MAIN MENU", theme.red, false);

        continueBtn.addListener(run(onContinue));
        playAgain.addListener(run(onPlayAgain));
        levelSelect.addListener(run(onLevelSelect));
        mainMenu.addListener(run(onMainMenu));

        Table card = theme.card();
        card.add(solved).padBottom(28).row();
        card.add(continueBtn).width(300).height(62).padBottom(18).row();
        card.add(playAgain).width(300).height(62).padBottom(18).row();
        card.add(levelSelect).width(300).height(62).padBottom(18).row();
        card.add(mainMenu).width(300).height(62).row();

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.add(titleLine).padBottom(4).row();
        root.add(cheerLine).padBottom(30).row();
        root.add(card).row();
        stage.addActor(root);
    }

    public Stage getStage() {
        return stage;
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void act(float delta) {
        stage.act(delta);
    }

    public void draw() {
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        theme.dispose();
    }

    private static ChangeListener run(Runnable action) {
        return new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                action.run();
            }
        };
    }
}
