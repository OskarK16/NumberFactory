package io.github.NumberFactory.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.NumberFactory.Main;

public class InstructionsScreen implements Screen {
    private static final Color BG_TOP = new Color(0.078f, 0.090f, 0.118f, 1f);
    private static final Color BG_BOTTOM = new Color(0.035f, 0.042f, 0.058f, 1f);
    private static final Color GRID_LINE = new Color(0.886f, 0.639f, 0.235f, 0.055f);
    private static final Color PANEL_FILL = new Color(0.105f, 0.117f, 0.145f, 0.92f);
    private static final Color PANEL_BORDER = new Color(0.223f, 0.247f, 0.305f, 1f);
    private static final Color ACCENT = new Color(0.949f, 0.725f, 0.220f, 1f);
    private static final Color BTN_UP = new Color(0.160f, 0.180f, 0.225f, 1f);
    private static final Color BTN_OVER = new Color(0.223f, 0.247f, 0.305f, 1f);
    private static final Color BTN_DOWN = new Color(0.120f, 0.135f, 0.170f, 1f);
    private static final Color TEXT = new Color(0.901f, 0.913f, 0.941f, 1f);
    private static final Color TEXT_DIM = new Color(0.450f, 0.470f, 0.520f, 1f);
    private static final Color C_GREEN = new Color(0.400f, 0.740f, 0.460f, 1f);

    private final Main game;
    private Stage stage;
    private Skin skin;
    private BitmapFont uiFont;
    private int texCounter = 0;

    public InstructionsScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        uiFont = buildUiFont(18);
        BitmapFont titleFont = buildTitleFont();

        skin.add("default-font", uiFont, BitmapFont.class);
        skin.add("title-font", titleFont, BitmapFont.class);
        buildStyles(uiFont, titleFont);

        Image background = new Image(skin.getDrawable("gradient"));
        background.setFillParent(true);
        stage.addActor(background);

        Image grid = new Image(skin.get("grid", TiledDrawable.class));
        grid.setFillParent(true);
        stage.addActor(grid);

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.center();

        Table card = new Table();
        card.setBackground(skin.getDrawable("panel"));
        card.pad(36, 48, 36, 48);

        Label title = new Label("CONTROLS", skin, "title");
        card.add(title).padBottom(40).colspan(2).row();

        Table contentTable = new Table();

        Table dragKeys = new Table();
        dragKeys.add(createKey("LMB")).padRight(8);
        dragKeys.add(new Label("(Drag)", skin, "dim"));

        Table zoomKeys = new Table();
        zoomKeys.add(createKey("LMB")).padRight(6);
        zoomKeys.add(new Label("+", skin, "dim")).padRight(6);
        zoomKeys.add(createKey("Scroll"));

        Table rmbKeys = new Table();
        rmbKeys.add(createKey("RMB")).padRight(8);
        rmbKeys.add(new Label("(Click)", skin, "dim"));

        Table cycleKeys = new Table();
        cycleKeys.add(createKey("LMB")).padRight(6);
        cycleKeys.add(new Label("(Hover)", skin, "dim")).padRight(6);
        cycleKeys.add(createKey("Scroll"));

        Table mmbKeys = new Table();
        mmbKeys.add(createKey("MMB")).padRight(8);
        mmbKeys.add(new Label("(Click)", skin, "dim"));

        Table hotbarKeys = new Table();
        hotbarKeys.add(createKey("Scroll"));

        Table numKeys = new Table();
        numKeys.add(createKey("1")).padRight(6);
        numKeys.add(new Label("-", skin, "dim")).padRight(6);
        numKeys.add(createKey("7"));

        Table tabKeys = new Table();
        tabKeys.add(createKey("TAB"));

        Table spaceKeys = new Table();
        spaceKeys.add(createKey("Space", 120));

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

        for (Object[] inst : instructionsList) {
            Table keyActor = (Table) inst[0];
            String actionText = (String) inst[1];
            Label actionLabel = new Label(actionText, skin, "default");

            contentTable.add(keyActor).left().padRight(30).padBottom(15);
            contentTable.add(actionLabel).left().padBottom(15).row();
        }

        card.add(contentTable).padBottom(40).row();

