package io.github.NumberFactory.view.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

final class Pixmaps {

    private Pixmaps() {}

    static Texture solid(Color color) {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(color);
        pm.fill();
        return texture(pm);
    }

    static Texture gradient(Color top, Color bottom, int height) {
        Pixmap pm = new Pixmap(1, height, Pixmap.Format.RGBA8888);
        Color c = new Color();
        for (int y = 0; y < height; y++) {
            c.set(top).lerp(bottom, y / (float) (height - 1));
            pm.setColor(c);
            pm.drawPixel(0, y);
        }
        return texture(pm);
    }

    static Texture grid(Color line, int tile) {
        Pixmap pm = new Pixmap(tile, tile, Pixmap.Format.RGBA8888);
        pm.setColor(0f, 0f, 0f, 0f);
        pm.fill();
        pm.setColor(line);
        pm.drawLine(0, 0, tile - 1, 0);
        pm.drawLine(0, 0, 0, tile - 1);
        Texture t = texture(pm);
        t.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        return t;
    }

    static Texture border(Color fill, Color border, int size, int thickness) {
        Pixmap pm = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pm.setColor(fill);
        pm.fill();
        pm.setColor(border);
        for (int i = 0; i < thickness; i++) {
            pm.drawRectangle(i, i, size - 2 * i, size - 2 * i);
        }
        return texture(pm);
    }

    private static Texture texture(Pixmap pm) {
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }
}
