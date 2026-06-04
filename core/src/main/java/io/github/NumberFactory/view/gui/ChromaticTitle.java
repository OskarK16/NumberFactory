package io.github.NumberFactory.view.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;

public final class ChromaticTitle extends Actor {

    private final BitmapFont font;
    private final String text;
    private final GlyphLayout layout;
    private final float offset;

    public ChromaticTitle(BitmapFont font, String text, float offset) {
        this.font = font;
        this.text = text;
        this.offset = offset;
        this.layout = new GlyphLayout(font, text);
        setSize(layout.width, layout.height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float x = getX();
        float top = getY() + layout.height;
        float ghost = parentAlpha * 0.55f;

        font.setColor(0.72f, 0.24f, 0.28f, ghost);
        font.draw(batch, text, x - offset, top);
        font.setColor(0.26f, 0.50f, 0.70f, ghost);
        font.draw(batch, text, x + offset, top);
        font.setColor(0.92f, 0.93f, 0.95f, parentAlpha);
        font.draw(batch, text, x, top);
        font.setColor(Color.WHITE);
    }
}
