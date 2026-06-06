package io.github.NumberFactory.view.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import io.github.NumberFactory.model.SequenceGoal;

import java.util.List;

public class TaskPanel extends Table {

    private final SequenceGoal goal;
    private final Label sequenceLabel;
    private final Label statusLabel;
    private int lastIndex = -1;
    private boolean wasFailed = false;

    public TaskPanel(SequenceGoal goal, Skin skin) {
        this.goal = goal;
        setBackground(skin.getDrawable("panel"));
        pad(24);

        ChromaticText title = new ChromaticText(goal.getTitle(), skin, 1.5f, 1.15f);
        add(title).padBottom(6).center().row();

        Label separator = new Label("--------------------------------", skin, "dim"); // TODO - do zastapoienia czyms ladniejszym
        separator.setAlignment(Align.center);
        add(separator).padBottom(16).center().row();

        Label descLabel = new Label(goal.getDescription(), skin, "default");
        descLabel.setWrap(true);
        descLabel.setAlignment(Align.topLeft);
        add(descLabel).width(360).padBottom(24).row();

        sequenceLabel = new Label("", skin, "default");
        sequenceLabel.setWrap(true);
        sequenceLabel.setAlignment(Align.topLeft);
        sequenceLabel.setFontScale(1.15f);
        sequenceLabel.getStyle().font.getData().markupEnabled = true;

        ScrollPane scrollPane = new ScrollPane(sequenceLabel, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        add(scrollPane).width(360).height(200).padBottom(20).row();

        statusLabel = new Label("", skin, "default");
        statusLabel.setAlignment(Align.center);
        statusLabel.getStyle().font.getData().markupEnabled = true;
        add(statusLabel).width(360).padBottom(20).row();

        TextButton closeBtn = new TextButton("CLOSE", skin);
        closeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toggle();
            }
        });

        add(closeBtn).width(120).height(40).center();

        forceUpdate();
    }

    public void toggle() {
        setVisible(!isVisible());
    }

    public void update() {
        if (goal.getCurrentIndex() != lastIndex || goal.isFailed() != wasFailed) {
            forceUpdate();
        }
    }

    private void forceUpdate() {
        lastIndex = goal.getCurrentIndex();
        wasFailed = goal.isFailed();

        StringBuilder sb = new StringBuilder();
        List<Integer> seq = goal.getExpectedSequence();

        for (int i = 0; i < seq.size(); i++) {
            if (i < lastIndex) {
                sb.append("[#76CFA2]").append(seq.get(i)).append("[] ");
            }
            else if (i == lastIndex && wasFailed) {
                sb.append("[#F27466]").append(seq.get(i)).append("[] ");
            }
            else {
                sb.append("[#F0F4F8]").append(seq.get(i)).append("[] ");
            }
        }
        sequenceLabel.setText(sb.toString());

        if (wasFailed) {
            statusLabel.setText("[#F27466]ERROR: Invalid sequence generated![]");
        }
        else if (goal.isComplete()) {
            statusLabel.setText("[#FADB5F]SUCCESS: Goal completed![]");
        }
        else {
            statusLabel.setText("[#707A8A]Awaiting machine output...[]");
        }
    }

    private static final class ChromaticText extends Actor {
        private final BitmapFont font;
        private final String text;
        private final GlyphLayout layout;
        private final float offset;
        private final float scale;

        ChromaticText(String text, Skin skin, float offset, float scale) {
            this.font = skin.get("title", Label.LabelStyle.class).font;
            this.text = text;
            this.offset = offset;
            this.scale = scale;

            float oldX = font.getData().scaleX;
            float oldY = font.getData().scaleY;
            font.getData().setScale(scale);
            this.layout = new GlyphLayout(font, text);
            font.getData().setScale(oldX, oldY);

            setSize(layout.width, layout.height);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            float oldX = font.getData().scaleX;
            float oldY = font.getData().scaleY;
            font.getData().setScale(scale);

            float x = getX();
            float top = getY() + layout.height;
            float ghost = parentAlpha * 0.65f;

            font.setColor(0.72f, 0.24f, 0.28f, ghost);
            font.draw(batch, text, x - offset, top);
            font.setColor(0.26f, 0.50f, 0.70f, ghost);
            font.draw(batch, text, x + offset, top);
            font.setColor(0.92f, 0.93f, 0.95f, parentAlpha);
            font.draw(batch, text, x, top);

            font.setColor(Color.WHITE);
            font.getData().setScale(oldX, oldY);
        }
    }
}