        TextButton backButton = menuButton("GOT IT", C_GREEN);
        card.add(backButton).width(300).height(62).colspan(2);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        rootTable.add(card).row();
        stage.addActor(rootTable);
    }

    private Table createKey(String text, float minWidth) {
        Table keyWrapper = new Table();
        keyWrapper.setBackground(skin.getDrawable("key-bg"));
        Label label = new Label(text, skin, "default");
        label.setAlignment(Align.center);

        keyWrapper.add(label).pad(4, 12, 4, 12).minWidth(minWidth);
        return keyWrapper;
    }

    private Table createKey(String text) {
        return createKey(text, 0);
    }

    private TextButton menuButton(String text, Color accent) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = uiFont;
        style.fontColor = TEXT;
        style.overFontColor = accent;
        style.downFontColor = accent;
        style.up = new NinePatchDrawable(borderPatch(BTN_UP, PANEL_BORDER));
        style.over = new NinePatchDrawable(borderPatch(BTN_OVER, accent));
        style.down = new NinePatchDrawable(borderPatch(BTN_DOWN, accent));

        TextButton button = new TextButton(text, style);
        button.setTransform(true);
        button.setOrigin(Align.center);
        button.addListener(new ClickListener() {

            @Override public void enter(InputEvent event, float x, float y, int pointer, Actor from) {
                if (pointer == -1) {
                    button.addAction(Actions.scaleTo(1.04f, 1.04f, 0.08f));
                }
            }

            @Override public void exit(InputEvent event, float x, float y, int pointer, Actor to) {
                if (pointer == -1) {
                    button.addAction(Actions.scaleTo(1f, 1f, 0.08f));
                }
            }

        });

        return button;
    }

    private BitmapFont buildTitleFont() {
        FileHandle file = Gdx.files.internal("fonts/title.ttf");
        if (file.exists()) {
            return generatePixelFont(file, 48);
        }

        BitmapFont font = new BitmapFont();
        font.getData().setScale(2.5f);
        return font;
    }

    private BitmapFont buildUiFont(int size) {
        FileHandle file = Gdx.files.internal("fonts/title.ttf");
        if (file.exists()) {
            return generatePixelFont(file, size);
        }

        return new BitmapFont();
    }

    private BitmapFont generatePixelFont(FileHandle file, int size) {
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

    private void buildStyles(BitmapFont defaultFont, BitmapFont titleFont) {
        Label.LabelStyle defaultStyle = new Label.LabelStyle(defaultFont, TEXT);
        skin.add("default", defaultStyle);

        Label.LabelStyle dimStyle = new Label.LabelStyle(defaultFont, TEXT_DIM);
        skin.add("dim", dimStyle);

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, ACCENT);
        skin.add("title", titleStyle);

        skin.add("gradient", gradientDrawable(BG_TOP, BG_BOTTOM), Drawable.class);
        skin.add("grid", gridDrawable(), TiledDrawable.class);

        skin.add("panel", new NinePatchDrawable(borderPatch(PANEL_FILL, PANEL_BORDER)), Drawable.class);
        skin.add("key-bg", new NinePatchDrawable(borderPatch(BTN_UP, PANEL_BORDER)), Drawable.class);
    }

    private TextureRegionDrawable gradientDrawable(Color top, Color bottom) {
        int h = 256;
        Pixmap pm = new Pixmap(1, h, Pixmap.Format.RGBA8888);
        Color c = new Color();
        for (int y = 0; y < h; y++) {
            c.set(top).lerp(bottom, y / (float) (h - 1));
            pm.setColor(c);
            pm.drawPixel(0, y);
        }

        TextureRegionDrawable d = new TextureRegionDrawable(register(pm));
        pm.dispose();
        return d;
    }

    private TiledDrawable gridDrawable() {
        int tile = 44;
        Pixmap pm = new Pixmap(tile, tile, Pixmap.Format.RGBA8888);
        pm.setColor(0f, 0f, 0f, 0f);
        pm.fill();
        pm.setColor(GRID_LINE);
        pm.drawLine(0, 0, tile - 1, 0);
        pm.drawLine(0, 0, 0, tile - 1);
        Texture t = new Texture(pm);
        pm.dispose();
        t.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        skin.add("grid-texture", t);
        return new TiledDrawable(new TextureRegion(t));
    }

    private NinePatch borderPatch(Color fill, Color border) {
        int size = 12;
        int b = 2;
        Pixmap pm = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pm.setColor(fill);
        pm.fill();
        pm.setColor(border);

        for (int i = 0; i < b; i++) {
            pm.drawRectangle(i, i, size - 2 * i, size - 2 * i);
        }

        Texture t = register(pm);
        pm.dispose();
        return new NinePatch(t, b + 1, b + 1, b + 1, b + 1);
    }

    private Texture register(Pixmap pm) {
        Texture t = new Texture(pm);
        skin.add("tex-" + (texCounter++), t);
        return t;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(BG_BOTTOM.r, BG_BOTTOM.g, BG_BOTTOM.b, 1f);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}

    @Override public void resume() {}

    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
