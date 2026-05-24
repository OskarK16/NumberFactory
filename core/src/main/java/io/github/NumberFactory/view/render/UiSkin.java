package io.github.NumberFactory.view.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public final class UiSkin {

    private UiSkin() {}

    private static final Color PANEL = new Color(0.105f, 0.117f, 0.145f, 0.95f);
    private static final Color BTN_UP = new Color(0.176f, 0.196f, 0.243f, 1f);
    private static final Color BTN_OVER = new Color(0.243f, 0.270f, 0.325f, 1f);
    private static final Color BTN_DOWN = new Color(0.130f, 0.145f, 0.180f, 1f);
    private static final Color ACCENT= new Color(0.886f, 0.639f, 0.235f, 1f);
    private static final Color TEXT = new Color(0.901f, 0.913f, 0.941f, 1f);
    private static final Color TEXT_DIM = new Color(0.400f, 0.420f, 0.470f, 1f);

    public static Skin build() {
        Skin skin = new Skin();
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font, BitmapFont.class);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = TEXT;
        skin.add("default", labelStyle);

        skin.add("up", solidTexture(BTN_UP));
        skin.add("over", solidTexture(BTN_OVER));
        skin.add("down", solidTexture(BTN_DOWN));
        skin.add("selected", solidTexture(ACCENT));
        skin.add("panel",solidTexture(PANEL));

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = TEXT;
        buttonStyle.overFontColor = ACCENT;
        buttonStyle.downFontColor = ACCENT;
        buttonStyle.disabledFontColor = TEXT_DIM;
        buttonStyle.up = drawable(skin, "up");
        buttonStyle.over = drawable(skin, "over");
        buttonStyle.down = drawable(skin, "down");
        buttonStyle.disabled = drawable(skin, "down");
        skin.add("default", buttonStyle);

        return skin;
    }

    private static TextureRegionDrawable drawable(Skin skin, String name) {
        return new TextureRegionDrawable(new TextureRegion(skin.get(name, Texture.class)));
    }

    private static Texture solidTexture(Color color) {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(color);
        pm.fill();
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }
}
