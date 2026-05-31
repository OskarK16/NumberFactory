package io.github.NumberFactory.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.NumberFactory.Main;

public class InstructionsScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Skin skin;

    public InstructionsScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin();
        BitmapFont font = new BitmapFont();
        skin.add("default-font", font, BitmapFont.class);

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = font;
        titleStyle.fontColor = new Color(0.95f, 0.75f, 0.20f, 1f);
        skin.add("title", titleStyle);

        Label.LabelStyle standardTextStyle = new Label.LabelStyle();
        standardTextStyle.font = font;
        standardTextStyle.fontColor = Color.WHITE;
        skin.add("default", standardTextStyle);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.35f, 0.35f, 0.35f, 1f); pixmap.fill();
        skin.add("key-bg", new Texture(pixmap));
        pixmap.dispose();

        createColorButtonStyle("btn-understood", 0.20f, 0.60f, 0.30f, font);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();

        Label title = new Label("CONTROLS", skin, "title");
        title.setFontScale(2.5f);
        mainTable.add(title).padBottom(40).colspan(2).row();

        Table contentTable = new Table();

        Table dragKeys = new Table();
        dragKeys.add(createKey("LMB")).padRight(8);
        dragKeys.add(new Label("(Drag)", skin));

        Table zoomKeys = new Table();
        zoomKeys.add(createKey("LMB")).padRight(6);
        zoomKeys.add(new Label("+", skin)).padRight(6);
        zoomKeys.add(createKey("Scroll"));

        Table rmbKeys = new Table();
        rmbKeys.add(createKey("RMB")).padRight(8);
        rmbKeys.add(new Label("(Click)", skin));

        Table cycleKeys = new Table();
        cycleKeys.add(createKey("LMB")).padRight(6);
        cycleKeys.add(new Label("(Hover)", skin)).padRight(6);
        cycleKeys.add(createKey("Scroll"));

        Table mmbKeys = new Table();
        mmbKeys.add(createKey("MMB")).padRight(8);
        mmbKeys.add(new Label("(Click)", skin));

        Table hotbarKeys = new Table();
        hotbarKeys.add(createKey("Scroll"));

        Table numKeys = new Table();
        numKeys.add(createKey("1")).padRight(6);
        numKeys.add(new Label("-", skin)).padRight(6);
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
            Label actionLabel = new Label(actionText, skin);

            contentTable.add(keyActor).left().padRight(20).padBottom(15);
            contentTable.add(actionLabel).left().padBottom(15).row();
        }

        mainTable.add(contentTable).padBottom(30).row();

        TextButton backButton = new TextButton("Got it", skin, "btn-understood");
        mainTable.add(backButton).width(200).height(55).colspan(2);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        stage.addActor(mainTable);
    }

    private void createColorButtonStyle(String name, float r, float g, float b, BitmapFont font) {
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

        pix.setColor(r, g, b, 1f); pix.fill();
        skin.add(name + "-up", new Texture(pix));

        pix.setColor(Math.min(r + 0.15f, 1f), Math.min(g + 0.15f, 1f), Math.min(b + 0.15f, 1f), 1f); pix.fill();
        skin.add(name + "-over", new Texture(pix));

        pix.setColor(Math.max(r - 0.15f, 0f), Math.max(g - 0.15f, 0f), Math.max(b - 0.15f, 0f), 1f); pix.fill();
        skin.add(name + "-down", new Texture(pix));

        pix.dispose();

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.fontColor = Color.WHITE;
        style.overFontColor = Color.WHITE;
        style.up = new TextureRegionDrawable(new TextureRegion(skin.get(name + "-up", Texture.class)));
        style.over = new TextureRegionDrawable(new TextureRegion(skin.get(name + "-over", Texture.class)));
        style.down = new TextureRegionDrawable(new TextureRegion(skin.get(name + "-down", Texture.class)));

        skin.add(name, style);
    }

    private Table createKey(String text, float minWidth) {
        Table keyWrapper = new Table();
        keyWrapper.setBackground(new TextureRegionDrawable(new TextureRegion(skin.get("key-bg", Texture.class))));
        Label label = new Label(text, skin);
        label.setAlignment(Align.center);

        keyWrapper.add(label).pad(4, 10, 4, 10).minWidth(minWidth);
        return keyWrapper;
    }

    private Table createKey(String text) {
        return createKey(text, 0);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.08f, 0.10f, 0.14f, 1f);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
