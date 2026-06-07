package io.github.NumberFactory.view.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import io.github.NumberFactory.Main;

public class InstructionsScreen extends MenuScreen {

    public InstructionsScreen(Main app) {
        super(app);
    }

    @Override
    protected void build() {
        Actor title = theme.title("CONTROLS", 2f, 48);

        Table dragKeys = new Table();
        dragKeys.add(theme.key("LMB")).padRight(8);
        dragKeys.add(new Label("(Drag)", theme.skin(), "dim"));

        Table zoomKeys = new Table();
        zoomKeys.add(theme.key("LMB")).padRight(6);
        zoomKeys.add(new Label("+", theme.skin(), "dim")).padRight(6);
        zoomKeys.add(theme.key("Scroll"));

        Table rmbKeys = new Table();
        rmbKeys.add(theme.key("RMB")).padRight(8);
        rmbKeys.add(new Label("(Click)", theme.skin(), "dim"));

        Table cycleKeys = new Table();
        cycleKeys.add(theme.key("LMB")).padRight(6);
        cycleKeys.add(new Label("(Hover)", theme.skin(), "dim")).padRight(6);
        cycleKeys.add(theme.key("Scroll"));

        Table mmbKeys = new Table();
        mmbKeys.add(theme.key("MMB")).padRight(8);
        mmbKeys.add(new Label("(Click)", theme.skin(), "dim"));

        Table hotbarKeys = new Table();
        hotbarKeys.add(theme.key("Scroll"));

        Table numKeys = new Table();
        numKeys.add(theme.key("1")).padRight(6);
        numKeys.add(new Label("-", theme.skin(), "dim")).padRight(6);
        numKeys.add(theme.key("7"));

        Table tabKeys = new Table();
        tabKeys.add(theme.key("TAB"));

        Table spaceKeys = new Table();
        spaceKeys.add(theme.key("Space", 120));

        Object[][] instructionsList = {
            {dragKeys, "Pan camera"},
            {zoomKeys, "Zoom in / out"},
            {rmbKeys, "Build component"},
            {cycleKeys, "Cycle port"},
            {mmbKeys, "Remove component"},
            {hotbarKeys, "Scroll hotbar"},
            {numKeys, "Quick select components"},
            {tabKeys, "Next component in hotbar"},
            {spaceKeys, "Start / Pause / Resume simulation"}
        };

        Table contentTable = new Table();
        for (Object[] inst : instructionsList) {
            Table keyActor = (Table) inst[0];
            String actionText = (String) inst[1];

            contentTable.add(keyActor).left().padRight(30).padBottom(15);
            contentTable.add(new Label(actionText, theme.skin(), "default")).left().padBottom(15).row();
        }

        TextButton backButton = theme.menuButton("GOT IT", theme.green, false);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.setScreen(new MainMenuScreen(app));
            }
        });

        Table card = theme.card();
        card.add(title).padBottom(40).row();
        card.add(contentTable).padBottom(40).row();
        card.add(backButton).width(300).height(62);

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.add(card);
        stage.addActor(root);

        addFooter();
    }
}
