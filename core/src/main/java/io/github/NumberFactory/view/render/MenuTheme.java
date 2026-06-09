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
import io.github.NumberFactory.view.gui.ChromaticTitle;

public final class MenuTheme implements Disposable {

    private static final String FONT_PATH  = "fonts/title.ttf";

    public final Color bgBottom;
    public final Color accent;
    public final Color green;
    public final Color purple;
    public final Color blue;
    public final Color red;
    public final Color yellow;
    public final Color text;

    private final Color surface;
    private final Color panelBorder;
    private final Color btnUp;
    private final Color btnDown;
    private final Color textDim;

    private final Skin skin = new Skin();
    private final BitmapFont uiFont;
    private int texCounter = 0;

    public MenuTheme() {
        Palette palette = new Palette();
        surface     = palette.surface;
        bgBottom    = palette.bgBottom;
        panelBorder = palette.panelBorder;
        btnUp       = palette.btnUp;
        btnDown     = palette.btnDown;
        accent      = palette.accent;
        text        = palette.text;
        textDim     = palette.textDim;
        green       = palette.green;
        purple      = palette.purple;
        blue        = palette.blue;
        red         = palette.red;
        yellow      = palette.yellow;

        uiFont = font(18);
        skin.add("default-font", uiFont, BitmapFont.class);
        skin.add("default", new Label.LabelStyle(uiFont, text));
        skin.add("dim", new Label.LabelStyle(uiFont, textDim));
        skin.add("gradient", new TextureRegionDrawable(register(Pixmaps.gradient(palette.background, bgBottom, 256))), Drawable.class);
        skin.add("grid", new TiledDrawable(new TextureRegion(register(Pixmaps.grid(palette.gridLine, 44)))), TiledDrawable.class);
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

    public Image dim() {
        Image image = new Image(new TextureRegionDrawable(new TextureRegion(solidTexture(new Color(0f, 0f, 0f, 0.55f)))));
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

    public Drawable panel(Color border) {
        return new NinePatchDrawable(borderPatch(surface, border));
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
