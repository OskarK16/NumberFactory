package io.github.NumberFactory.view.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import io.github.NumberFactory.controller.EditController;
import io.github.NumberFactory.controller.GameController;
import io.github.NumberFactory.model.board.Board;
import io.github.NumberFactory.model.board.Cell;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

public class EditPanel extends Table {

    private final EditController edit;
    private final GameController game;
    private final Label headerLabel;
    private final Label[] portLabels = new Label[Directions.values().length];

    public EditPanel(EditController edit, GameController game, Skin skin) {
        this.edit = edit;
        this.game = game;

        setBackground(skin.getDrawable("panel"));
        pad(8);

        headerLabel = new Label("", skin);
        add(headerLabel).colspan(2).padBottom(6).row();

        for (Directions dir : Directions.values()) {
            Label dirLabel = new Label(dir.name(), skin);
            Label portLabel = new Label("", skin);
            portLabels[dir.ordinal()] = portLabel;
            add(dirLabel).padRight(8).left();
            add(portLabel).left().row();
        }

        TextButton okBtn = new TextButton("OK", skin);
        TextButton noBtn = new TextButton("CANCEL", skin);
        okBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) { edit.commit(); }});
        noBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) { edit.cancel(); }});
        add(okBtn).padTop(8).width(80).height(28).padRight(6);
        add(noBtn).padTop(8).width(80).height(28);
    }

    public void update() {
        boolean active = edit.hasActiveEdit();
        setVisible(active);
        if (!active) return;
        int x = edit.getEditingX();
        int y = edit.getEditingY();
        Board board = game.getBoard();
        Cell cell = board.getCell(x, y);
        Component c = cell == null ? null : cell.getComponent();
        if (c == null) {
            headerLabel.setText("(no component)");
            for (Label l : portLabels) l.setText("");
            return;
        }
        headerLabel.setText(c.getClass().getSimpleName() + " @ (" + x + "," + y + ")");
        for (Directions d : Directions.values()) {
            PortType p = c.getPort(d);
            portLabels[d.ordinal()].setText(p == null ? "CLOSED" : p.name());
        }
    }
}
