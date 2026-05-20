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
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.NumberFactory.Main;

public class CampaignScreen implements Screen {
    private static final int LEVEL_COUNT = 5;

    private final Main game;
    private Stage stage;
    private Skin skin;

    public CampaignScreen(Main game) {
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
        titleStyle.fontColor = Color.WHITE;
        skin.add("default", titleStyle);

        // buttons styles
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.25f, 0.25f, 0.25f, 1f); pixmap.fill();
        skin.add("up", new Texture(pixmap));
        pixmap.setColor(0.40f, 0.40f, 0.40f, 1f); pixmap.fill();
        skin.add("over", new Texture(pixmap));
        pixmap.setColor(0.15f, 0.15f, 0.15f, 1f); pixmap.fill();
        skin.add("down", new Texture(pixmap));
        pixmap.dispose();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.overFontColor = Color.RED;
        buttonStyle.up   = new TextureRegionDrawable(new TextureRegion(skin.get("up",   Texture.class)));
        buttonStyle.over = new TextureRegionDrawable(new TextureRegion(skin.get("over", Texture.class)));
        buttonStyle.down = new TextureRegionDrawable(new TextureRegion(skin.get("down", Texture.class)));
        skin.add("default", buttonStyle);

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(new Label("Campaign", skin)).padBottom(40).row();

        for (int i = 1; i <= LEVEL_COUNT; i++) {
            final int levelNumber = i;
            TextButton level = new TextButton("Level " + i, skin);
            level.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.setScreen(new LevelScreen(game, levelNumber));
                }
            });
            table.add(level).width(200).height(50).padBottom(15).row();
        }

        TextButton back = new TextButton("Back", skin);
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        table.add(back).width(200).height(50).padTop(20).row();

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.15f, 1f);
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
