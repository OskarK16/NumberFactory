package io.github.NumberFactory.view.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public final class Palette {

    private static final String THEME_PATH = "ui/theme.json";

    public final Color background;
    public final Color bgBottom;
    public final Color surface;
    public final Color panelBorder;
    public final Color gridLine;
    public final Color btnUp;
    public final Color btnDown;
    public final Color accent;
    public final Color text;
    public final Color textDim;
    public final Color green;
    public final Color purple;
    public final Color blue;
    public final Color red;
    public final Color yellow;

    public Palette() {
        JsonValue c = load();
        background  = color(c, "background");
        bgBottom    = color(c, "bgBottom");
        surface     = color(c, "surface");
        panelBorder = color(c, "panelBorder");
        gridLine    = color(c, "gridLine");
        btnUp       = color(c, "btnUp");
        btnDown     = color(c, "btnDown");
        accent      = color(c, "accent");
        text        = color(c, "text");
        textDim     = color(c, "textDim");
        green       = color(c, "green");
        purple      = color(c, "purple");
        blue        = color(c, "blue");
        red         = color(c, "red");
        yellow      = color(c, "yellow");
    }

    private static JsonValue load() {
        FileHandle file = Gdx.files.internal(THEME_PATH);
        if (file.exists()) return new JsonReader().parse(file);
        return new JsonValue(JsonValue.ValueType.object);
    }

    private static Color color(JsonValue colors, String key) {
        String hex = colors.getString(key, null);
        if (hex == null || hex.isEmpty()) return new Color(Color.GRAY);
        return Color.valueOf(hex);
    }
}
