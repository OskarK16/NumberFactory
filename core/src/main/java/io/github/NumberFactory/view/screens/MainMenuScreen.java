package io.github.NumberFactory.view.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import io.github.NumberFactory.Main;
import io.github.NumberFactory.config.ConfigManager;

public class MainMenuScreen extends MenuScreen {

    public MainMenuScreen(Main app) {
        super(app);
    }

    @Override
    protected void build() {
        String player = new ConfigManager().load().currentPlayer;

        Actor title = theme.title("NUMBER FACTORY", 2.5f, 60);

        Label subtitle = new Label("build  ·  compute  ·  deliver", theme.skin(), "dim");
        subtitle.setFontScale(0.8f);
        subtitle.setAlignment(Align.center);

        Label welcome = new Label("Welcome, " + player, theme.skin());
        welcome.setColor(theme.text);
        TextButton changePlayer = theme.menuButton("CHANGE PLAYER", theme.accent, false);
        changePlayer.getLabel().setFontScale(0.7f);
        changePlayer.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                app.setScreen(new ChoosePlayerScreen(app));
            }
        });
        Table welcomeRow = new Table();
        welcomeRow.add(welcome).padRight(18);
        welcomeRow.add(changePlayer).height(36).padLeft(6);

        Table card = theme.card();

        TextButton sandbox = theme.menuButton("SANDBOX", theme.green, false);
        TextButton campaign = theme.menuButton("CAMPAIGN (soon)", theme.accent, true);
        TextButton instructions = theme.menuButton("INSTRUCTIONS", theme.purple, false);

        card.add(sandbox).width(300).height(62).padBottom(18).row();
        card.add(campaign).width(300).height(62).padBottom(18).row();
        card.add(instructions).width(300).height(62).row();

        sandbox.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                app.setScreen(new SandboxSaveScreen(app, player));
            }
        });
        instructions.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                app.setScreen(new InstructionsScreen(app));
            }
        });

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.add(title).padBottom(18).row();
        root.add(subtitle).padBottom(28).row();
        root.add(welcomeRow).padBottom(40).row();
        root.add(card).row();
        stage.addActor(root);

        addFooter();
    }
}
