package io.github.NumberFactory.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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

public class MainMenuScreen implements Screen {

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
    private static final Color C_PURPLE = new Color(0.620f, 0.420f, 0.820f, 1f);

    private final Main game;
    private Stage stage;
    private Skin skin;
    private BitmapFont uiFont;

    public MainMenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        uiFont = buildUiFont(18);
        skin.add("default-font", uiFont, BitmapFont.class);
        buildStyles(uiFont);

        Image background = new Image(skin.getDrawable("gradient"));
        background.setFillParent(true);
        stage.addActor(background);

        Image grid = new Image(skin.get("grid", TiledDrawable.class));
        grid.setFillParent(true);
        stage.addActor(grid);

        ChromaticTitle title = new ChromaticTitle(buildTitleFont(), "NUMBER FACTORY", 2.5f);

        Label subtitle = new Label("build  ·  compute  ·  deliver", skin, "dim");
        subtitle.setFontScale(0.8f);
        subtitle.setAlignment(Align.center);

        Table card = new Table();
        card.setBackground(skin.getDrawable("panel"));
        card.pad(36, 48, 36, 48);

        TextButton sandbox = menuButton("SANDBOX", C_GREEN, false);
        TextButton campaign = menuButton("CAMPAIGN (soon)", ACCENT, true);
        TextButton instructions = menuButton("INSTRUCTIONS", C_PURPLE, false);

        card.add(sandbox).width(300).height(62).padBottom(18).row();
        card.add(campaign).width(300).height(62).padBottom(18).row();
        card.add(instructions).width(300).height(62).row();

        sandbox.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new LevelScreen(game, 0));
            }
        });
        instructions.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new InstructionsScreen(game));
            }
        });

        Table root = new Table();
        root.setFillParent(true);
        root.center();
        root.add(title).padBottom(18).row();
        root.add(subtitle).padBottom(54).row();
        root.add(card).row();
        stage.addActor(root);

        Label footer = new Label("Dominik  ·  Józef  ·  Oskar", skin, "dim");
        footer.setFontScale(0.7f);
        Table footerBar = new Table();
        footerBar.setFillParent(true);
        footerBar.bottom();
        footerBar.add(footer).padBottom(18);
        stage.addActor(footerBar);
    }

    private TextButton menuButton(String text, Color accent, boolean disabled) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = uiFont;
        style.fontColor = TEXT;
        style.overFontColor = accent;
        style.downFontColor = accent;
        style.disabledFontColor = TEXT_DIM;
        style.up = new NinePatchDrawable(borderPatch(BTN_UP, disabled ? PANEL_BORDER : accent));
        style.over = new NinePatchDrawable(borderPatch(BTN_OVER, accent));
        style.down = new NinePatchDrawable(borderPatch(BTN_DOWN, accent));
        style.disabled = new NinePatchDrawable(borderPatch(BTN_DOWN, PANEL_BORDER));

        TextButton button = new TextButton(text, style);
        button.setDisabled(disabled);
        if (disabled) return button;

        button.setTransform(true);
        button.setOrigin(Align.center);
        button.addListener(new ClickListener() {
            @Override public void enter(InputEvent event, float x, float y, int pointer, Actor from) {
                if (pointer == -1) button.addAction(Actions.scaleTo(1.04f, 1.04f, 0.08f));
            }
            @Override public void exit(InputEvent event, float x, float y, int pointer, Actor to) {
                if (pointer == -1) button.addAction(Actions.scaleTo(1f, 1f, 0.08f));
            }
        });
        return button;
    }

    private BitmapFont buildTitleFont() {
        FileHandle file = Gdx.files.internal("fonts/title.ttf");
        BitmapFont font;
        if (file.exists()) font = generatePixelFont(file, 60);
        else {
            font = new BitmapFont();
            font.getData().setScale(3.4f);
        }
        skin.add("title-font", font, BitmapFont.class);
        return font;
    }

    private BitmapFont buildUiFont(int size) {
        FileHandle file = Gdx.files.internal("fonts/title.ttf");
        if (file.exists()) return generatePixelFont(file, size);
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

    private void buildStyles(BitmapFont font) {
        skin.add("dim", new Label.LabelStyle(font, TEXT_DIM));

        skin.add("gradient", gradientDrawable(BG_TOP, BG_BOTTOM), Drawable.class);
        skin.add("grid", gridDrawable(), TiledDrawable.class);
        skin.add("panel", new NinePatchDrawable(borderPatch(PANEL_FILL, PANEL_BORDER)), Drawable.class);
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
    private int texCounter = 0;

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

    private static final class ChromaticTitle extends Actor {
        private final BitmapFont font;
        private final String text;
        private final GlyphLayout layout;
        private final float offset;

        ChromaticTitle(BitmapFont font, String text, float offset) {
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

}
