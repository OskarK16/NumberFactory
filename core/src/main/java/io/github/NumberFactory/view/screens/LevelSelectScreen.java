package io.github.NumberFactory.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.NumberFactory.Main;
import io.github.NumberFactory.controller.LevelController;
import io.github.NumberFactory.view.render.UiSkin;

public class LevelSelectScreen implements Screen {

    private static final Color BG_TOP = Color.valueOf("14171eff");
    private static final Color BG_BOTTOM = Color.valueOf("090b0fff");
    private static final Color GRID_LINE = Color.valueOf("e2a33c0e");

    private final Main game;
    private Stage stage;
    private Skin skin;

    private Texture gradientTexture;
    private Texture gridTexture;

    public LevelSelectScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        skin = UiSkin.build();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        gradientTexture = createGradientTexture(BG_TOP, BG_BOTTOM);
        Image background = new Image(new TextureRegionDrawable(gradientTexture));
        background.setFillParent(true);
        stage.addActor(background);

        gridTexture = createGridTexture();
        TiledDrawable tiledGrid = new TiledDrawable(new TextureRegion(gridTexture));
        Image grid = new Image(tiledGrid);
        grid.setFillParent(true);
        stage.addActor(grid);

        Table root = new Table();
        root.setFillParent(true);
        root.center();

        Label title = new Label("CAMPAIGN PROBLEMS", skin, "title");
        title.setFontScale(1.5f);
        root.add(title).padBottom(40).row();

        Table card = new Table();
        card.setBackground(skin.getDrawable("panel"));
        card.pad(36, 48, 36, 48);

        /*
        TODO BEGIN
        poniższy kod powinien ładować poziomy na podstawie zawartości w folderze save/savesystem/levels
        powinno się wyjąć z nich tytuł i może opis oraz trzeba wtedy zerkąć na savy które ewentualnie istnieją
        i sprawdzić czy completed: true
        wtedy pole text mogłoby wyświetlić np:
        1. fibonacci - not started
        2. catalan - completed
         */

        card.add(createLevelButton("1. FIBONACCI", 1, "fibonacci")).width(300).height(62).padBottom(18).row();
        card.add(createLevelButton("2. CATALAN", 2, "catalan")).width(300).height(62).padBottom(18).row();

        TextButton backBtn = new TextButton("BACK TO MENU", skin);
        backBtn.addListener(
            new ChangeListener() {
                @Override public void changed(ChangeEvent event, Actor actor) {
                    game.setScreen(new MainMenuScreen(game));
                }
            });
        // TODO END
        card.add(backBtn).width(300).height(62);

        root.add(card);
        stage.addActor(root);
    }

    private TextButton createLevelButton(String text, int levelNumber, String levelName) {
        TextButton btn = new TextButton(text, skin);
        btn.addListener(
            new ChangeListener() {
                @Override public void changed(ChangeEvent event, Actor actor) {
                    LevelController levelCtrl = new LevelController(false, levelNumber, levelName, "player1", null);
                    game.setScreen(new LevelScreen(game, levelCtrl));
                }
            });
        return btn;
    }

    private Texture createGradientTexture(Color top, Color bottom) {
        int h = 256;
        Pixmap pm = new Pixmap(1, h, Pixmap.Format.RGBA8888);
        Color c = new Color();

        for (int y = 0; y < h; y++) {
            c.set(top).lerp(bottom, y / (float) (h - 1));
            pm.setColor(c);
            pm.drawPixel(0, y);
        }
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }

    private Texture createGridTexture() {
        int tile = 44;

        Pixmap pm = new Pixmap(tile, tile, Pixmap.Format.RGBA8888);
        pm.setColor(0f, 0f, 0f, 0f);
        pm.fill();
        pm.setColor(GRID_LINE);
        pm.drawLine(0, 0, tile - 1, 0);
        pm.drawLine(0, 0, 0, tile - 1);

        Texture t = new Texture(pm);
        pm.dispose();
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

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();

        if (gradientTexture != null) {
            gradientTexture.dispose();
        }
        if (gridTexture != null) {
            gridTexture.dispose();
        }
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
