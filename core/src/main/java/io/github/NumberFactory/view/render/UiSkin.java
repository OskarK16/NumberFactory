package io.github.NumberFactory.view.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public final class UiSkin {

    private UiSkin() {
    }

    private static final Color PANEL_FILL = new Color(0.105f, 0.117f, 0.145f, 0.92f);
    private static final Color PANEL_BORDER = new Color(0.223f, 0.247f, 0.305f, 1f);
    private static final Color ACCENT = new Color(0.949f, 0.725f, 0.220f, 1f);
    private static final Color BTN_UP = new Color(0.160f, 0.180f, 0.225f, 1f);
    private static final Color BTN_OVER = new Color(0.223f, 0.247f, 0.305f, 1f);
    private static final Color BTN_DOWN = new Color(0.120f, 0.135f, 0.170f, 1f);
    private static final Color TEXT = new Color(0.901f, 0.913f, 0.941f, 1f);
    private static final Color TEXT_DIM = new Color(0.450f, 0.470f, 0.520f, 1f);

    private static final String FONT_PATH = "fonts/title.ttf";
    private static final int FONT_SIZE = 18;

    private static int texCounter = 0;

    public static Skin build() {
        Skin skin = new Skin();
        BitmapFont font = buildFont(FONT_SIZE);
        skin.add("default-font", font, BitmapFont.class);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = TEXT;
        skin.add("default", labelStyle);

        Label.LabelStyle dimStyle = new Label.LabelStyle();
        dimStyle.font = font;
        dimStyle.fontColor = TEXT_DIM;
        skin.add("dim", dimStyle);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = font;
        titleStyle.fontColor = ACCENT;
        skin.add("title", titleStyle);

        skin.add("panel", borderPatch(skin, PANEL_FILL, PANEL_BORDER));
        skin.add("up", borderPatch(skin, BTN_UP, PANEL_BORDER));
        skin.add("over", borderPatch(skin, BTN_OVER, ACCENT));
        skin.add("down", borderPatch(skin, BTN_DOWN, ACCENT));
        skin.add("selected", borderPatch(skin, ACCENT, PANEL_BORDER));
        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
        scrollStyle.vScrollKnob = skin.getDrawable("up");
        scrollStyle.vScroll = skin.getDrawable("panel");
        skin.add("default", scrollStyle);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = TEXT;
        buttonStyle.overFontColor = ACCENT;
        buttonStyle.downFontColor = ACCENT;
        buttonStyle.disabledFontColor = TEXT_DIM;

        buttonStyle.up = skin.getDrawable("up");
        buttonStyle.over = skin.getDrawable("over");
        buttonStyle.down = skin.getDrawable("down");

        buttonStyle.disabled = new NinePatchDrawable(borderPatch(skin, BTN_DOWN, PANEL_BORDER));
        skin.add("default", buttonStyle);

        return skin;
    }

    private static BitmapFont buildFont(int size) {
        FileHandle file = Gdx.files.internal(FONT_PATH);
        if (!file.exists()) return new BitmapFont();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(file);
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = size;
        param.color = Color.WHITE;
        param.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "·";
        param.minFilter = Texture.TextureFilter.Nearest;
        param.magFilter = Texture.TextureFilter.Nearest;
        BitmapFont font = generator.generateFont(param);
        generator.dispose();
        return font;
    }

    private static NinePatch borderPatch(Skin skin, Color fill, Color border) {
        int size = 12;
        int b = 2;
        Pixmap pm = new Pixmap(size, size, Pixmap.Format.RGBA8888);

        pm.setColor(fill);
        pm.fill();

        pm.setColor(border);
        for (int i = 0; i < b; i++) {
            pm.drawRectangle(i, i, size - 2 * i, size - 2 * i);
        }

        Texture t = new Texture(pm);
        skin.add("tex-" + (texCounter++), t);
        pm.dispose();

        return new NinePatch(t, b + 1, b + 1, b + 1, b + 1);
    }
}
