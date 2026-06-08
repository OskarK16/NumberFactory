package io.github.NumberFactory.view.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import io.github.NumberFactory.view.gui.ChromaticTitle;

public final class MenuTheme implements Disposable {

    private static final String THEME_PATH = "ui/theme.json";
    private static final String FONT_PATH  = "fonts/title.ttf";

    public final Color bgBottom;
    public final Color accent;
    public final Color green;
    public final Color purple;
    public final Color blue;
    public final Color text;

    private final Color panelBorder;
    private final Color btnUp;
    private final Color btnDown;
    private final Color textDim;

    private final Skin skin = new Skin();
    private final BitmapFont uiFont;
    private int texCounter = 0;

    public MenuTheme() {
        JsonValue c = loadColors();
        Color background = color(c, "background");
        Color surface = color(c, "surface");
        Color gridLine = color(c, "gridLine");
        bgBottom = color(c, "bgBottom");
        panelBorder = color(c, "panelBorder");
        btnUp = color(c, "btnUp");
        btnDown = color(c, "btnDown");
        accent = color(c, "accent");
        text = color(c, "text");
        textDim = color(c, "textDim");
        green = color(c, "green");
        purple = color(c, "purple");
        blue = color(c, "blue");

        uiFont = font(18);
        skin.add("default-font", uiFont, BitmapFont.class);
        skin.add("default", new Label.LabelStyle(uiFont, text));
        skin.add("dim", new Label.LabelStyle(uiFont, textDim));
        skin.add("gradient", new TextureRegionDrawable(register(Pixmaps.gradient(background, bgBottom, 256))), Drawable.class);
        skin.add("grid", new TiledDrawable(new TextureRegion(register(Pixmaps.grid(gridLine, 44)))), TiledDrawable.class);
        skin.add("panel", new NinePatchDrawable(borderPatch(surface, panelBorder)), Drawable.class);
        skin.add("key", new NinePatchDrawable(borderPatch(btnUp, panelBorder)), Drawable.class);
    }

    public Skin skin() { return skin; }

    public Image background() {
        Image image = new Image(skin.getDrawable("gradient"));
        image.setFillParent(true);
        return image;
    }

    public Image grid() {
        Image image = new Image(skin.get("grid", TiledDrawable.class));
        image.setFillParent(true);
        return image;
    }

    public Actor title(String text, float offset, int size) {
        BitmapFont font = font(size);
        skin.add("title-font-" + size, font, BitmapFont.class);
        return new ChromaticTitle(font, text, offset);
    }

    public Table card() {
        Table card = new Table();
        card.setBackground(skin.getDrawable("panel"));
        card.pad(36, 48, 36, 48);
        return card;
    }

    public Image divider() {
        return new Image(new TextureRegionDrawable(new TextureRegion(solidTexture(panelBorder))));
    }

    public Table key(String text) {
        return key(text, 0);
    }

    public Table key(String text, float minWidth) {
        Table wrapper = new Table();
        wrapper.setBackground(skin.getDrawable("key"));
        Label label = new Label(text, skin, "default");
        label.setAlignment(Align.center);
        wrapper.add(label).pad(4, 12, 4, 12).minWidth(minWidth);
        return wrapper;
    }

    public TextButton menuButton(String label, Color accent, boolean disabled) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = uiFont;
        style.fontColor = text;
        style.overFontColor = accent;
        style.downFontColor = accent;
        style.disabledFontColor = textDim;
        style.up = new NinePatchDrawable(borderPatch(btnUp, disabled ? panelBorder : accent));
        style.over = new NinePatchDrawable(borderPatch(panelBorder, accent));
        style.down = new NinePatchDrawable(borderPatch(btnDown, accent));
        style.disabled = new NinePatchDrawable(borderPatch(btnDown, panelBorder));

        TextButton button = new TextButton(label, style);
        button.setDisabled(disabled);
        if (disabled) return button;

        button.setTransform(true);
        button.setOrigin(Align.center);
        button.addListener(new ClickListener() {
            @Override public void enter(InputEvent e, float x, float y, int pointer, Actor from) {
                if (pointer == -1) button.addAction(Actions.scaleTo(1.04f, 1.04f, 0.08f));
            }
            @Override public void exit(InputEvent e, float x, float y, int pointer, Actor to) {
                if (pointer == -1) button.addAction(Actions.scaleTo(1f, 1f, 0.08f));
            }
        });
        return button;
    }

    public TextField textField(String placeholder) {
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = uiFont;
        style.fontColor = text;
        style.cursor = new TextureRegionDrawable(new TextureRegion(solidTexture(accent)));
        style.selection = new TextureRegionDrawable(new TextureRegion(solidTexture(new Color(accent.r, accent.g, accent.b, 0.35f))));
        style.background = new NinePatchDrawable(borderPatch(btnUp, panelBorder));
        TextField field = new TextField("", style);
        field.setMessageText(placeholder);
        return field;
    }

    public ScrollPane scrollPane(Table content) {
        ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();
        style.vScroll = new TextureRegionDrawable(new TextureRegion(solidTexture(btnUp)));
        style.vScrollKnob = new TextureRegionDrawable(new TextureRegion(solidTexture(accent)));
        ScrollPane pane = new ScrollPane(content, style);
        pane.setFadeScrollBars(false);
        return pane;
    }

    @Override
    public void dispose() {
        skin.dispose();
    }

    private JsonValue loadColors() {
        FileHandle file = Gdx.files.internal(THEME_PATH);
        if (file.exists()) {
            return new JsonReader().parse(file);
        }
        Gdx.app.error("MenuTheme", "Missing asset " + THEME_PATH + ", falling back to grey palette");
        return new JsonValue(JsonValue.ValueType.object);
    }

    private static Color color(JsonValue colors, String key) {
        String hex = colors.getString(key, null);
        if (hex == null || hex.isEmpty()) return new Color(Color.GRAY);
        return Color.valueOf(hex);
    }

    private BitmapFont font(int size) {
        FileHandle file = Gdx.files.internal(FONT_PATH);
        if (!file.exists()) {
            BitmapFont font = new BitmapFont();
            font.getData().setScale(size / 18f);
            return font;
        }
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

    private NinePatch borderPatch(Color fill, Color border) {
        return new NinePatch(register(Pixmaps.border(fill, border, 12, 2)), 3, 3, 3, 3);
    }

    private Texture solidTexture(Color color) {
        return register(Pixmaps.solid(color));
    }

    private Texture register(Texture texture) {
        skin.add("tex-" + (texCounter++), texture);
        return texture;
    }
}
